/* 할 일 추가 관련 요소를 얻어와 변수에 저장 */
const addBtn     = document.querySelector("#addBtn");
const todoTitle  = document.querySelector("#todoTitle");
const todoDetail = document.querySelector("#todoDetail");

// 추가버튼 (#addBtn) 클릭시
addBtn.addEventListener("click", () => {

  // 클릭된 순간 화면에 작성되어있는 제목, 내용 얻어오기
  const title  = todoTitle.value;
  const detail = todoDetail.value;

  // Ajax (insert는) POST 방식으로 요청하기 위한 fetch() API 코드 작성

  // HTTP 통신 시
  // - haeders : 요청관련정보(어떤데이터, 어디서 요청 ...)
  // - body : POST/PUT/DELETE 요청 시 전달할 데이터
  
  fetch("/todo/add", { // 지정된 주소로 비동기 요청(ajax)
    method : "POST", // 데이터 전달 방식을 POST로 지정
    headers: {"Content-Type": "application/json"}, // 요청 데이터의 형식을 JSON으로 지정
    body : JSON.stringify( {"todoTitle":title, "todoDetail":detail} ) // JS객체를 JSON 형태로 변환하여 Body에 추가
    })
    .then(response => response.text() ) // 요청에 대한 응답 객체(response)를 필요한 형태로 파싱
    // controller 반환값을 text 형태로 변환해서 다음 then 매개변수로 반환함

    .then(result => {
      console.log("result : ", result)

      // 비동기통신 응답결과가 1(삽입성공)인 경우
      if(result > 0){
        alert("할 일 추가 성공");
        
        // 추가하려고 작성한 값 화면에서 지우기
        todoTitle.value = '';
        todoDetail.value = '';
        
      } else {
        alert("할 일 추가 실패....");
        
      }

    }); // 첫 번째 then에서 파싱한 데이터를 이용한 동작 작성

});

/******************************************************************************************** */

/* Ajax 기초 연습 - todoNo 일치하는 할 일의 제목 얻어오기 */
const inputTodoNo = document.querySelector("#inputTodoNo");
const searchBtn   = document.querySelector("#searchBtn");

// #searchBtn 클릭시
searchBtn.addEventListener("click", ()=>{

  //#inputTodoNo에 작성된 값 얻어오기
  const todoNo = inputTodoNo.value;

  // 비동기로 todoNo을 서버에 전달하고 해당하는 할일 제목 가져오기(fetch) - GET방식
  // - GET방식 사용 : 주소에 제출할 값이 쿼리스트링 형태로 담긴다
  fetch("/todo/searchTitle?todoNo=" + todoNo)
  .then(response => response.text())
  .then(todoTitle => {
    // 매개변수 todoTitle
    // == 서버에서 반환 받은 "할 일 제목"이 담긴 변수
    alert(todoTitle);
  })

});