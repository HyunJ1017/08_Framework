// 현재 상세 조회한 게시글 번호
const boardNo = location.pathname.split("/")[4];

/* 좋아요 하트 클릭 시 */
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click", e => {

  // 1. 로그인 여부 검사
  if(loginCheck === false){
    alert("로그인 후 이용해 주세요");
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
    throw new Error("좋아요 실패");
  })
  .then(result => {
    //console.log("result : ", result);

    // 좋아요 결과가 담긴 result 객체의 check 값에 따라
    // 하트 아이콘을 비우기/채우기 지정
    if(result.check === 'insert'){ // 채우기
      boardLike.classList.add("fa-solid");
      boardLike.classList.remove("fa-regular");

    } else { // 비우기
      boardLike.classList.add("fa-regular");
      boardLike.classList.remove("fa-solid");
    }

    // 좋아요 하트 다음 형제 요소의 내용을 
    // result.count로 변경
    boardLike.nextElementSibling.innerText = result.count;

  })
  .catch(err => console.error(err));


})

// -------------------------------------------------

/* * 삭제 버튼 클릭 시 *
  - 삭제 버튼 클릭 시 "정말 삭제 하시겠습니까?" confirm() 

  - /editBoard/delete, POST 방식, 동기식 요청
   -> form 태그 생성 + 게시글 번호가 세팅된 input
   -> body 태그 제일 아래 추가해서 submit()

  - 서버(Java)에서 로그인한 회원의 회원 번호를 얻어와
    로그인한 회원이 작성한 글이 맞는지 SQL에서 검사
*/

// 삭제 버튼 요소 얻어오기
const deleteBtn = document.querySelector("#deleteBtn");

// 삭제 버튼이 존재할 때 이벤트 리스너 추가
deleteBtn?.addEventListener("click", () => {

  if(confirm("정말 삭제 하시겠습니까?") == false){
    return;
  }

  const url = "/editBoard/delete"; // 요청 주소
  // 게시글 번호 == 전역 변수 boardNo

  // form 태그 생성
  const form = document.createElement("form");
  form.action = url;
  form.method = "POST";

  // input 태그 생성
  const input = document.createElement("input");
  input.type = "hidden";
  input.name = "boardNo";

  input.value = boardNo;

  form.append(input); // form 자식으로 input 추가

  // body 자식으로 form 추가
  document.querySelector("body").append(form); 

  form.submit(); // 제출
  
})

// ---------------------------------------------------

/* * 수정 버튼 클릭 시 *
  - /editBoard/{boardCode}/{boardNo}/updateView, POST, 동기식
   -> form 태그 생성 
   -> body 태그 제일 아래 추가해서 submit()

  - 서버(Java)에서 로그인한 회원의 회원 번호를 얻어와
    로그인한 회원이 작성한 글이 맞을 경우
    해당 글을 상세 조회 한 후
    수정 화면으로 전환 
*/

const updateBtn = document.querySelector("#updateBtn");

updateBtn?.addEventListener("click", () => {
  const form = document.createElement("form");

  //  /editBoard/{groupNo}/{boardTypeCode}/{boardNo}/updateView
  form.action = location.pathname.replace("board","editBoard")
                + "/updateView";

  form.method = "POST";
  
  document.querySelector("body").append(form);
  form.submit();
})

// ----------------------------------------------------

/* 목록으로 버튼 클릭 시 */
const goToListBtn = document.querySelector("#goToListBtn");

goToListBtn.addEventListener("click", () => {

  // 페이지당 게시글 수
  const limit = 10;

  let url 
    = location.pathname + "/goToList?limit=" + limit;

  // /board/{boardCode}/{boardNo}/goToList?limit=10

  // location.search      : 쿼리스트링 반환
  // URLSearchParams 객체 : 쿼리스트링 관리하는 객체
  const params = new URLSearchParams(location.search);

  // 쿼리스트링에 key가 존재하는 경우 == 검색인 경우
  if(params.get("key") !== null){
    url += `&key=${params.get("key")}&query=${params.get("query")}`;
  }

  location.href = url;
  // /board/{boardCode}/{boardNo}/goToList
  // (검색 X) ?limit=10
  // (검색 O) ?limit=10&key=t&query=검색어

})