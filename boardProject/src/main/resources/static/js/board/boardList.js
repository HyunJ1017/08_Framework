// 페이지 이동을 위한 버튼 모두 얻어오기
// pagination 클래스 후손의 a 태그들
const pageNoList = document.querySelectorAll(".pagination a");

/*
  [Array 또는 NodeList에서 제공하는 forEach()매서드]

  forEach( (item) => {} )
  - 배열 내 요소 개수 만큼 반복
  - 매 반복 마다 {} 내부 코드가 수행
  - item : 0번 인덱스 부터 차례대로 요소를 얻어와 저장하는 변수


  forEach( (item, index) => {} )
  - 배열 내 요소 개수 만큼 반복
  - 매 반복 마다 {} 내부 코드가 수행
  - item : 0번 인덱스 부터 차례대로 요소를 얻어와 저장하는 변수
  - index : 현재 반복 접근 중인 index

*/
// 페이지 이동 버튼이 클릭되었을때
pageNoList?.forEach( (item, index) => {

  // 클릭 되었을 때
  item.addEventListener("click", e => {
    e.preventDefault();

    // 만약 클릭된 a태그에 "current" 클래스가 있을 경우
    // == 현재 페이지 숫자를 클릭한 경우
    if(item.classList.contains("current")) {
      return;
    }

    // 클릭된 버튼이 <<, <, >, >> 인 경우
    // console.log(item.innerText);

    // const -> let 으로 변경

    let pathName = location.pathname;
    switch(item.innerText){
      case '<<' :
        pathName +=  "?cp=1";
        break;

      case  '<' :
        pathName +=  "?cp=" + pagination.prevPage;
        break;

      case  '>' :
        pathName +=  "?cp=" + pagination.nextPage;
        break;

      case '>>' :
        pathName +=  "?cp=" + pagination.maxPage;
        break;

      default : pathName +=  "?cp=" + item.innerText;
    } // switch end

    /* 검색화면중 이동인 경우 pathname 뒤에 쿼리스트링 추가 */
  
    // URLSearchParams : 쿼리스트링으 관리하는 객체
    // - 쿼리스트링 생성, 기존 쿼리스트링을 K:V 형태로 분할 관리
    const params = new URLSearchParams(location.search);
    const key   = params.get("key");   // K:V중 k 가 " key "인 요소의 값
    const query = params.get("query"); // K:V중 k 가 "query"인 요소의 값
  
    if(key !== null){ // 검색인 경우
  
      pathName += `&key=${key}&query=${query}`;
  
    } // if end
  
    location.href = pathName;
  }); // event end

}); // for end

//------------------------------------------------------

/* 쿼리스트링에 검색기록이 있을경우 화면에 똑같이 선택/출력하기 */

// 즉시 실행 함수
// - 변수명 중복 문제 해결 + 약간의 속도적 우위를 가지는 함수
//(()=>{})()
(()=>{
  
  // 쿼리스트링 모두 얻어와 관리하는 객체
  const params = new URLSearchParams(location.search);
  const key = params.get("key");
  const query = params.get("query");
  if(key === null) return;
  // 검색어 화면에 출력하기
  document.querySelector("#searchQuery").value = query;
  // 검색조건 선택하기
  const options = document.querySelectorAll("#searchKey > option");
  options.forEach( op => {
    if(op.value === key){
      op.selected = true;
    }
  });
})();



//------------------------------------------------------

/* 글쓰기 버튼 클릭 시 */
const insertBtn = document.querySelector("#insertBtn");

insertBtn?.addEventListener("click", () => {

  // 현재 주소 : /board/{boardCode}
  // 요청 주소 : /editBoard/{boardCode}/insert

  const boardCode = location.pathname.split("/")[2];

  location.href = `/editBoard/${boardCode}/insert`;
});