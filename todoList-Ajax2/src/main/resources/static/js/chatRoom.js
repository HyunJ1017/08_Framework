const sendChatting = document.querySelector("#sendChatting");
const inputChat = document.querySelector("#inputChat");
const chatViewer = document.querySelector("#chatViewer");

/* 보내기 버튼 클릭시 */
sendChatting.addEventListener("click", ()=>{

  /* 입력내용 불러오기 */
  const chatBody = inputChat.value.trim();
  // alert(chatBody);

  const memberNo = document.querySelector("#memberNo").innerText;
  // alert(memberNo);

  let obj = {};
  obj.chatBody = chatBody;
  obj.memberNo = memberNo;

  fetch("/chat/insertChat",{
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
  .then(response => {
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

  })
  .catch(err => console.error(err));


});

/* 메세지창 최신화 */