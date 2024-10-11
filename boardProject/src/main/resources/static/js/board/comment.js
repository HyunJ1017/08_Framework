// 댓글 목록이 출력되는 ul을 감싸는 div
const commentListArea = document.querySelector(".comment-list-area");

/* 댓글 목록 전체를 비동기로 조회하는 함수
 * 
 */
const selectCommentList = () => {

  fetch("/board/commentList?boardNo=" + boardNo)
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("댓글불러오기 오류, " + response.status)
  })
  .then(html => {
    // html : Thymeleaf가 해석되어 완성된 HTML.text
    // 응답받은 comment.html에서
    // 필요한 댓글부분 : ".comment-list-area"
    // JAVA에서 thymleaf 구문을 사용하여 일부 조각만 불어올것임

    commentListArea.innerHTML = html;
    
    /* innerHTML로 새로 만들어진 요소에는 이벤트리스너가 추가되어있지 않기 때문에 답글, 수정, 삭제등이 동작하지 않는다.
    */
    addEventChildComment();  // 답글 버튼에 클릭이벤트 추가
    addEventDeleteComment(); // 삭제 버튼에 클릭이벤트 추가
    addEventUpdateComment(); // 수정 버튼에 이벤트 추가

  })
  .catch(errMassage => console.error( errMassage ));

} // function end : selectCommntList(boardNo)



// ============================================================================
// ============================================================================

// 댓글작성영역
const commentContent = document.querySelector("#commentContent");

/* 댓글 등록 하기 */

/**
 * @param {*} parentCommentNo  : 부모댓글 번호(없으면 undifined)
 */
const insertComment = (parentCommentNo) => {

  // 서버에 제출할 값을 저장할 객체
  const data = {};
  data.boardNo = boardNo;// 댓글이 작성된 게시글번호
  data.commentContent = commentContent.value;

  if(parentCommentNo !== undefined){
    data.parentCommentNo = parentCommentNo;
    data.commentContent = 
      document.querySelector(".child-comment-content").value;
  }

  fetch("/comment", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(data) // JS 객체 -> JSON (문자열)
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("댓글등록하기 오류, " + response.status)
  })
  .then(result => {
    if(result < 1){
      messageDiv.innerText = "댓글 작성 실패";
      popupLayer.classList.remove("display-hidden");
      return;
    } else {
      messageDiv.innerText = "댓글이 등록 되었습니다.";
      popupLayer.classList.remove("display-hidden");
      // 작성한 댓글을 남겨두지 않기
      commentContent.value = '';

      // 댓글목록 조회 함수 호출
      selectCommentList();
    }
  })
  .catch(errMassage => console.error( errMassage ));


}




// ============================================================================
// ============================================================================

/** 답글버튼 클릭시 해당댓글 답글작성영역을 추가하는 함수
 * @param btn : 클릭된 답글 버튼
 */
const showChildComment = (btn) => {

  /* 로그인이 되어있지 않다면 */
  if(loginCheck === false){
    messageDiv.innerText = "로그인 후 이용하세요.";
    popupLayer.classList.remove("display-hidden");
    return;
  }


  // ** 답글 작성 textarea가 한 개만 열릴 수 있도록 만들기 **
  const temp = document.getElementsByClassName("child-comment-content");

  if (temp.length > 0) { // 답글 작성 textara가 이미 화면에 존재하는 경우

    if (confirm("다른 답글을 작성 중입니다. 현재 댓글에 답글을 작성 하시겠습니까?")) {
      temp[0].nextElementSibling.remove(); // 버튼 영역부터 삭제
      temp[0].remove(); // textara 삭제 (기준점은 마지막에 삭제해야 된다!)

    } else {
      return; // 함수를 종료시켜 답글이 생성되지 않게함.
    }
  }

  // 클릭된 답글 버튼이 속해있는 댓글(li) 요소 찾기
  // .closest("태그명") : 부모중 가장 가까운 태그 찾기
  const li = btn.closest("li"); // 부모찾는함수
  
  // 탑글이 작성되는 댓글 부모댓글 번호 얻어오기
  const parentCommentNo = li.dataset.commentNo;

      // 답글을 작성할 textarea 요소 생성
      const textarea = document.createElement("textarea");
      textarea.classList.add("child-comment-content");

      li.append(textarea);

      // 답글 버튼 영역 + 등록/취소 버튼 생성 및 추가
      const commentBtnArea = document.createElement("div");
      commentBtnArea.classList.add("comment-btn-area");

      const insertBtn = document.createElement("button");
      insertBtn.innerText = "등록";

      /* 등록 버튼 클릭 시 댓글 등록 함수 호출(부모 댓글 번호 전달)  */
      insertBtn.addEventListener("click", () => insertComment(parentCommentNo));

      const cancelBtn = document.createElement("button");
      cancelBtn.innerText = "취소";
      // cancelBtn.setAttribute("onclick", "insertCancel(this)");

      /* 취소 버튼 클릭 시 답글 작성 화면 삭제 */
      cancelBtn.addEventListener("click", () => {

        // console.log(li.lastElementChild);
        li.lastElementChild.remove();
        li.lastElementChild.remove();
      });

      // 답글 버튼 영역의 자식으로 등록/취소 버튼 추가
      commentBtnArea.append(insertBtn, cancelBtn);

      // 답글 버튼 영역을 화면에 추가된 textarea 뒤쪽에 추가
      textarea.after(commentBtnArea);
}




// ============================================================================
// ============================================================================


/** 댓글 삭제 함수
 * @param {*} btn : 삭제버튼
 */
const deleteComment = (btn) => {
  if(confirm("정말 삭제 하시겠습니까?") === false) return;

  // 삭제할 댓글번호 얻어오기
  const li = btn.closest("li");
  const commentNo = li.dataset.commentNo;

  fetch("/comment", {
    method : "DELETE",
    headers : {"Content-Type" : "application/json"},
    body : commentNo
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("댓글삭제 오류 " + response.status)
  })
  .then(result => {
    if(result > 0){
      messageDiv.innerText = "댓글을 삭제 하였습니다.";
      popupLayer.classList.remove("display-hidden");
      selectCommentList(); // 댓글목록 초기화
      return;
    } else {
      messageDiv.innerText = "댓글 삭제 실패";
      popupLayer.classList.remove("display-hidden");
      return;
    }
  })
  .catch(e => console.error(e));
}




// ============================================================================
// ============================================================================
let beforeCommentRow;

const showUpdateComment = (btn) => {
  
  /* 댓글 수정 화면이 1개만 열려 있을 수 있게 하기 */
  // == 이미 열려있는 수정 화면이 있으면 닫아버리기
  const temp = document.querySelector(".update-textarea");

  if(temp != null){ // 이미 열려있는 수정 화면이 있을 경우

    if(confirm("수정 중인 댓글이 있습니다. " 
              + "현재 댓글을 수정 하시겠습니까?") === true){
                
      const commentRow = temp.parentElement; // 열려있는 댓글 행
      commentRow.after(beforeCommentRow); // 백업본을 다음 요소로 추가
      commentRow.remove(); // 열려있던 행 삭제
      
      // 백업본 버튼에 이벤트 추가
      const childeCommentBtn = beforeCommentRow.querySelector(".child-comment-btn");
      const updateCommentBtn = beforeCommentRow.querySelector(".update-comment-btn");
      const deleteCommentBtn = beforeCommentRow.querySelector(".delete-comment-btn");

      childeCommentBtn.addEventListener("click", () => showChildComment(childeCommentBtn));
      updateCommentBtn.addEventListener("click", () => showUpdateComment(updateCommentBtn));
      deleteCommentBtn.addEventListener("click", () => deleteComment(deleteCommentBtn));
    } else {
      return;
    }

  }
  
  
  // 1. 수정할 commentNo 얻어오기
  const commentRow = btn.closest("li");
  const commentNo = commentRow.dataset.commentNo;

  // 2. 취소버튼 동작에 대비하여 현재댓글의 요소를 복제해서 백업
  beforeCommentRow = commentRow.cloneNode(true);

  /*
   cloneNode(true);
   - 요소 복제하여 반환
   - true : 복제하려는 요소의 하위 요소들도 복제
  */

   // 3. 기존 내용에 작성된 내용만 얻어오기
   let beforeContent = commentRow.children[1].innerText;

   // 4. 댓글 행 내부를 모두 삭제
  commentRow.innerHTML = "";

  // 5. textarea 생성 + 클래스 추가 + 내용 추가
  const textarea = document.createElement("textarea");
  textarea.classList.add("update-textarea");
  textarea.value = beforeContent;

  // 6. 댓글 행에 textarea 추가
  commentRow.append(textarea);

  // 7. 버튼 영역 생성
  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area");

  // 8. 수정 버튼 생성
  const updateBtn = document.createElement("button");
  updateBtn.innerText = "수정";

      // 수정버튼 클릭시 댓글수정
      updateBtn.addEventListener("click", () => {

        const data = {
          "commentNo" : commentNo,
          "commentContent" : textarea.value
        }

        fetch("/comment", {
          method : "PUT",
          headers : {"Content-Type" : "application/json"},
          body : JSON.stringify(data)
        })
        .then(response => {
          if(response.ok) return response.text();
          throw new Error("댓글 수정 실패" + response.status)
        })
        .then(result => {
          if(result > 0){
            messageDiv.innerText = "댓글이 수정되었습니다.";
            popupLayer.classList.remove("display-hidden");
            selectCommentList();
            return;
          } else {
            messageDiv.innerText = "수정 실패.";
            popupLayer.classList.remove("display-hidden");
            return;
          }
        })
        .catch(e=>console.error(e));


      })

  // 9. 취소 버튼 생성
  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";

      cancelBtn.addEventListener("click", ()=>{
        if(confirm("취소하시겠습니까") === false) return;
        // 현제댓글행 다음위치에 백업한 원본댓글을 추가
        // append: 자식으로 추가
        // after : 다음행에 추가
        commentRow.after(beforeCommentRow);
        commentRow.remove(); // 수정화면으로 변환된 행 삭제

        /* 원상복구된 댓글 버튼에 이벤트 추가하기 */
        const childCommentBtn = beforeCommentRow.querySelector(".child-comment-btn");
        const updateCommentBtn = beforeCommentRow.querySelector(".update-comment-btn");
        const deleteCommentBtn = beforeCommentRow.querySelector(".delete-comment-btn");
        childCommentBtn.addEventListener("click", ()=>{showChildComment(childCommentBtn)});
        updateCommentBtn.addEventListener("click", ()=>{showUpdateComment(updateCommentBtn)});
        deleteCommentBtn.addEventListener("click", ()=>{deleteComment(deleteCommentBtn)});

      })

  // 10. 버튼 영역에 수정/취소 버튼 추가 후
  //     댓글 행에 버튼 영역 추가
  commentBtnArea.append(updateBtn, cancelBtn);
  commentRow.append(commentBtnArea);

}


// ---------------------------------------

/* REST(REpresentational State Transfer)  API

- 자원(데이터,파일)을 이름(주소)으로 
  구분(representational) 하여
  자원의 상태(State)를 주고 받는 것(Transfer)

 -> 자원의 이름(주소)를 명시하고
   HTTP Method(GET,POST,PUT,DELETE) 를 이용해
   지정된 자원에 대한 CRUD 진행

  자원의 이름(주소)는 하나만 지정 (ex. /comment)
   
  삽입 == POST    (Create)
  조회 == GET     (Read)
  수정 == PUT     (Update)
  삭제 == DELETE  (Delete)
*/



// ============================================================================
// ============================================================================

/* 이벤트 추가 구문을 모아두는 영역*/

// 댓글등록버튼 클릭동작 추가
const addComment = document.querySelector("#addComment");
addComment.addEventListener("click", () => {
  // 로그인 여부 검사
  // 댓글 작성여부 검사
  
  if(loginCheck === false) {  // loginCheck : boardDetail.html 전역변수
    messageDiv.innerText = "로그인 후 이용해 주세요.";
    popupLayer.classList.remove("display-hidden");
    return;
  }

  if(commentContent.value.trim().length === 0){
    messageDiv.innerText = "작성한 내용이 없습니다.";
    popupLayer.classList.remove("display-hidden");
    return;
  }

  insertComment();

})

// 화면에 존재하는 모든 댓글 등록버튼 클릭동작 추가
const addEventChildComment = () => {
  const btns = document.querySelectorAll(".child-comment-btn");

  btns.forEach( btn => {
    btn.addEventListener("click", () => {
      showChildComment(btn); // 답글 작성화면 출력함수 호출
    });
  })

}

// 화면에 존재하는 모든 댓글 삭제버튼 클릭동작 추가
const addEventDeleteComment = () => {
  const btns = document.querySelectorAll(".delete-comment-btn");

  btns.forEach(btn => {
    btn.addEventListener("click", ()=>{
      deleteComment(btn)
    });
  })
}

// 화면에 존재하는 수정버튼에 이벤트 추가
const addEventUpdateComment = () => {
  const btns = document.querySelectorAll(".update-comment-btn");
  
  btns.forEach(btn => {
    btn.addEventListener("click", ()=>{
      showUpdateComment(btn);
    })
  })
};


/* 화면 코드 해석 완료 후*/
document.addEventListener("DOMContentLoaded", () => {
  addEventChildComment();  // 답글 버튼에 이벤트 추가
  addEventDeleteComment(); // 삭제 버튼에 이벤트 추가
  addEventUpdateComment(); // 수정 버튼에 이벤트 추가
});