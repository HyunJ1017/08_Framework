const chatSection = document.querySelector(".chat-boxSec");
const chatBoxGroupName = document.querySelector(".chat-topBody");
const chatBoxNickname = document.querySelector(".chat-nickDiv");
const chatBoxContents = document.querySelector(".chat-contentDiv");
const chatBtnOff = document.querySelector(".chat-offBtn");
const chatBtnOn = document.querySelector(".chat-onBtn");

let openChatRoom = null;
let scrollFlag = true;

/* ******************************************************** */
/* 채팅창 크기변경 */


const chatBody = document.querySelector(".chat-body");
const chatUls = document.querySelectorAll(".chat-body ul");
const resizer = document.createElement("div");
resizer.classList.add("resizer");
chatBody.appendChild(resizer);

let startX, startY, startWidth, startHeight;
resizer.addEventListener("mousedown", (e) => {
  e.preventDefault();
  
  startX = e.clientX;
  startY = e.clientY;
  startWidth = chatBody.offsetWidth;
  startHeight = chatBody.offsetHeight;

  window.addEventListener("mousemove", resize);
  window.addEventListener("mouseup", stopResize);

});

function resize(e) {
  const newWidth = Math.max(406, Math.min(806, startWidth - (e.clientX - startX)));
  const newHeight = Math.max(406, Math.min(window.innerHeight * 0.55, startHeight - (e.clientY - startY)));

  chatBody.style.width = newWidth + "px";
  chatBody.style.height = newHeight + "px";

  chatUls[0].style.height = (newHeight - 175) + 'px';
  chatUls[1].style.height = (newHeight - 175) + 'px';
}

function stopResize() {
  window.removeEventListener("mousemove", resize);
  window.removeEventListener("mouseup", stopResize);
}

chatBoxGroupName.addEventListener("wheel", (e) => {
  e.preventDefault();
  container.scrollLeft += e.deltaY; // 휠로 가로 스크롤
});



/* ******************************************************** */
/* 채팅창 열기 기능 */

// 가입목록과, 마지막읽은 모임번호를 가져오는 함수
const chatInfo = (memberNo) => {


  fetch("/chat/getChatInfo?memberNo=" + memberNo)
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("")
  })
  .then(map => {

    const groupList = map.groupList;

    chatBoxGroupName.innerHTML = '';
    let allUnReaded = 0;
    groupList.forEach(group => {
      const div = document.createElement("div");
      if(group.unreadCount < 1){
        div.innerText = group.groupName;
      } else {
        const span = document.createElement("span");
        span.innerText = group.unreadCount;
        allUnReaded += group.unreadCount;
        span.classList.add("chat-unReaded");
        span.classList.add(group.groupNo);
        div.innerText = group.groupName;
        div.append(span);
      }
      div.dataset.groupNo = group.groupNo;
      div.classList.add("chat-groupName");
      div.addEventListener("click", ()=>{openChat(group.groupNo);});
      chatBoxGroupName.append(div);
    });

    if(allUnReaded>0) document.querySelector(".chat-onBtn > .chat-unReaded").innerText = allUnReaded;

    // 채팅창이 켜진게 아니라면 모임별 읽지않은 메세지만 가져오고 stop;
    if(!chatBtnOn.classList.contains("display-none")) return;

    openChat(map.lastReaded);

    // 채팅목록 가져오는동안 창 열기
    chatSection.classList.remove("display-none");
    chatBody.classList.remove("display-none");

  })
  .catch(err => {
    console.error(err);
  });

}; // chatInfo end


// 로그인 목록, 채팅컨탠츠 가져오는 함수
const openChat = (groupNo) => {

  openChatRoom = groupNo; // 현재 열려있는 채팅창번호 저장
  if(groupNo < 1){
    chatUls[1].innerHTML = '';
    chatUls[1].innerText = '채팅방을 선택해 주세요';
    return;
  }
  
  // 채팅 컨탠츠 가져오기
  fetch("/chat/getContent?groupNo=" + groupNo + "&loginMemberNo=" + chatLoginMemberNo)
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("")
  })
  .then(chatList => {

    if(chatList === null){
      chatUls[1].innerHTML = '채팅내역이 없습니다';
      return;
    }

    // 클릭한 모임버튼 강조
    const chatGroupArr = document.querySelectorAll(".chat-groupName");
    chatGroupArr.forEach((chatGroup) => {
      const btnGroupNo = chatGroup.dataset.groupNo;
      if(btnGroupNo == groupNo){
        chatGroup.classList.add("slectedGroup");
        chatGroup.querySelector(".chat-unReaded")?.remove();
        
      } else {
        chatGroup.classList.remove("slectedGroup");
      }
    })
    
    document.querySelector(".chat-onBtn > .chat-unReaded").innerText = '';

    chatUls[1].innerHTML = '';
    chatList.forEach(chat => {
      if(chatLoginMemberNo !== chat.memberNo) contentOthers(chat);
      else contentSelf(chat);
    });

    
    chatUls[1].style.height = (chatBody.offsetHeight - 175) + 'px';
    if(scrollFlag){
      chatUls[1].scrollTop = chatUls[1].scrollHeight; // 스크롤 제일 밑으로
    }
    /* websocket으로 회원닉네임리스트 호출 */
    const obj = {
      "loginMemberNo" : chatLoginMemberNo,
      "groupNo" : groupNo
    };
    memsocket.send( JSON.stringify(obj) );

  })
  .catch(err => {
    console.error(err);
  });

}; // openChat() end

// 상대방 채팅 만들기
const contentOthers = (chat) => {
  const li = document.createElement("li");
    li.classList.add("chat-contentOthers");
    const div1 = document.createElement("div");
      div1.classList.add("chat-memberNinkname");
      div1.innerText = chat.memberNickname;
      let url;
      if(chat.memberProfileUrl) url = chat.memberProfileUrl;
      else url = "/images/default.png"
      if (url) {
        const img = new Image();
        img.src = url;
        img.alt = "profile";
        img.style.width = "45px";
        img.style.height = "45px";
        img.style.borderRadius = "5px";
        div1.prepend(img);
      }

    const div2 = document.createElement("div");
      const div21 = document.createElement("div");
        div21.classList.add("chat-contents")
        div21.innerText = (chat.chatContent);
      const div22 = document.createElement("div");
        div22.classList.add("chat-comments");
        const div221 = document.createElement("div");
          div221.classList.add("chat-date");
          if(chat.chatWriteDate === null){
            div221.innerText = '방금전';
          } else {
            div221.innerText = chat.chatWriteDate;
          }
        const div222 = document.createElement("div");
          div222.classList.add("chat-unReaded");
          if(chat.chatCount > 0){
            div222.innerText = chat.chatCount;
          }
      div22.append(div221, div222);
    div2.append(div21, div22);
  li.append(div1, div2);
  chatUls[1].appendChild(li);
};
// 내가쓴 채팅 만들기
const contentSelf = (chat) => {
  const li = document.createElement("li");
    li.classList.add("chat-contentSelf");
    const div1 = document.createElement("div");
      div1.classList.add("chat-comments");
      const div11 = document.createElement("div");
      div11.classList.add("chat-date");
      if(chat.chatWriteDate === null){
        div11.innerText = '방금전';
      } else {
        div11.innerText = chat.chatWriteDate;
      }
      const div12 = document.createElement("div");
      div12.classList.add("chat-unReaded");
      if(chat.chatCount > 0){
        div12.innerText = chat.chatCount;
      }
    div1.append(div11, div12);
    const div2 = document.createElement("div");
      div2.classList.add("chat-contents");
      div2.innerText = (chat.chatContent);
  li.append(div1, div2);
  chatUls[1].appendChild(li);
};


// 채팅 아이콘클릭시 위 함수 시행
chatBtnOn?.addEventListener("click", ()=>{
  chatBtnOn.classList.add("display-none");
  chatBtnOff.classList.remove("display-none");
  scrollFlag = true;
  chatInfo(chatLoginMemberNo);
  addCloseChat();
});


/* ******************************************************** */
/* 채팅창 닫기 기능 */


const addCloseChat = () => {
  chatSection.addEventListener("click", (e) => {
    // 채팅창 닫기
    chatSection.classList.add("display-none");
    chatBody.classList.add("display-none");
    chatBtnOff.classList.add("display-none");
    chatBtnOn.classList.remove("display-none");
  });
};


/* ******************************************************** */
/* 채팅메시지 입력시 */


const chatForm = document.querySelector("#chat-form");
const chatTextarea = document.getElementById("chat-textarea");

// 메시지 보내는 함수
const sendChat = ()=>{
  
  if(chatLoginCheck === false) return;// 비로그인 반환
  if(openChatRoom === null) return; // 채팅창 안열었을때 반환
  const msg = chatTextarea.value;
  if(msg.trim().length === 0) return; // 입력한 메세지 없으면 반환
  scrollFlag = true;

  const obj = {
    "memberNo" : chatLoginMemberNo,
    "chatContent" : msg,
    "groupNo" : openChatRoom
  };

  chatsocket.send( JSON.stringify(obj) );

  chatTextarea.value = '';

};

// 텍스트 에어리어에서 엔터 확인시(시프트엔터 x)
chatTextarea?.addEventListener("keyup", e => {
  // 입력한 키가 Enter인 경우
  if(e.key == "Enter"){
    if(!e.shiftKey){ /// shift가 눌러지지 않은 경우
      // == shift + enter 입력 시 제출 X
      sendChat();
    }
  }
});

// 폼태그가 전송될때(버튼누를때)
chatForm?.addEventListener("submit", e => {
  e.preventDefault();
  sendChat();
});



/* ******************************************************** */
/* websocket설정 */


let chatsocket;
let memsocket;

if(chatLoginCheck){
  chatsocket = new SockJS("/chatsocket");
  memsocket = new SockJS("/memsocket");
}

if(chatsocket != null){
  
  chatsocket.addEventListener("message", e => {
    
    const chat = JSON.parse(e.data);

    // 채팅메시지일때
    
    // 채팅창 닫혀있을때
    if(chatBody.classList.contains("display-none")){
      let num =  document.querySelector(".chat-onBtn>.chat-unReaded").innerText;
      if(num.length === 0) num = 0;
      document.querySelector(".chat-onBtn>.chat-unReaded").innerText
      = Number(num) + 1;
      return;
    }
    // 채팅 창은 열려있는데 방이 다른방일때
    if(chatBody.classList.contains("display-none") && chat.groupNo !== openChatRoom){
      // 그룹 버튼 중 해당그룹 선택
      const btnArr = document.querySelectorAll(".chat-groupName");
      btnArr.forEach( btn => {
        if(btn.dataset.groupNo === chat.groupNo){
          btn.children.innerText = Number(btn.children.innerText) + 1;
        }
      })
      return;
    }
    
    // 채팅창 열려있을때
    if(chatLoginMemberNo !== chat.memberNo) contentOthers(chat);
    else contentSelf(chat);

    chatUls[1].style.height = (chatBody.offsetHeight - 175) + 'px';
    scrollFlag = true;
    if(scrollFlag) chatUls[1].scrollTop = chatUls[1].scrollHeight; // 스크롤 제일 밑으로
    // 15초 후 채팅창 새로고침
    setTimeout(() => {
      if(!chatBody.classList.contains("display-none")){
        scrollFlag = false;
        chatInfo(chatLoginMemberNo);
      }
    }, 15000);
  });

} // socket end

if(memsocket != null){
  
  memsocket.addEventListener("message", e => {
    
    const memberNickList = JSON.parse(e.data);

    // 로그인중인 멤버목록을 받아오면
    chatUls[0].innerHTML = '';
    memberNickList.forEach(chat => {
      const li = document.createElement("li");
      li.innerText = chat.memberNickname;
      chatUls[0].append(li);
    });
    // 15초 후 채팅창 새로고침
    setTimeout(() => {
      if(!chatBody.classList.contains("display-none")){
        scrollFlag = false;
        chatInfo(chatLoginMemberNo);
      }
    }, 15000);
  });

} // socket end

/* ******************************************************** */
/* 메시지 */

const alertM = (message)=>{
  const massageWindow = document.querySelector(".msg-body");
  const contentBox = document.querySelector("#msg-content");
  contentBox.innerText = message;
  massageWindow.classList.remove("display-none");
  
  const msgCloseButton = document.querySelector("#msg-closeBtn");
  msgCloseButton.addEventListener("click", () => {
    massageWindow.classList.add("display-none");
  });
};

/* ******************************************************** */
/* 컨펌창 */

const confirmM = (message)=>{
  return new Promise((resolve, reject) => {
    const confirmWindow = document.querySelector(".cnf-body");
    const contentBox = document.querySelector("#cnf-content");
    const confirmYes = document.querySelector("#cnf-yBtn");
    const confirmNo = document.querySelector("#cnf-nBtn");


    // 메시지 설정
    contentBox.innerText = message;
    confirmWindow.classList.remove("display-none");

    // 확인 버튼 클릭 시 resolve 호출
    confirmYes.addEventListener("click", () => {
      confirmWindow.classList.add("display-none");
      resolve(true);  // '확인'을 눌렀을 때 true 반환
    }, { once: true });

    // 취소 버튼 또는 닫기 버튼 클릭 시 reject 호출
    confirmNo.addEventListener("click", () => {
      confirmWindow.classList.add("display-none");
      resolve(false);  // '취소'를 눌렀을 때 false 반환
    }, { once: true });

  });

};
/*
confirmM(message)
.then(result => {
  if(!result) return;
  // 컨펌 진행함수 start 

}); // 컨펌 진행함수 end
*/

/* ******************************************************** */
/* 페이지 로딩시 */


document.addEventListener("DOMContentLoaded", ()=>{

  // 채팅아이콘 표시
  if(chatLoginCheck){
    setTimeout(() => {
      // 안읽은 메시지 갯수 불러오기
      chatInfo(chatLoginMemberNo);
    }, 1000);
    chatBtnOn.classList.remove("display-none");
  }
  
  // 전달받은 메시지가 있으면 메시지창 띄우기
  if(messageCheck) {
    alertM(messageContent);
  }
  
  // 세션메세지 띄우기
  if(sessionMessage){
    alertM(sessionMessage);
    fetch('/popup/removeMessage', { method: 'POST' });
  }
});