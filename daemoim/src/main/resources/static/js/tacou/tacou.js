document.getElementById('goSignup').addEventListener('click', () => {
  const checkboxes = document.querySelectorAll('.agree'); // 모든 체크박스 선택
  const allChecked = Array.from(checkboxes).every(checkbox => checkbox.checked); // 모두 체크되었는지 확인

  if (allChecked) {
      document.getElementById('tacouAgree').submit(); // 모든 체크가 되었으면 폼 제출
      window.location.href = '/signup';
    } else {
      alert('모든 약관에 동의해야 다음으로 진행할 수 있습니다.'); // 체크가 하나라도 안 되었으면 경고 메시지
  }
});