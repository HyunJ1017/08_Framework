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

/******************************************************************************************** */

/* todoNo(단일값) 일치하는 할 일의 모든 정보(다수의값) 얻어오기 */
const inputTodoNo2 = document.querySelector("#inputTodoNo2");
const searchBtn2   = document.querySelector("#searchBtn2");

// #searchBtn2 클릭시
searchBtn2.addEventListener("click", ()=>{
  
  const todoNo = inputTodoNo2.value;
  
  fetch("/todo/selectTodo?todoNo=" + todoNo)
  .then(response => {
    
    if(response.ok){
      return response.text();
    }

    throw new Error("비동기통신에러");
  })
  .then(result => {

    console.log(result);
    console.log(typeof result); // => string

    /* JSON.parse(JASON 문자열)
     - JASON -> JS Object로 변경
    */
    const todo = JSON.parse(result);
    console.log(todo);
    console.log(typeof todo); // => object
    
    /* #result2에 결과값 출력하기 */
    const ul = document.querySelector("#result2");
    ul.innerHTML = '';

    // li를 생성해서 ul(#result2) 의 자식으로 추가하는 함수
    const createLi = (key) => {
      const li = document.createElement("li");
      li.innerText = `${key} : ${todo[key]}`;
      ul.append(li);
    }

    createLi("listNo");
    createLi("todoTitle");
    createLi("todoDetail");
    createLi("complete");
    createLi("regDate");
    createLi("color");

  })
  .catch(e => {console.log(e)});
});

