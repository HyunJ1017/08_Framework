// 페이지 이동을 위한 버튼 모두 얻어오기
const pageNoList = document.querySelectorAll(".pagination a");

// 페이지 이동 버튼이 클릭 되었을 때
pageNoList?.forEach((item, index) => {
  item.addEventListener("click", e => {
    e.preventDefault();
    
    if(item.classList.contains("current")) return;

    let pathname = location.pathname;

    switch(item.innerText){
      case '<<': pathname += "?cp=1";                       break;
      case '<' : pathname += "?cp=" + pagination.prevPage;  break;
      case '>' : pathname += "?cp=" + pagination.nextPage;  break;
      case '>>': pathname += "?cp=" + pagination.maxPage;   break;
      default  : pathname += "?cp=" + item.innerText;
    }

    const params = new URLSearchParams(location.search);
    const key = params.get("key");
    const query = params.get("query");

    if(key !== null){
      pathname += `&key=${key}&query=${query}`;
    }

    location.href = pathname;
  });
});

// 검색 기록 유지
(() => {
  const params = new URLSearchParams(location.search);
  const key = params.get("key");
  const query = params.get("query");

  if(key === null) return;

  document.querySelector("#searchQuery").value = query;

  const options = document.querySelectorAll("#searchKey > option");
  options.forEach(op => {
    if(op.value === key){
      op.selected = true;
      return;
    }
  });
})();

// 글쓰기 버튼 클릭 시
const insertBtn = document.querySelector("#insertBtn");

insertBtn?.addEventListener("click", () => {
  const groupNo = location.pathname.split("/")[2];
  const boardTypeCode = location.pathname.split("/")[3];
  location.href = `/editBoard/${groupNo}/${boardTypeCode}/insert`;
});

// 스크롤 시 헤더 고정
window.addEventListener('scroll', () => {
  const header = document.querySelector('header');
  const footer = document.querySelector('footer');
  
  if(header && footer) {
    header.style.position = 'fixed';
    header.style.top = '0';
    header.style.width = '100%';
    header.style.zIndex = '1000';
    
    footer.style.position = 'fixed';
    footer.style.bottom = '0';
    footer.style.width = '100%';
  }
});