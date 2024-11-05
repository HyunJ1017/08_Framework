document.addEventListener("DOMContentLoaded", () => {
  // input 요소 읽기 전용 설정
  const memberNicknameInput = document.getElementById("memberNickname");
  if (memberNicknameInput) {
      memberNicknameInput.setAttribute("readonly", true);
  }

  // textarea 요소 읽기 전용 설정
  const memberImTextarea = document.getElementById("memberIm");
  if (memberImTextarea) {
      memberImTextarea.setAttribute("readonly", true);
  }
});
