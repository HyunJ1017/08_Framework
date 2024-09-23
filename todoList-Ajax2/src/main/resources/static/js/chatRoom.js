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
    return '"' + result + '"';
  })
  .catch(err => console.log(err));
}

/* 채팅 메세지창 최신화 */
function chatView(){

  const chatViewer = document.querySelector("#chatViewer");

  fetch("/chat/selectChat")
  .then(response => {
    if(response.ok){
      return response.json();
    }
    throw new Error("메세지에러" + response.status);
  })
  .then(result => {
    let str;
    let preNo = -1;
    // 이전 멤버번호와 다른사람이라면 멤버 닉네임 불러와서 띄우고
    // 같으면 그냥 메세지만 출력

    for( let key of result ){
      // console.log(key);
      
      if(preNo !== key.memberNo){
        str = str + '<div><pre class="nameBoxPre">' + selectMemberName(key.memberNo) + '</pre></div>';
      }
      preNo = key.memberNo;

      str = str + '<div><pre class="chatBoxPre">' + key.chatBody + '</pre></div>';
    }

    chatViewer.innerHTML = str;
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

  const memberNo = document.querySelector("#memberNo").innerText;
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

