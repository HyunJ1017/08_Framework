document.addEventListener("DOMContentLoaded", function() {
  // 팝업창
  const popup = document.getElementById("popup");
  const popupOverlay = document.getElementById("popupOverlay");
  const closePopupBtn = document.getElementById("closePopup");

  if (popup && popupOverlay) {
    popup.classList.add("active");
    popupOverlay.classList.add("active");
  
    // 팝업 닫기 버튼 클릭 시 팝업 닫기
    closePopupBtn.onclick = function() {
      popup.classList.remove("active");
      popupOverlay.classList.remove("active");
    }
  }

  // 로그인 버튼 클릭 시
  const loginBtn = document.getElementById("login-btn");
  if (loginBtn) {
    loginBtn.addEventListener("click", function() {
      const loginWrapper = document.querySelector(".login-wrapper");

      // 로그인 버튼 -> 알림과 마이페이지 버튼으로 변경
      loginWrapper.innerHTML = `
        <button id="alert-btn">알림</button>
        <button id="mypage-btn">마이페이지</button>
      `;

      // 알림 버튼 클릭 시
      const alertBtn = document.getElementById("alert-btn");
      alertBtn.addEventListener("click", function() {
      });

      // 마이페이지 버튼 클릭 시
      const mypageBtn = document.getElementById("mypage-btn");
      mypageBtn.addEventListener("click", function() {
      });
    });
  }
});
