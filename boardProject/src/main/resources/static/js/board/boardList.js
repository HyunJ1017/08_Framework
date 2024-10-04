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
    const pathName = location.pathname;
    switch(item.innerText){
      case '<<' :
        location.href = pathName + "?cp=1";
        break;

      case  '<' :
        location.href = pathName + "?cp=" + pagination.prevPage;
        break;

      case  '>' :
        location.href = pathName + "?cp=" + pagination.nextPage;
        break;

      case '>>' :
        location.href = pathName + "?cp=" + pagination.maxPage;
        break;

      default : location.href = pathName + "?cp=" + item.innerText;
    }
  });

})

//------------------------------------------------------

/* 글쓰기 버튼 클릭 시 */
const insertBtn = document.querySelector("#insertBtn");

insertBtn?.addEventListener("click", () => {

  // 현재 주소 : /board/{boardCode}
  // 요청 주소 : /editBoard/{boardCode}/insert

  const boardCode = location.pathname.split("/")[2];

  location.href = `/editBoard/${boardCode}/insert`;
});