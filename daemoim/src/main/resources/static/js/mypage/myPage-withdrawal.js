function changeEmail() {
  const emailKindInput = document.getElementById("email-kind");
  const emailTypeSelect = document.getElementById("email-type");
  
  // 선택된 옵션의 값 가져오기
  const selectedValue = emailTypeSelect.value;

  // "직접입력" 옵션이 선택된 경우
  if (selectedValue === "") {
    emailKindInput.value = ''; // 입력 필드 초기화
    emailKindInput.disabled = false; // 입력 필드 활성화
  } else {
    emailKindInput.value = selectedValue; // 선택된 값 입력 필드에 설정
    emailKindInput.disabled = true; // 입력 필드 비활성화
  }
}
