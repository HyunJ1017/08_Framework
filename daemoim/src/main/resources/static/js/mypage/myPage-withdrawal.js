//이메일 드롭박스로 적용하기
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

// 탈퇴 페이지
const withdrawal = document.querySelector("#withdrawal");
withdrawal?.addEventListener("submit", e=>{

/*   // 비밀번호 입력 확인 
  const memberPw = document.querySelector("#memberPw");
  if(memberPw.value.trim().length === 0){// 미입력시 
    alert("비밀번호를 입력해 주세요");
    e.preventDefault();
    return;
  } */

  //2) 체크 되었는지 검사 
  const agree = document.querySelector("#agree");

  if(agree.checked === false){// 체크가 되어있지 않은 경우 
    alert("탈퇴를 원하시면 동의를 체크해 주세요");
    e.preventDefault();
    return;
  }

  //3) confirm을 이용해서 탈퇴할건지 확인
  if(confirm("진짜 탈퇴 할껴?") === false){// 취소 클릭시 
    alert("오케이");
    e.preventDefault();
    return;


  }

});