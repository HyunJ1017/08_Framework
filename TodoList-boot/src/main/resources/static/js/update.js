const goToList    = document.querySelector("#goToList");

// 목록으로 버튼이 클릭된 경우
goToList.addEventListener("click", () => {
  // "/" (메인페이지) 요청 (GET방식)
  location.href = "/";
});