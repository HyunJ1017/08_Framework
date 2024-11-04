document.addEventListener("DOMContentLoaded", function() {
  // 공지사항 데이터를 가져와서 화면에 표시하는 함수
  function loadBoardList() {
      fetch('/board/boardList')  // API 호출
          .then(response => {
            if(response.ok) return response.json();
          })  // 응답을 JSON으로 파싱
          .then(data => renderBoardList(data.slice(0,5)))  // 파싱한 데이터를 렌더링
          .catch(error => console.error("출력오류"));
  }


  // 공지사항 목록을 HTML로 렌더링하는 함수
  function renderBoardList(boardList) {
      const tableBody = document.querySelector(".notice tbody");
      tableBody.innerHTML = '';  // 기존 내용을 초기화

      // 각 공지사항 데이터를 테이블에 추가
      boardList.forEach(board => {
          const row = document.createElement("tr");

          const cellNotice = document.createElement("td");
          cellNotice.textContent = '공지'; // 고정된 텍스트
          row.appendChild(cellNotice);

          const cellTitle = document.createElement("td");
          cellTitle.textContent = board.boardTitle;
          row.appendChild(cellTitle);

          const cellDate = document.createElement("td");
          cellDate.textContent = board.boardWriteDate;
          row.appendChild(cellDate);

          tableBody.appendChild(row);
      });
  }

  // 페이지가 로드되면 공지사항을 불러옴
  loadBoardList();
});
