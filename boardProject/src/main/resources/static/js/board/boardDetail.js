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

      /* 알림설정 */
      const content = `<strong>${memberNickname}</strong>님이 <strong>${boardDetail.boardTitle}</strong> 게시글에 좋아요를 눌렀습니다.`;

      sendNotification("boardLike", location.pathname, boardNo, content);

    } else {
      document.querySelector("#boardLike").classList.remove("fa-solid");
      document.querySelector("#boardLike").classList.add("fa-regular");
    }
    
    document.querySelector('.like-area span').innerText = result.count;

  })
  .catch(err => console.error(err));

});


// ============================================================================
// ============================================================================


/*  삭제버튼 클릭시

- 삭제버튼 클릭 시 "정말 삭제 하시겠습니까?"
- /editBoard/delete, POST방식, 동기식요청
-> form태그 만들어서 보내야 해요
-> + 게시글 번호가 세팅된 인풋태그 포함
-> body태그 제일 아래에 추가해서 submit();

- 서버에서 로그인한 회원의 회원 번호를 얻어와
로그인한 회원이 작성한 글이 맞는지 SQL에서 검사
*/

const deleteBtn = document.querySelector("#deleteBtn");
// 로그인 한 회원만 삭제버튼이 보여서, 안전탐색연산자(?) 필요
deleteBtn?.addEventListener("click", ()=>{
  
  if(confirm("삭제하겠습니까?") == false) return;
  
  const url = '/editBoard/delete';
  
  // 게시글 번호는 전역변수로 설정해놓은 boardNo 사용
  
  // form 생성
  const form = document.createElement("form");
  form.action=url;
  form.method="POST";
  
  // input 생성
  const input = document.createElement("input");
  input.type="hidden";
  input.name="boardNo";
  input.value=boardNo;
  
  // form 자식에 input 추가
  form.append(input);
  
  // body 자식에 form 추가
  document.querySelector("body").append(form);
  
  // 제출
  form.submit();
  
 })

// ============================================================================
// ============================================================================


/*  수정버튼 클릭시

- /editBoard/{boardCode}/{boardNo}, POST, 동기식 요청

- 로그인한 회원이 맞을 경우
  해당글을 상세조회한 후
  수정화면으로 전환

*/

const updateBtn = document.querySelector("#updateBtn");

updateBtn?.addEventListener("click", () => {

  const url = location.pathname.replace("board", "editBoard");

  const form = document.createElement("form");
  form.action=url + "/updateView";
  form.method="POST";
  document.querySelector("body").append(form);
  form.submit();

})


// ============================================================================
// ============================================================================

/* 돌아가기버튼클릭시

- /board/{boardCode}

- {boardCode}가 일치하는 게시글 + 삭제되지 않은 게시글 중
  현재 게시글이 몇번째 게시글인지 구해서 돌아가야 되는 cp값 구하기

*/
const goToListBtn = document.querySelector("#goToListBtn");
goToListBtn.addEventListener("click", ()=>{

  // 한 페이지에 보여주는 최대 게시글 갯수
  const limit = 10;

  const params = new URLSearchParams(location.search);

  let url 
    = location.pathname + "/gotoList?limit=" + limit;


  if( params.get("key") !== null ){
    // 검색인 경우
    url += `&key=${params.get("key")}&query=${params.get("query")}`;
  }
  location.href = url;
  // /board/{boardCode}/{boardNo}/gotoList?limit=10
  // /board/{boardCode}/{boardNo}/gotoList?limit=10&key=t&query=123
  // location.search : 쿼리스트링 반환
  // URLSearchParams : 쿼리스트링 관리해주는 객체
  // new URLSearchParams(location.search).get("key")
});