/* 할 일 추가 관련 요소를 얻어와 변수에 저장 */
const todoTitle = document.querySelector("#todoTitle");
const todoDetail = document.querySelector("#todoDetail");
const addBtn = document.querySelector("#addBtn");

// 추가 버튼(#addBtn) 클릭 시
addBtn.addEventListener("click", () => {

  // 클릭된 순간 화면에 작성되어있는 제목, 내용 얻어오기
  const title = todoTitle.value;
  const detail = todoDetail.value;

  // Ajax(비동기) POST 방식으로 요청하기 위한 fetch() API 코드 작성
  
  // HTTP 통신 시 
  // - headers : 요청 관련 정보(어떤 데이터, 어디서 요청 ....)
  // - body : POST/PUT/DELETE 요청 시 전달할 데이터

  fetch("/todo/add", {   // 지정된 주소로 비동기 요청(ajax)
      method : "POST",  // 데이터 전달 방식을 POST로 지정
      headers: {"Content-Type": "application/json"}, // 요청 데이터의 형식을 JSON으로 지정     
      body : JSON.stringify( {"todoTitle" : title, "todoDetail" : detail} ) 
            // JS객체를 JSON 형태(문자열화)로 변환하여 Body에 추가
  })
  .then(response => response.text() ) // 요청에 대한 응답 객체(response)를 필요한 형태로 파싱
  // response.text() : 컨트롤러 반환 값을 text형태로 변환
  //                   (아래 두 번째 then 매개 변수로 전달)

  .then(result => {
    console.log("result : ", result);

    // 비동기 통신 응답 결과가 1인 경우(삽입 성공인 경우)
    if(result > 0){ 
      alert("할 일 추가 성공");

      // 추가하려고 작성한 값 화면에서 지우기
      todoTitle.value = "";
      todoDetail.value = "";

      // 비동기로 전체 할 일 개수를 조회해 화면에 반영하기
      getTotalCount();

      // 전체 할 일 목록을 조회해서 화면에 반영(출력)
      selectTodoList();

    } else {
      alert("할 일 추가 실패...");
    }

  }); // 첫 번째 then에서 파싱한 데이터를 이용한 동작 작성

});


// ---------------------------------------------------------------



/* 전체 Todo 개수 비동기(ajax) 조회 */
function getTotalCount(){

  // fetch() API 작성  (fetch : 가져오다)

  /* GET 방식 fetch() */
  fetch("/todo/totalCount") // 비동기 요청 수행 -> Promise 객체 반환
  .then(response => {
    // response : 비동기 요청에 대한 응답이 담긴 객체
    console.log(response)

    // 비동기 요청에 대한 응답에 문제가 없을 경우
    // == 비동기 요청 성공 시
    //if(response.status === 200) // 아래 if문과 같은 의미
    if(response.ok){
      // response.text() : 응답 결과 데이터를
      //    text(string) 형태로 변환(parsing)
      return response.text();
    }

    // 예외 강제 발생
    throw new Error("비동기 통신 실패");
  })

  // 첫 번째 then에서 반환된 값을 매개 변수에 저장한 후
  // 수행되는 구문
  .then(totalCount => {
    console.log("totalCount : ", totalCount);

    // #totalCount 요소의 내용으로 비동기 통신 결과를 대입
    document.querySelector("#totalCount").innerText = totalCount;
  })

  // 첫 번째 then에서 던져진 Error를 잡아서 처리하는 구문
  .catch(e => console.error(e));
}


// ----------------------------------------------------------

/* 완료된 할 일 개수 비동기로 조회 */
function getCompleteCount(){

  /* 첫 번째 then
   - 비동기 요청의 결과(응답)에 따라 어떤 코드를 수행할지 제어
   - 매개변수 response : 
        응답 결과, HTTP 상태 코드, 요청 주소 등이 담겨 있는 객체
 
 
   * 두 번째 then
   - 비동기 요청으로 얻어온 값을 이용해서 수행될 JS 코드 작성하는 구문
  */

  fetch("/todo/completeCount") // 비동기 요청해서 결과 데이터 응답 받아오겠다
  .then(response => {
    if(response.ok){ // 비동기 통신 성공 시(HTTP 상태 코드 200)
      return response.text(); // response에서 응답 결과를
                              // text 형태로 변환해서 반환
                              // -> 두 번째 then 매개 변수에 대입
    }

    // 요청 실패 시 예외(에러) 던지기 -> catch 구문에서 처리
    throw new Error("완료된 할 일 개수 조회 실패");
  })

  .then(count => {
    console.log("완료된 할 일 개수 : ", count);
    document.querySelector("#completeCount").innerText = count; 
  })

  .catch( e => {console.error(e)});
}

// ---------------------------------------------------------------

/**
 * 비동기로 할 일 목록 전체 조회
 */
function selectTodoList(){

  fetch("/todo/todoList")
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("목록 조회 실패 : " + response.status);
  })
  .then(result => {
    // JSON(List 형태) -> JS 객체 배열
    const todoList = JSON.parse(result); 
    console.log(todoList);

    /* #tbody 내용을 모든 지운 후 조회된 내용의 요소 추가 */

    const tbody = document.querySelector("#tbody");
    tbody.innerHTML = "";

    // JS 향상된 for문 : for(요소 of 배열){}
    for(let todo of todoList){

      // 1) todoNo가 들어갈 th 요소 생성
      const todoNo = document.createElement("th");
      todoNo.innerText = todo.todoNo;

      // 2) todoTitle이 들어갈 td, a 요소 생성
      const todoTitle = document.createElement("td");

      const a = document.createElement("a");
      a.innerText = todo.todoTitle;
      a.href = `/todo/detail/${todo.todoNo}`;
      
      // a 요소를 todoTitle(td)요소 자식으로 추가
      todoTitle.append(a);

      // a 요소가 클릭 되었을 때
      a.addEventListener("click", e => {
        // e : 이벤트 객체
        // e.preventDefault() : 요소 기본 이벤트 막기
        //  -> a태그의 클릭 이벤트를 막아버림
        e.preventDefault();

        // 할 일 상세조회 비동기 요청
        // e.target.href : 클릭된 a태그의 href 속성 값 얻어오기
        selectTodo(e.target.href);
      });





      // 3) todoComplete가 들어갈 th 요소 생성
      const todoComplete = document.createElement("th");
      todoComplete.innerText
        = (todo.todoComplete == 1) ? 'O' : 'X';

      // 4) regDate가 들어갈 td 요소 생성
      const regDate = document.createElement("td");
      regDate.innerText = todo.regDate;

      // 5) tr 요소를 만들어 1,2,3,4 에서 만든 요소 자식으로 추가
      const tr = document.createElement("tr");
      tr.append(todoNo, todoTitle, todoComplete, regDate);

      // 6) tbody에 tr 요소 자식으로 추가
      tbody.append(tr);
    }
  })
  .catch( e => console.error(e) );
}


/* 페이지 로딩이 완료된 후 todo 제목(a태그) 클릭 막기 */
document.addEventListener("DOMContentLoaded", ()=>{
  // DOMContentLoaded : 화면이 모두 로딩된 후

  document.querySelectorAll("#tbody a").forEach((a) => {
    // 매개변수 a : 반복마다 하나씩 요소가 꺼내져 저장되는 변수

    // a 기본 이벤트 막고 selectTodo() 호출하게 하기
    a.addEventListener("click", e => {
      e.preventDefault(); 
      selectTodo(e.target.href);
    });
  })
});


//---------------------------------------------------------
/**
 * 비동기로 할 일 상세 조회하여 팝업 레이어에 출력하기
 * @param url : /todo/detail/10 형태
 */
function selectTodo(url){

  fetch(url)
  .then(response => {
    if(response.ok){ // 요청/응답 성공 시

      // response.json()
      //  ->  reponse.text() + JSON.parse() 합친 메서드
      //   (문자열 형태 변환)  (JSON -> JS Object 변환)
      return response.json();
    }

    throw new Error("상세 조회 실패 : " + response.status);
  })
  .then(todo => {
    console.log(todo);

    /* 팝업 레이어에 조회된 todo 내용 추가하기 */
    const popupTodoNo = document.querySelector("#popupTodoNo");
    const popupTodoTitle = document.querySelector("#popupTodoTitle");
    const popupComplete = document.querySelector("#popupComplete");
    const popupRegDate = document.querySelector("#popupRegDate");
    const popupTodoContent = document.querySelector("#popupTodoContent");
    
    // 완료 여부 - 0 == 'X', 1 == 'O'
    popupComplete.innerText    = todo.todoComplete == 1 ? 'O' : 'X';

    popupTodoNo.innerText      = todo.todoNo;
    popupTodoTitle.innerText   = todo.todoTitle;
    popupRegDate.innerText     = todo.regDate;
    popupTodoContent.innerText = todo.todoDetail;


    // 팝업 레이어 보이게 하기
    // -> 클래스 중 popup-hidden 제거
    document.querySelector("#popupLayer")
              .classList.remove("popup-hidden");
              
  })
  .catch( err => console.error(err) );
}

// 팝업 레이어 X 버튼 클릭 시 닫히게 만들기
document.querySelector("#popupClose").addEventListener("click", () => {
  document.querySelector("#popupLayer").classList.add("popup-hidden");      
});


// -----------------------------------------------

/* 완료 여부 변경 버튼 클릭 시 */
const changeComplete = document.querySelector("#changeComplete");

changeComplete.addEventListener("click", () => {

  // 팝업 레이어에 작성된 todoNo 내용 얻어오기
  const todoNo 
    = document.querySelector("#popupTodoNo").innerText;

  

 
  // 비동기로 완료 여부 변경

  /* ajax(비동기) 요청 시 사용 가능한 데이터 전발 방식
    (REST API와 관련됨)

    GET    : 조회   (SELECT)
    POST   : 삽입   (INSERT)
    PUT    : 수정   (UPDATE)
    DELETE : 삭제   (DELETE)
  */
  
  fetch("/todo/todoComplete", {
    method : "PUT", // PUT 방식 요청
    headers : {"Content-Type" : "application/json"},
    // 제출되는 데이터는 json 형태라고 정의
    body : todoNo // todoNo 번호
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("완료 여부 변경 실패 : " + response.status)
  })
  .then(result => {
    console.log(result);

    // 완료 여부 span
    const todoComplete = document.querySelector("#popupComplete");

    // 팝업 레이어 완료 여부 부분 O <-> X 변경
    todoComplete.innerText 
      = (todoComplete.innerText == 'O') ? 'X' : 'O';

    // 완료된 할 일 개수를 비동기로 조회하는 함수 호출
    getCompleteCount();

    // 할 일 목록을 비동기로 조회하는 함수 호출
    selectTodoList();

  })
  .catch( err => console.error(err) );
});


// ----------------------------------------------------------

/*  할 일 삭제 */
const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", () => {

  if(!confirm("삭제할까요?")) return;

  // 삭제할 할 일 번호 얻어오기
  const todoNo 
    = document.querySelector("#popupTodoNo").innerText;

  
  // 비동기로 삭제 요청 하기(DELETE 방식 요청)
  fetch("/todo/todoDelete", {
    method : "DELETE",
    headers : {"Content-Type" : "application/json"},
    body : todoNo
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("삭제 실패 : " + response.status);
  })
  .then(result => {
    // 팝업 닫기
    document.querySelector("#popupLayer")
      .classList.add("popup-hidden");

    // 전체 목록 수, 할 일 완료 개수, 할일 목록 다시 조회
    getTotalCount();
    getCompleteCount();
    selectTodoList();
  })
  .catch( err => console.error(err) );

});


// -------------------------------------------------------

// 수정 관련되 요소 얻어오기
const popupLayer   = document.querySelector("#popupLayer");
const updateLayer  = document.querySelector("#updateLayer");

// 수정 레이어 열기
const updateView   = document.querySelector("#updateView");

// 수정 비동기 요청 버튼
const updateBtn    = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");


// 수정 레이어 열기
updateView.addEventListener("click", () => {
  popupLayer.classList.add("popup-hidden"); // 팝업 숨기기
  updateLayer.classList.remove("popup-hidden"); // 수정 레이어 보이기

  // 상세 조회 제목/내용
  const todoTitle = document.querySelector("#popupTodoTitle").innerText;
  const todoContent = document.querySelector("#popupTodoContent").innerHTML;

  // 수정 레이어 제목/내용 대입
  document.querySelector("#updateTitle").value = todoTitle;
 
  document.querySelector("#updateContent").value 
    = todoContent.replaceAll("<br>", "\n"); // 줄바꿈 문자 변경


  // 수정 버튼(#updateBtn)에 todoNo(pk) 넣기
  // - dataset 속성 : 요소에 js에서 사용할 값(data)를 추가하는 속성
  //  요소.dataset.속성명 = "값";  -> 대입
  //  요소.dataset.속성명;         -> 값 얻어오기
  updateBtn.dataset.todoNo = 
    document.querySelector("#popupTodoNo").innerText;
})


// 수정 취소 시
updateCancel.addEventListener("click", () => {
  popupLayer.classList.remove("popup-hidden"); // 팝업 보이기
  updateLayer.classList.add("popup-hidden"); // 수정 레이어 숨기기
});


// 수정 버튼(#updateBtn) 클릭 시
updateBtn.addEventListener("click", () => {

  // 서버로 제출 되어야 하는 값을 JS 객체로 형태로 묶기
  const obj = {}; // 빈 객체 생성

               // 버튼에 dataset 값 얻어오기
  obj.todoNo = updateBtn.dataset.todoNo; 
  obj.todoTitle = document.querySelector("#updateTitle").value;
  obj.todoDetail = document.querySelector("#updateContent").value;
  console.log(obj);


  // 비동기로 할 일 수정 요청
  fetch("/todo/todoUpdate", {
    method : "PUT",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
    //  obj 객체를 JSON 문자열 형태로 변환해서 제출
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("수정 실패 : " + response.status);
  })
  .then(result => {
    console.log(result); // 1/0

    if(result > 0){ // 수정 성공
      alert("수정 성공!");
      
      // 수정 레이어 숨기기
      updateLayer.classList.add("popup-hidden");

      // 상세 조회(팝업 레이어 같이 열림) 함수 호출
      selectTodo("/todo/detail/" + updateBtn.dataset.todoNo);

      // 목록 조회 다시
      selectTodoList();


    } else { // 실패
      alert("수정 실패ㅜㅜ");
    }

  })
  .catch( err => console.error(err) );


});