/* ajax 기초 연습 - todoNo 일치하는 할 일의 제목 얻어오기 */
const inputTodoNo = document.querySelector("#inputTodoNo");
const searchBtn = document.querySelector("#searchBtn");

// #searchBtn 클릭 시
searchBtn.addEventListener("click", () => {

  // #inputTodoNo에 작성된 값 얻어오기
  const todoNo = inputTodoNo.value;

  // 비동기로 todoNo를 서버에 전달하고
  // 해당하는 할 일 제목(값) 가져오기(fetch) 
  // - GET 방식(주소에 제출할 값이 쿼리스트링 형태로 담긴다!!)

  fetch("/todo/searchTitle?todoNo=" + todoNo)
  .then(response => response.text())
  .then(todoTitle => {
    // 매개 변수 todoTitle 
    // == 서버에서 반환 받은 "할 일 제목"이 담긴 변수
    alert(todoTitle);
  })
});

// ---------------------------------------------------


/* todoNo(단일) 일치하는 할 일의 모든 정보(다수) 얻어오기 */
const inputTodoNo2 = document.querySelector("#inputTodoNo2");
const searchBtn2 = document.querySelector("#searchBtn2");

searchBtn2.addEventListener("click", () => {

  // (비동기) GET방식 요청은 주소에 전달할 값(파라미터)를
  // 쿼리스트링 형태로 작성!!!

  const todoNo = inputTodoNo2.value; // 입력된 할 일 번호 얻어오기

  fetch("/todo/todoDetail?todoNo=" + todoNo)
  .then(response => {
    if(response.ok){
      return response.text(); // 응답 결과를 text(string)으로 변경
    } 
    throw new Error("비동기 요청 실패");
  })
  .then(result => {
    console.log(result);
    console.log(typeof result); // string

    /* JSON.parse(JSON문자열) 
      - JSON -> JS Object로 변경
    */
    const todo = JSON.parse(result);
    console.log(todo);

  
    // #result2 요소 얻어오기
    const ul = document.querySelector("#result2");

    // #result2 이전 내용 모두 삭제
    ul.innerHTML = "";

    // li태그를 생성해서 ul(#result2) 자식으로 추가하는 함수
    const createLi = (key) => {
      const li = document.createElement("li");
      li.innerText = `${key} : ${todo[key]}`;
      ul.append(li);
    }

    createLi("todoNo");
    createLi("todoTitle");
    createLi("regDate");
    createLi("todoDetail");
    createLi("todoComplete");

  })
  .catch(e => console.error(e));

});