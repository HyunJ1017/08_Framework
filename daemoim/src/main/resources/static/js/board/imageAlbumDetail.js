// 전역 변수
const boardNo = location.pathname.split("/")[4];
const modal = document.getElementById("imageModal");
const modalImg = document.getElementById("modalImage");
const modalCaption = document.getElementById("modalCaption");
const closeBtn = document.getElementsByClassName("modal-close")[0];

// 이미지 클릭 시 모달 표시
document.querySelectorAll('.image-item img').forEach(img => {
  img.addEventListener('click', function () {
    modal.style.display = "block";
    modalImg.src = this.src;
    modalCaption.textContent = this.alt;
  });
});

// 모달 닫기
closeBtn.addEventListener('click', () => {
  modal.style.display = "none";
});

// 모달 외부 클릭 시 닫기
window.addEventListener('click', (e) => {
  if (e.target === modal) {
    modal.style.display = "none";
  }
});

// ESC 키로 모달 닫기
document.addEventListener('keydown', (e) => {
  if (e.key === "Escape" && modal.style.display === "block") {
    modal.style.display = "none";
  }
});

/* 좋아요 하트 클릭 시 */
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click", e => {

  // 1. 로그인 여부 검사
  if (loginCheck === false) {
    alert("로그인 후 이용해 주세요");
    return;
  }


  // 2. 비동기로 좋아요 요청
  fetch("/board/like", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: boardNo
  })
    .then(response => {
      if (response.ok) return response.json();
      throw new Error("좋아요 실패");
    })
    .then(result => {

      // 좋아요 결과가 담긴 result 객체의 check 값에 따라
      // 하트 아이콘을 비우기/채우기 지정
      if (result.check === 'insert') { // 채우기
        boardLike.classList.add("fa-solid");
        boardLike.classList.remove("fa-regular");

        // 게시글 작성자에게 알림 보내기
        const content
          = `<strong>${memberNickname}</strong>님이 <strong>${boardDetail.boardTitle}</strong> 게시글을 좋아합니다`;

        // type, url, pkNo, content
        sendNotification(
          "boardLike",
          location.pathname,  // 게시글 상세 조회 페이지 주소
          boardDetail.boardNo,
          content
        );

      } else { // 비우기
        boardLike.classList.add("fa-regular");
        boardLike.classList.remove("fa-solid");
      }

      // 좋아요 하트 다음 형제 요소의 내용을 
      // result.count로 변경
      boardLike.nextElementSibling.innerText = result.count;

    })
    .catch(err => console.error(err));


})

// 삭제 기능
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn?.addEventListener("click", () => {
  if (!confirm("정말 삭제 하시겠습니까?")) return;

  const form = document.createElement("form");
  form.action = "/editBoard/delete";
  form.method = "POST";

  const input = document.createElement("input");
  input.type = "hidden";
  input.name = "boardNo";
  input.value = boardNo;

  form.append(input);
  document.body.append(form);
  form.submit();
});

// 수정 페이지로 이동
const updateBtn = document.querySelector("#updateBtn");
updateBtn?.addEventListener("click", () => {
  const form = document.createElement("form");
  form.action = location.pathname.replace("board", "editBoard") + "/updateView";
  form.method = "POST";

  document.body.append(form);
  form.submit();
});

// 목록으로 이동
const goToListBtn = document.querySelector("#goToListBtn");
goToListBtn.addEventListener("click", () => {
  const limit = 10;
  let url = location.pathname + "/goToList?limit=" + limit;

  const params = new URLSearchParams(location.search);
  if (params.get("key") !== null) {
    url += `&key=${params.get("key")}&query=${params.get("query")}`;
  }

  location.href = url;
});