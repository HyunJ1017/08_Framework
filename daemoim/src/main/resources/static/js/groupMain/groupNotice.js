document.addEventListener("DOMContentLoaded", function () {
  // 공지사항 데이터를 가져와서 화면에 표시하는 함수
  function loadBoardList() {
    console.log("호출 : /groupMain/boardList?groupNo=" + groupNo);
    fetch("/groupMain/boardList?groupNo=" + groupNo)  // API 호출
      .then(response => {
        if (response.ok) return response.json();
        throw new Error(response.status);
      })  // 응답을 JSON으로 파싱
      .then(data => {
        console.log(data);
        renderBoardList(data.slice(0, 5))
      })  // 파싱한 데이터를 렌더링
      .catch(error => console.error(error));
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
      const a1 = document.createElement("a");
      a1.href = "/board/" + groupNo + "/1/" + board.boardNo;
      a1.innerText = board.boardTitle;
      a1.style.color = "black";
      a1.style.textDecoration = "none";
      cellTitle.appendChild(a1);
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

/* 모입가입 신청 */

const sidebarLinks = document.querySelectorAll(".sidebar-link");
const joinGroupModal = document.getElementById("joinGroupModal");
const closeModal = document.getElementById("closeModal");
const confirmJoin = document.getElementById("confirmJoin");
const cancelJoin = document.getElementById("cancelJoin");
const modalMessage = document.getElementById("modalMessage");

// 사이드바 링크 클릭 이벤트
sidebarLinks.forEach(link => {
  link.addEventListener("click", (event) => {
    event.preventDefault();

    if (!isLoginMember) {
      // 로그인하지 않은 경우 로그인 페이지로 이동
      location.href = "/signin";
    } else if (memberGroupCheck && link.id === "joinGroupBtn") {
      // 로그인했지만 이미 가입된 경우 알림 표시
      alert("이미 가입된 회원입니다.");
    } else if (!memberGroupCheck && link.id === "joinGroupBtn") {
      // 로그인했고 아직 가입하지 않은 경우 가입 신청 모달 표시
      modalMessage.textContent = `가입하시겠습니까?`;
      confirmJoin.style.display = "inline-block"; // 신청 버튼 보이기
      joinGroupModal.style.display = "block";
    }
  });
});

// 모달 창 닫기
closeModal.onclick = function () {
  joinGroupModal.style.display = "none";
};
cancelJoin.onclick = function () {
  joinGroupModal.style.display = "none";
};

// 가입 신청 확인 버튼 클릭 시
confirmJoin.addEventListener("click", () => {
  fetch(`/groupMain/joinGroup/${groupNo}`, {
    method: "POST",
  })
  .then(response => {
    if (response.ok) {
      alert("가입 신청이 완료되었습니다.");
      joinGroupModal.style.display = "none";
      location.reload();
      return;

    } 
      alert("가입 신청에 실패하였습니다. 다시 시도해주세요.");
      return;
    

  });
  
});






