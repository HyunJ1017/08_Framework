// 팝업창
window.onload = function() {
  document.getElementById("popup").classList.add("active");
  document.getElementById("popupOverlay").classList.add("active");
}

// 팝업 닫기 버튼 클릭 시 팝업 닫기
document.getElementById("closePopup").onclick = function() {
  document.getElementById("popup").classList.remove("active");
  document.getElementById("popupOverlay").classList.remove("active");
}

// -------------------------------------------------------------------------

// 로그인 버튼 클릭 시
document.getElementById("login-btn").addEventListener("click", function() {
  const loginBtn = document.querySelector(".login");

  // 로그인 버튼 -> 알림과 마이페이지 버튼
  loginBtn.innerHTML = `
    <button id="alert-btn">알림</button>
    <button id="mypage-btn">마이페이지</button>
  `;

  document.getElementById("alert-btn").addEventListener("click", function() {
  });

  document.getElementById("mypage-btn").addEventListener("click", function() {
  });
});