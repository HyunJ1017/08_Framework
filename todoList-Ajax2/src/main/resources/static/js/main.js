const memberNickname = document.querySelector("[name = memberNickname]");

memberNickname.addEventListener("submit", ()=>{
  
  inputNickname = memberNickname.value.trim();

  const nicknamePattern = /^[가-힣0-9\s]{3,11}$/;

  if (nicknamePattern.test(nicknameInput) === false) {
    alert("띄어쓰기를 포함한 3~11자의 한글 또는 숫자만 입력해 주세요")
  }
});