document.addEventListener("DOMContentLoaded", () => {
  // 팝업창
  const popup = document.getElementById("popup");
  const popupOverlay = document.getElementById("popupOverlay");
  const closePopupBtn = document.getElementById("closePopup");

  if (popup && popupOverlay) {
    popup.classList.add("active");
    popupOverlay.classList.add("active");

    // 팝업 닫기 버튼 클릭 시 팝업 닫기
    closePopupBtn.onclick = () => {
      popup.classList.remove("active");
      popupOverlay.classList.remove("active");
    };
  }

  // 로그인 버튼 클릭 시
  const loginBtn = document.getElementById("login-btn");
  if (loginBtn) {
    loginBtn.addEventListener("click", () => {
      const loginWrapper = document.querySelector(".login-wrapper");

      // 알림 버튼 클릭 시
      const alertBtn = document.getElementById("alert-btn");
      if (alertBtn) {
        alertBtn.addEventListener("click", () => {
          // 알림 기능 로직 추가
        });
      }

      // 마이페이지 버튼 클릭 시
      const mypageBtn = document.getElementById("mypage-btn");
      if (mypageBtn) {
        mypageBtn.addEventListener("click", () => {
          // 마이페이지 이동 로직 추가
        });
      }
    });
  }



 // 카테고리 박스 클릭 시 페이지 이동
 document.querySelectorAll(".category-box").forEach(box => {
   box.addEventListener("click", (event) => {
     if (event.target.tagName === "A") {
       return;
     }
     const link = box.querySelector("a.categoryGroup-name");
     if (link) {
       window.location.href = link.href;
     }
   });
 });
});

