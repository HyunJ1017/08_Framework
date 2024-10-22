/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardWriteFrm");
form.addEventListener("submit", e => {

  // 제목, 내용 input 얻어오기
  const boardTitle = document.querySelector("[name=boardTitle]");
  const boardContent = document.querySelector("[name=boardContent]");

  // 제목을 입력 안 했을 때
  if(boardTitle.value.trim().length === 0){
    alert("제목을 작성해주세요");
    boardTitle.focus();
    e.preventDefault();
    return;
  }

  // 내용을 입력 안 했을 때
  if(boardContent.value.trim().length === 0){
    alert("내용을 작성해주세요");
    boardContent.focus();
    e.preventDefault();
    return;
  }

})
