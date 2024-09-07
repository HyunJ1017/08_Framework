const goToList    = document.querySelector("#goToList");
const completeBtn = document.querySelector("#completeBtn");
const updateBtn   = document.querySelector("#updateBtn");
const deleteBtn   = document.querySelector("#deleteBtn");
const listNo      = ( document.querySelector("#listNo") ).value;


// 목록으로 버튼이 클릭된 경우
goToList.addEventListener("click", () => {
  location.href = "/";
});




// 삭제 버튼 클릭시
deleteBtn.addEventListener("click", () => {

  // 1. 정말 삭제할 것인지 confirm()을 이용해서 확인
  if( !confirm("정말 삭제 하시겠습니까?") ){
    // 취소 클릭시
    return;
  }

  // 2. confirm() 확인 클릭시
  //   /todo/delete/인덱스번호 GET 방식으로 요청 보내기
  const title = document.querySelector("#title");
  location.href = "/todo/delete/" + listNo;

});


// 수정버튼 클릭시 수정할 수 있는 화면을 요청
updateBtn.addEventListener("click", () => {
  // GET 방식 요청
  location.href = "/todo/update/" + listNo;
});