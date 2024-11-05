const pageNoList = document.querySelectorAll(".pagination a");

const urlParts = window.location.href.split("/");

let boardTypeCode = 1;

$(function () {
  // Datepicker를 #calendar에 적용
  $("#calendar").datepicker({
    dateFormat: 'yy-mm-dd', // 날짜 형식
    onSelect: function (dateText, inst) {
      // 날짜 셀 클릭 시에만 이동
      if ($(inst.dpDiv.find('.ui-state-active')).length) {
        const baseUrl = "/board/boardCalendar/" + urlParts[urlParts.length - 1];
        window.location.href = `${baseUrl}`;
      }
    }
  });
});


// 게시글 목록 조회
const selectList = (groupNo, boardTypeCode, cp) => {

  const listSpace = document.querySelector(".list-wrapper");

  const url = `/board/${groupNo}/${boardTypeCode}?cp=${cp}`;

  fetch(url)
    .then(resp => resp.text())
    .then(html => {
      listSpace.innerHTML = html


      listSpace.querySelectorAll(".pagination a").forEach(a => {

        a.addEventListener("click", e => {
          e.preventDefault();

          if (a.dataset.page != null) {
            selectList(groupNo, boardTypeCode, a.dataset.page);
          }

        })
      });

    })
    .catch(error => {
      console.error("Error fetching data:", error);
  })


}



const bannerList = document.querySelectorAll(".banner");

bannerList.forEach(item => {

  item.addEventListener("click", e => {
    const url = item.getAttribute("href");


    if (url.slice(url.lastIndexOf("/") + 1) == 3) return;

    boardTypeCode = url.slice(url.lastIndexOf("/") + 1);

    document.querySelector(".btn-write").classList.remove("hidden");

    document.querySelector(".btn-write").href = `${location.pathname.replace("board", "editBoard")}/${boardTypeCode}/insert`;

    e.preventDefault();

    const groupNo = location.pathname.split("/")[2];
    selectList(groupNo, boardTypeCode, 1);

    // fetch(url)
    //   .then(resp => resp.text())
    //   .then(html => {
    //     listSpace.innerHTML = html
    //   })
    //   .catch(error => {
    //     console.error("Error fetching data:", error);
    // })

  })
})



pageNoList?.forEach((item, index) => {
  
  // 클릭 되었을 때
  item.addEventListener("click", e => {
    e.preventDefault();
    
    // 만약 클릭된 a태그에 "current" 클래스가 있을 경우
    // == 현재 페이지 숫자를 클릭한 경우
    if (item.classList.contains("current")) return;
    
    // const -> let으로 변경
    let pathname = location.pathname;
    switch (item.innerText) {
      case '<<': pathname += "?cp=1"; break;
      case '<': pathname += "?cp=" + pagination.prevPage; break;
      case '>': pathname += "?cp=" + pagination.nextPage; break;
      case '>>': pathname += "?cp=" + pagination.maxPage; break;
      default: pathname += "?cp=" + item.innerText;
    }
    
    const params = new URLSearchParams(location.search);
    const key = params.get("key");
    const query = params.get("query");
    
    if (key !== null) {
      pathname += `&key=${key}&query=${query}`;
    }
    
    location.href = pathname;
  })
});

// 검색 기록 유지
(() => {
  const params = new URLSearchParams(location.search);
  const key = params.get("key");
  const query = params.get("query");
  
  if (key === null) return;
  
  document.querySelector("#searchQuery").value = query;
  
  const options = document.querySelectorAll("#searchKey > option");
  options.forEach(op => {
    if (op.value === key) {
      op.selected = true;
      return;
    }
  });
})();

document.addEventListener("DOMContentLoaded", e => {
  selectList(groupNo, 1, 1);
})