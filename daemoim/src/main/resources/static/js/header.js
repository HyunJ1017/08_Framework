
    // 팝업 불러오기 함수
    const popupViewCheck = () => {

      
      fetch("/popup/popupCheck")
      .then(response => {
        if(response.ok) return response.json();
        throw new Error("popup창 실패");
      })
      .then(map => {

        console.log("popup result : ", map.result);

        // 결과 0이면 종료
        if(map.result === 0 ) return;
        
        // 전달받은 이미지 표시
        const popupImage = document.getElementById("popupImage");
        popupImage.src = map.popup.popupLocation + map.popup.popupRename;
        
        // 이미지가 로드된 후 창 크기 변경
        popupImage.onload = function() {

          const popupView = document.querySelector(".popup-view");

          // 이미지의 실제 크기를 기준으로 창 크기 설정
          popupView.style.width = `${popupImage.naturalWidth}px`;
          popupView.style.height = `${popupImage.naturalHeight}px`;

          // 창 위치설정
          popupView.style.top = map.popup.popupUp + 'vh';
          popupView.style.left = map.popup.popupLeft + 'vw';

          popupView.classList.remove("display-non");
        };

      })
      .catch(err => console.error(err));

    };

    // 팝업창 닫기버튼 함수
    const addPopupcloseBtn = () => {
      const popupCheckClose = document.querySelector("#popupCheckClose");
      popupCheckClose.addEventListener("submit", e => {
        e.preventDefault();
        const popupCheck = document.querySelector("#popupCheck");
        let check = 0;
        if(popupCheck.checked == true) {
          // + 체크 활성화시 쿠키남기기 ajax 실행
          check = 1;
        }

        // 되돌려서 세션에 팝업 열었다고 남기고,
        // 하루동안 안뜨게 했으면 쿠키남기기
        fetch("/popup/popupClose?check=" + check)
        .then(response=>{
          if(response.ok === true) console.log("채팅닫기잘됨");
          if(response.ok === false) throw new Error("채팅닫기오류");
        })
        .catch(err => console.error(err));

        // 비동기 보내놓고 창 닫기
        const popupView = document.querySelector(".popup-view");
        popupView.classList.add("display-non");

      });
    };

    // 페이지 로딩시 함수세팅
    document.addEventListener("DOMContentLoaded", ()=>{
      if(document.querySelector(".popup-view") !== null){
        popupViewCheck();
        addPopupcloseBtn();
      }
    });



// ----------------------------------------------------------------
/* 알림창 */
const connectSse = () => {

  if(notificationLoginCheck === false) return;

  const eventSource = new EventSource("/sse/connect")


eventSource.addEventListener("message", e => {
  console.log("SSE 메시지 수신:", e.data);

  const obj = JSON.parse(e.data);
  console.log(obj);

  // 알림 모양 변경
  const notificationBtn = document.querySelector(".styled-notification-btn");
  notificationBtn.classList.add("fa-solid");
  notificationBtn.classList.remove("fa-regular");


  const notificationCountArea = document.querySelector(".notification-count-area");
  
  if (obj.notiCount !== undefined) {
    notificationCountArea.innerText = obj.notiCount;
  } else {
    console.error("알림 개수를 업데이트할 수 없습니다.");
  }

  const notificationList  
      = document.querySelector(".notification-list");

    if(notificationList.classList.contains("notification-show")){
      selectNotificationList();
    }

    notReadCheck();
});

eventSource.addEventListener("error", () => {
    console.log("서버 연결이 종료되어 재연결을 시도합니다.");

    eventSource.close();

    setTimeout(() => connectSse(), 5000);
  })

}



// 알림 메시지 전송
const sendNotification = (content, sendMemberNo, receiveMemberNo, groupNo, url, type) => {

  /* 
  content          : 알림 내용
  sendMemberNo     : 보낸 회원 번호
  receiveMemberNo  : 받는 회원 번호
  groupNo          : 모임 번호
  url              : 연결 페이지 url
  type             : 알림 종류

  */


  if(notificationLoginCheck === false) return;

  // 서버로 제출할 데이터 JS 형태로 저장
  const notification = {
    "notificationContent" : content, 
    "sendMemberNo" : sendMemberNo, 
    "receiveMemberNo" : receiveMemberNo, 
    "groupNo" : groupNo, 
    "notificationUrl" : url,
    "notificationType" : type
  }

  fetch("/sse/send", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body: JSON.stringify(notification)
  })

  .then(response => {
    if (!response.ok) { 
      throw new Error("알림 전송 실패");
    }
    console.log("알림 전송 성공");
  })
  .catch(err => console.error(err));
}

// 비동기로 알림 목록 조회
// 알림 읽음 처리
const updateNotification = (notificationNo) => {
  return fetch(`/notification?notificationNo=${notificationNo}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ notificationNo }) 
  })
  .then(response => {
    console.log("응답 상태 확인:", response.status);

      if (response.ok) {
        notReadCheck();
        const notificationItem = document.querySelector(`.notification-item[data-notification-id='${notificationNo}']`);
        if (notificationItem) {
          notificationItem.classList.remove('not-read');
          notificationItem.classList.add('read');
        }
      } else {
          throw new Error("알림 읽음 처리 실패");
      }
  })
  .catch(err => console.error(err));
};

// 전체 알림 읽음 처리
const updateAllNotifications = () => {
  fetch(`/notification/all`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" }
  })
  .then(response => {
      if (response.ok) {
          console.log("전체 알림 읽음 처리 성공");
          selectNotificationList();
          notReadCheck();

      } else {
          throw new Error("전체 알림 읽음 처리 실패");
      }
  })
  .catch(err => console.error(err));
};

// 알림 삭제 기능 수정
const deleteNotification = (notificationNo) => {
  fetch(`/notification/${notificationNo}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" }
  })
  .then(response => {
      if (response.ok) {
          console.log("알림 삭제 성공");
          notReadCheck();
          document.querySelector(`.notification-item[data-notification-id='${notificationNo}']`)?.remove();
      } else {
          throw new Error("알림 삭제 실패");
      }
  })
  .catch(err => console.error(err));
};

// 비동기로 알림 목록 조회
const selectNotificationList = () => {
  if (notificationLoginCheck === false) return;

  fetch("/notification")
      .then(response => {
          if (response.ok) return response.json();
          throw new Error("알림 목록 조회 실패!");
      })
      .then(selectList => {
          console.log(selectList);

          const notiList = document.querySelector(".notification-list");
          notiList.innerHTML = '';

          // 알림이 없을 때 빈 목록 메시지 표시
          if (selectList.length === 0) {
              const emptyMessage = document.createElement("div");
              emptyMessage.className = 'no-notifications';
              emptyMessage.innerText = '새 알림 없음!';
              notiList.appendChild(emptyMessage);
              return;
          }

          // 전체 읽음 버튼 생성
          const allReadBtn = document.createElement("button");
          allReadBtn.className = 'all-read-btn';
          allReadBtn.innerText = '전체 읽음';
          allReadBtn.addEventListener("click", () => updateAllNotifications());
          notiList.appendChild(allReadBtn);

          // 전체 삭제 버튼 생성
          const allDeleteBtn = document.createElement("button");
          allDeleteBtn.className = 'all-delete-btn';
          allDeleteBtn.innerText = '알림 전체 삭제';
          allDeleteBtn.addEventListener("click", () => {
              fetch(`/notification/all`, {
                  method: "DELETE"
              })
              .then(response => {
                  if (response.ok) {
                      console.log("전체 알림 삭제 완료");
                      document.querySelector(".notification-list").innerHTML = '';
                      notReadCheck();
                  } else {
                      throw new Error("전체 알림 삭제 실패");
                  }
              })
              .catch(err => console.error(err));
          });
          notiList.appendChild(allDeleteBtn);

          // 개별 알림 목록 생성
          for (let data of selectList) {
              const notiItem = document.createElement("li");
              notiItem.className = 'notification-item';
              notiItem.setAttribute('data-notification-id', data.notificationNo);

              if (data.notificationCheck === 'N') {
                  notiItem.classList.add("not-read");
              } else {
                  notiItem.classList.add("read");
              }

              const notiText = document.createElement("div");
              notiText.className = 'notification-text';

              // 클릭 시 읽음 처리
              notiText.addEventListener("click", (e) => {
                e.preventDefault();
                if (data.notificationCheck === 'N') {
                  // 읽지 않은 알림 클릭 시 업데이트 후 이동
                  updateNotification(data.notificationNo)
                    .then(() => {
                      window.location.href = data.notificationUrl;
                    })
                    .catch(err => console.error("알림 업데이트 오류: ", err));
                } else {
                  // 이미 읽은 알림 클릭 시 바로 이동
                  window.location.href = data.notificationUrl;
                }
              });

              const senderProfile = document.createElement("img");
              senderProfile.src = data.sendMemberProfileImg || notificationDefaultImage;

              const contentContainer = document.createElement("div");
              contentContainer.className = 'notification-content-container';

              const notiDate = document.createElement("p");
              notiDate.className = 'notification-date';
              notiDate.innerText = data.notificationDate;

              const notiContent = document.createElement("p");
              notiContent.className = 'notification-content';
              notiContent.innerHTML = data.notificationContent;

              const notiDelete = document.createElement("span");
              notiDelete.className = 'notification-delete';
              notiDelete.innerHTML = '&times;';
              notiDelete.addEventListener("click", () => deleteNotification(data.notificationNo));

              notiList.append(notiItem);
              notiItem.append(notiText, notiDelete);
              notiText.append(senderProfile, contentContainer);
              contentContainer.append(notiDate, notiContent);
          }

          notReadCheck(); // 알림 개수 업데이트
      })
      .catch(err => console.error(err));
};





/* 읽지 않은 알림 개수 조회 및 
   알림 유무 표시 여부 변경 */
  const notReadCheck = () => {

    // 로그인 되어있지 않으면 리턴
    if(!notificationLoginCheck) return; 
  
    fetch("/notification/notReadCheck")
    .then(response => {
      if(response.ok) return response.text();
      throw new Error("알림 개수 조회 실패");
    })
    .then(count => {
      console.log("알림 개수 : ", count);
  
      const notificationBtn = document.querySelector(".styled-notification-btn i");
      const notificationCountArea = document.querySelector(".notification-count-area");
  
      // 알림 개수를 화면에 표시
      notificationCountArea.innerText = count;
  
      // 읽지 않은 알림 수가 0보다 크다면
      // == 읽지 않은 알림이 존재한다! == 노란색으로 불들어오게 하기
      if(count > 0){
        notificationBtn.classList.add("fa-solid");
        notificationBtn.classList.remove("fa-regular");
        notificationCountArea.style.display = "block";
      }
      else { // 모든 알림을 읽은 상태
        notificationBtn.classList.add("fa-regular");
        notificationBtn.classList.remove("fa-solid");
        notificationCountArea.style.display = "none";
      }
    })
    .catch(err => console.error(err));
  
  }
  
  
  
  
  // ----------------------------------------------


// 페이지 로딩 완료 후 수행
document.addEventListener("DOMContentLoaded", () => {
  const notificationBtn = document.querySelector(".styled-notification-btn");

  connectSse(); // SSE 연결
  notReadCheck(); // 알림 수 확인

  notificationBtn.addEventListener("click", () => {
      let notificationList = document.querySelector(".notification-list");

      if (!notificationList) {
          notificationList = document.createElement("div");
          notificationList.className = "notification-list";
          document.body.appendChild(notificationList);
      }

      notificationList.classList.toggle("notification-show");
      if (notificationList.classList.contains("notification-show")) {
          selectNotificationList();
      }
  });


  /* 쿼리스트링 중 cn(댓글 번호)가 존재하는 경우
     해당 댓글을 찾아 화면을 스크롤해서 이동하기
  */
 
  // 쿼리스트링 다룰 수 있는 객체
  const params = new URLSearchParams(location.search);
  const cn = params.get("cn"); // cn 값 얻어오기

  if(cn != null){
    const targetId = "c" + cn; // "c100", "c1234" 형태로 변환

    // 아이디가 일치하는 댓글 요소 얻어오기
    const target = document.getElementById(targetId);

    // 댓글 요소가 제일 위에서 얼만큼 떨어져 있는지 반환 받기
    const scrollPosition = target.offsetTop;

    // 창을 스크롤
    window.scrollTo({
      top : scrollPosition - 200 , // 스크롤 할 길이
      behavior : "smooth" // 부드럽게 동작(행동)
    });
  }
});


