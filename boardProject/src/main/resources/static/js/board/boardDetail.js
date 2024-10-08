/* 전역변수 */
// 현제 상세조회중인 게시글번호
const boardNo = location.pathname.split('/')[3];


/* 좋아요 ❤ 클릭시 */
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click", e => {

  // 1. 로그인 여부 검사
  if(loginCheck == false){
    messageDiv.innerText = "로그인 후 이용해 주세요";
    popupLayer.classList.remove("display-hidden");
    return;
  }


  // 2. 비동기로 좋아요 요청
  fetch("/board/like", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : boardNo
  })
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("좋아요 요청 오류 " + response.status)
  })
  .then(result => {
    // console.log(result);

    if(result.check === 'insert'){
      document.querySelector("#boardLike").classList.remove("fa-regular");
      document.querySelector("#boardLike").classList.add("fa-solid");
    } else {
      document.querySelector("#boardLike").classList.remove("fa-solid");
      document.querySelector("#boardLike").classList.add("fa-regular");
    }
    
    document.querySelector('.like-area span').innerText = result.count;

  })
  .catch(err => console.error(err));

});