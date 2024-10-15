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

// 주소 API
function findAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      var addr = ''; // 주소 변수
      
      // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else { // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }
  
      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById('postcode').value = data.zonecode;
      document.getElementById("address").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("detailAddress").focus();
    }
  }).open();
  }
  
    // 주소 검색 버튼 클릭 시
  const searchAddress = document.querySelector("#searchAddress");
  searchAddress.addEventListener("click", findAddress);
  
  /*전화번호 유효성 검사*/
  const memberTel = document.querySelector("#memberTel");
  
  memberTel?.addEventListener("input", ()=>{ // ? 안전탐색 연산자
  
  // 입력된 전화 번호
  const inputTel = memberTel.value.trim();
  
  // 전화번호 정규식 검사
  
  // 010으로 시작하고 11글자 
  const validFormat = /^010\d{8}$/; 
  
  // 입력 받은 전화번호가 유효한 형식이 아닌 경우 
  if(!validFormat.test(inputTel)){
  
   memberTel.classList.add("red");
   memberTel.classList.remove("green");
  
   checkObj.memberTel = false;
  return;
  }else{
    memberTel.classList.remove("red");
   memberTel.classList.add("green");
  
   checkObj.memberTel = true;
  }