
/* 페이지 로딩이 완료된 후 a태그 클릭 재설정 */
document.addEventListener("DOMContentLoaded", () => {
  
  document.querySelector("#logoLink").addEventListener('click', function(e) {
    e.preventDefault(); // 바로 페이지 이동하지 않도록 방지
    const logo = document.getElementById('homeLogo');
    
    // 로고에 확대 클래스 추가
    logo.classList.add('expand');
  
    // 애니메이션이 끝난 후 페이지 이동
    setTimeout(function() {
      window.location.href = '/'; // 메인 페이지로 이동
    }, 600); // CSS 애니메이션 시간과 일치시킴
  });
  
});

/*
[알림기능 구현]

SSE (Server - Sent - Event)

. 기본적으로 / 요청 : 클라이언트, 응답 : 서버
. SSE에서는  / 요청 : 서버      , 응답 : 클라이언트

.- 서버가 클라이언트에게 실시간으로 데이터를 전송할 수 있는 기술
.- HTTP 프로토콜 기반으로 동작
.- 스프링-웹에 이쁘게 들어있음
.- implementation 'org.springframework.boot:spring-boot-starter-web'
.- 단방향 통신 (ex : 무전기)


[동작순서]

1) 클라이언트가 서버에 연결 요청
.  -> 클라이언트가 서버로부터 데이터를 받기 위한 대기상태에 놓임
.     (EventSource)
.   유저 1  연결요청 ◢
.                       서버
.   유저 2  연결요청 ◥

2) 서버가 연결된 클라이언트에게 데이터를 전달
.  (서버 -> 클라이언트 데이터 전달하라는 요청을 AJAX를 이용해 비동기 요청)
.   유저 1  요청 ◢
.                  서버
.   유저 2  응답 ◣


*/

/* 
[SSE 연결하는 함수]

연결을 요청한 클라이언트가 서버로부터 데이터가 전달될 때 까지 대기상태가 됨(비동기)

*/
const connectSse = () => {
  
  // 로그인되어있지않으면 함수종료
  if(notificationLoginCheck === false) return;

  console.log("conectSse() 호출");

  // 서버의 "/sse/connect" 주소로 연결요청
  const eventSource = new EventSource("/sse/connect");

  // ------------------------------------------------------------------------------

  /* 서버로부터 메시지가 왔을 경우 */
  eventSource.addEventListener("message", e => {
    console.log("0. :" + e.data); // e.data === 전달받은 메세지
                         // Spring에 HttpMassageConverter가 JSON으로 변환해서 응답해줌

    const obj = JSON.parse(e.data);
    console.log("1. :" + obj);

    // 종 버튼에 색 추가(활성화)
    const notificationBtn = document.querySelector(".notification-btn ");
    notificationBtn.classList.add("fa-shake");

    // 알림 갯수 표시
    const notificationCountArea = document.querySelector(".notification-count-area");
    notificationCountArea.innerText = obj.notiCount;



    /* 메세지가 왔을때 알림목록창이 열려있을경우 */
    const notificationList = document.querySelector(".notification-list");
    if(notificationList.classList.contains("notification-show")){
      selectNotificationList(); // 알림목록 비동기조회
    }
  }) // event end

  /* 서버 연결이 종료된 경우(타임아웃 초과) */
  eventSource.addEventListener("error", () => {
    console.log("SSE 재연결 시도");

    eventSource.close(); // 기존 연결 닫기

    setTimeout(()=>{connectSse()}, 5000); // 5초 후 재연결 시도
     
  }) // event end

} // connectSse() end



/* 
알림 메시지 전송 함수
- 알림을 받을 특정 클라이언트의 id
  (memberNo 또는 memberNo를 알아낼 수 있는 값)

[동작 원리] 
1) AJAX를 이용해 SseController에 요청
2) 연결된 클라이언트 대기명단(emitters)에서
.  클라이언트 id가 일치하는 회원을 찾아 메시지 전달하는 send() 메서드를 수행
3) 서버로부터 메세지를 전달받은 클라이언트의
.  eventSource의 message를 받은 이벤트가 동작함

*/

const sendNotification = (type, url, pkNo, content) => {

  // 로그인되어있지않으면 함수종료
  if(notificationLoginCheck === false) return;

  /* 서버로 제출할 데이터를 js객체 행태로 저장하는 구문 */
  const obj = {
    "notificationType"    : type,   // 댓글, 답글, 게시글 좋아요 등을 구분하는 값
    "notificationUrl"     : url,    // 알림 클릭 시 이동할 페이지 주소
    "pkNo"                : pkNo,   // 알림 받는 회원 번호 또는 회원번호를 찾을 수 있는 pk값
    "notificationContent" : content // 알림 내용
  };

  fetch("/sse/send", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(response => {
    if(!response.ok) {
    throw new Error("알림 전송 실패 " + response.status);
    } else {
      console.log("알림전송성공");
    }
  }) 
  // .then() => 알림보낸사람은 할게 없음
  .catch(err => console.error( err ));

}; // sendNotification() end

/* ************************************************************************ */

/* 비동기로 알림 목록을 조회하는 함수 */
const selectNotificationList = () => {
  if(notificationLoginCheck === false) return;

  fetch("/notification")
  .then(response => {
    if( response.ok ) return response.json();
    throw new Error("알림 목록 조회 실패 : " + response.status);
  })
  .then(selectList =>{
    console.log(selectList);
    // 이전 알림 목록 삭제
    const notiList = document.querySelector(".notification-list");
    notiList.innerHTML = '';

    for (let data of selectList) {

      // 알림 전체를 감싸는 요소
      const notiItem = document.createElement("li");
      notiItem.className = 'notification-item';


      // 알림을 읽지 않은 경우 'not-read' 추가
      if (data.notificationCheck == 'N') notiItem.classList.add("not-read");


      // 알림 관련 내용(프로필 이미지 + 시간 + 내용)
      const notiText = document.createElement("div");
      notiText.className = 'notification-text';


      // 알림 클릭 시 동작
      notiText.addEventListener("click", e => {

        // 만약 읽지 않은 알람인 경우
        if (data.notificationCheck == 'N') {
          fetch("/notification", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: data.notificationNo
          })
          // 컨트롤러 메서드 반환값이 없으므로 then 작성 X
        }

        // 클릭 시 알림에 기록된 경로로 이동
        location.href = data.notificationUrl;
      }) // event end


      // 알림 보낸 회원 프로필 이미지
      const senderProfile = document.createElement("img");
      if (data.sendMemberProfileImg == null) senderProfile.src = notificationDefaultImage;  // 기본 이미지
      else senderProfile.src = data.sendMemberProfileImg; // 프로필 이미지


      // 알림 내용 영역
      const contentContainer = document.createElement("div");
      contentContainer.className = 'notification-content-container';

      // 알림 보내진 시간
      const notiDate = document.createElement("p");
      notiDate.className = 'notification-date';
      notiDate.innerText = data.notificationDate;

      // 알림 내용
      const notiContent = document.createElement("p");
      notiContent.className = 'notification-content';
      notiContent.innerHTML = data.notificationContent; // 태그가 해석 될 수 있도록 innerHTML

      // 삭제 버튼
      const notiDelete = document.createElement("span");
      notiDelete.className = 'notidication-delete';
      notiDelete.innerHTML = '&times;';


      /* 삭제 버튼 클릭 시 비동기로 해당 알림 지움 */
      notiDelete.addEventListener("click", e => {

        fetch("/notification", {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          body: data.notificationNo
        })
        .then(resp => {
          if (resp.ok){
            notiDelete.parentElement.remove();
            notReadCheck();
            return;
          }
          throw new Error("네트워크 응답이 좋지 않습니다.");
        }) // fetch end

      }) // event end

      // 조립
      notiList.append(notiItem);
      notiItem.append(notiText, notiDelete);
      notiText.append(senderProfile, contentContainer);
      contentContainer.append(notiDate, notiContent);

    } // for end
    
  })
  .catch(IWantGoHome => console.error(IWantGoHome));
}; // selectNotificationList() end






/* ************************************************************************ */

  /* 읽지않은 알림갯수 조회 및 알림 유무 표시여부 변경 */
const notReadCheck = () => {
  if(notificationLoginCheck === false) return;

  fetch("/notification/notReadCheck")
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("알림 갯수 조회 실패" + response.status);
  })
  .then(count => {
    // console.log(count);

    // 종 버튼에 색 초기화(비활성화)
    const notificationBtn = document.querySelector(".notification-btn ");
    notificationBtn.classList.remove("fa-shake");
    
    // 알림 갯수 표시
    const notificationCountArea = document.querySelector(".notification-count-area");
    notificationCountArea.innerText = count;
    

    // 종 버튼에 색 추가(활성화)
    if(count < 1) return;
    notificationBtn.classList.add("fa-shake");
  })
  .catch(e=>console.error(e));

}; // notReadCheck() end


/* ************************************************************************ */

document.addEventListener("DOMContentLoaded", ()=>{
  // 서버에 sse연결요청
  connectSse();

  // 서버에 읽지않은 알림메시지 확인요청
  notReadCheck();

  // 알림버튼 클릭시 알림창 이벤트
  const notificationBtn = document.querySelector(".notification-btn");
  notificationBtn?.addEventListener("click", () => {

    const notificationList = document.querySelector(".notification-list");


    // 알림 목록이 보이고 있을 경우
    if(notificationList.classList.contains("notification-show")){
      notificationList.classList.remove("notification-show");
    } else {// 안보이는 경우
      selectNotificationList();
      notificationList.classList.add("notification-show");
    }

  });


  /* 쿼리스트링중 cn이 존재하는 경우 해당 댓글을 찾아 화면을 스크롤해서 이동하는 잡기술 */
  const params = new URLSearchParams(location.search);
  const cn = params.get("cn");
  if(cn !== null){
    const searchId = 'c' + cn;
    const target = document.getElementById(searchId);
    // const target = document.querySelector(`"#${searchId}"`);
    // -> 이거로 얻어오면 못씀 ㅜㅜ
    // 댓글 요소가 제일 위에서 얼만큼 떨어져 있는지 반환받기
    const scrollPosition = target.offsetTop

    window.scrollTo({
      top : scrollPosition - 200, // 스크롤 길이 - 적당히
      behavior : "smooth"   // 부드업게 동작
    })
  }

}); // DOMContentLoaded() end