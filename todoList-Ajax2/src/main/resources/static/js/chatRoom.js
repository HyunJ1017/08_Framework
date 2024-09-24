const sendChatting = document.querySelector("#sendChatting");
const inputChat = document.querySelector("#inputChat");
const chatViewer = document.querySelector("#chatViewer");


/* 멤버번호로 멤버이름 불러오기 */
function selectMemberName(memberNo){
  fetch("/chat/selectName?memberNo=" + memberNo)
  .then(response => {
    if(response.ok){
      return response.text();
    }
    throw new Error("이름찾기에러" + response.status);
  })
  .then(result => {
    console.log(result);
    return result;
  })
  .catch(err => console.log(err));
}


/* 채팅 메세지창 최신화 */
function chatView(){
  
  // 채팅창 호출
  const chatViewer = document.querySelector("#chatViewer");
  const loginMemberNickname = document.querySelector("#loginMemberNickname").value;

  fetch("/chat/selectChat")
  .then(response => {
    if(response.ok){
      return response.json();
    }
    throw new Error("메세지에러" + response.status);
  })
  .then(result => {

    /* str : 코드누적, preNick 이전 닉네임 기억용 */
    let str = '';
    let preNick = '';

    /* 불러온 채팅에 하나씩 접근 */
    for( let key of result ){

      // console.log(key);
      let justifycontent = '';

      // 접속자 닉네임과 불러온 닉네임을 비교해서 좌우 정렬 선택
      if(loginMemberNickname === key.memberNickname) justifycontent= 'justifyRight';
      else justifycontent = 'justifyLeft';
      
      // 채팅 입력자의 닉네임이 바뀔때만 출력
      if(preNick !== key.memberNickname){
        str = str + '<div class="' + justifycontent + '"><pre class="nameBoxPre">' + key.memberNickname + '</pre></div>';
      }

      // 방금 비교한 닉네임을 다음에 비교할 닉네임에 저장
      preNick = key.memberNickname;
      
      // 채팅내용 출력
      str = str + '<div class="' + justifycontent + '"><pre class="chatBodyPre">' + key.chatBody + '</pre><pre class="chatDatePre">' + key.creationDate + '</pre></div>';
    }

    // 누적된 str을 채팅화면에 출력
    chatViewer.innerHTML = str;

    // 스크롤바를 최하단으로 이동
    chatViewer.scrollTop = chatViewer.scrollHeight;
  })
  .catch(err => console.log(err));

}

// 3초마다 최신 메시지를 가져옴
setInterval(chatView, 3000);

/* 보내기 버튼 클릭시 */
sendChatting.addEventListener("click", ()=>{

  /* 입력내용 불러오기 */
  const chatBody = inputChat.value.trim();
  // alert(chatBody);

  const memberNo = document.querySelector("#memberNo").value;
  // alert(memberNo);

  let obj = {};
  obj.memberNo = memberNo;
  obj.chatBody = chatBody;

  fetch("/chat/insertChat",{
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
  .then(response => {
    console.log(obj["chatBody"]);
    if (response.ok) return response.text();
    throw new Error("수정 실패: " + response.status);
  })
  .then(result => {
    console.log(result); // 1 | 0

    if (result > 0) { // 수정 성공
      console.log("입력 성공");
    } else { // 실패
      console.log("입력 실패");
    }

    chatView();
    inputChat.value = '';
  })
  .catch(err => console.error(err));

});

document.addEventListener("DOMContentLoaded", () => chatView() );