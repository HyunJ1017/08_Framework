// 이메일 드롭박스 적용
function changeEmail() {
  const emailDomain = document.getElementById("emailDomain");
  const emailTypeSelect = document.getElementById("email-type");
  
  // 선택된 옵션의 값 가져오기
  const selectedValue = emailTypeSelect.value;

  // "직접입력" 옵션이 선택된 경우
  if (selectedValue === "") {
    emailDomain.value = ''; // 입력 필드 초기화
    emailDomain.disabled = false; // 입력 필드 활성화
  } else {
    emailDomain.value = selectedValue; // 선택된 값 입력 필드에 설정
    emailDomain.disabled = true; // 입력 필드 비활성화
  }
}

/***** 회원 가입 유효성 검사 *****/

/* 필수 입력 항목의 유효성 검사여부를 체크하기 위한 객체(체크리스트)*/
const checkObj = {
  "memberEmail"     : false,
  "memberDomain"    : false,
  "memberId"        : false,
  "memberPw"        : false,
  "memberPwConfirm" : false,
  "memberNickname"  : false,
  "memberTel"       : false,
  "authKey"         : false 
};

// 이메일 유효성 검사

const emailDomain = document.getElementById("emailDomain");
const emailLocalPart = document.getElementById("memberEmail");
const emailMessage = document.getElementById("emailMessage");

// 2) 이메일 관련 메시지 작성
const emailMessageObj = {};
emailMessageObj.normal = "이메일을 입력해 주세요";
emailMessageObj.invalid = "유효하지 않은 이메일 형식 입니다";
emailMessageObj.duplication = "이미 사용중인 이메일 입니다.";
emailMessageObj.check = "사용 가능한 이메일 입니다.";
//3) 이메일 입력 될때 마다 유효성 검사 

// 로컬파트 유효성 검사
emailLocalPart.addEventListener("input", () =>{

  // 입력된 이메일이 없을 경우
  if( emailLocalPart.value.trim().length === 0){
    // 4)이메일 메시지를 normal 상태 메시지로 변경
    emailMessage.innerText = emailMessageObj.normal;

    // 
    emailMessage.classList.remove("confirm", "error");

    // checkObj에서 memberEmail을 false로 변경
    checkObj.memberEmail = false;

    emailLocalPart.value = ""; // 잘못 입력된 값(띄어쓰기) 제거
    
    return;


  }
  // 5) 이메일 형식이 맞는지 검사 (정규 표현식을 이용한 검사)

const regEx1 = /^[a-zA-Z0-9]{5,12}$/;

// 입력값이 정규표현식이랑 맞는지 검사
if( regEx1.test(emailLocalPart.value.trim()) === false){
  emailMessage.innerText = emailMessageObj.invalid; // 유효 X 메시지
  emailMessage.classList.add("error"); // 안된다고 띄우기
  emailMessage.classList.remove("confirm"); // 넘어가기
  checkObj.memberEmail = false; // 유효하지 않다고 체크
  return
}else{
   emailMessage.innerText = '유효한 아이디 입니다'
};

});

// 도메인 유효성 검사
emailDomain.addEventListener("input", () =>{

  // 입력된 이메일이 없을 경우
  if( emailDomain.value.trim().length === 0){
    // 4)이메일 메시지를 normal 상태 메시지로 변경
    emailMessage.innerText = emailMessageObj.normal;

    // #emailMessage에 색상 관련 클래스를 모두 제거
    emailMessage.classList.remove("confirm", "error");

    // checkObj에서 memberEmail을 false로 변경
    checkObj.memberDomain = false;

    emailDomain.value = ""; // 잘못 입력된 값(띄어쓰기) 제거
    
    return;


  }
  // 5) 이메일 형식이 맞는지 검사 (정규 표현식을 이용한 검사)

const regEx = /^[a-zA-Z.]{2,12}$/;

// 입력값이 정규표현식이랑 맞는지 검사
if( regEx.test(emailDomain.value.trim()) === false){
  emailMessage.innerText = emailMessageObj.invalid; // 유효 X 메시지
  emailMessage.classList.add("error"); // 안된다고 띄우기
  emailMessage.classList.remove("confirm"); // 넘어가기
  checkObj.memberDomain = false; // 유효하지 않다고 체크
  return
}else{
 emailMessage.innerText = '유효한 아이디 입니다'
}

})

// localpart + domain 합치기
const memberFullEmail() = () => emailLocalPart.value.trim() + "@" + emailDomain.value.trim();
 
 memberFullEmail.addEventListener("click",() =>{

//6) 이메일 중복 검사 (AJAX)

fetch("/emailCheck?email=" + memberFullEmail)
  .then(response => {
    if(response.ok){ // HTTP 응답 상태 코드 200번(응답 성공)
      return response.json(); // 응답 결과를 text로 파싱
    }

    throw new Error("이메일 중복 검사 에러");
  })
  .then(count => {
    // 매개 변수 count : 첫 번째 then에서 return된 값이 저장된 변수

    if(count == 1){ // 중복인 경우
      emailMessage.innerText = emailMessageObj.duplication; // 중복 메시지
      emailMessage.classList.add("error");
      emailMessage.classList.remove("confirm");
      checkObj["member-domain"] = false;
      return;
    } 

    // 중복이 아닌 경우
    emailMessage.innerText = emailMessageObj.check; // 중복X 메시지
    emailMessage.classList.add("confirm");
    emailMessage.classList.remove("error");
    checkObj.memberEmail = true; // 유효한 이메일임을 기록

  })
  .catch( err => console.error(err) );
 
  })


// 인증번호 


// 아이디 중복 검사

/* ----- 아이디 유효성 검사 ----- */
// 1) 아이디 유효성 검사에 사용되는 요소 얻어오기
const memberId = document.getElementById("memberId");
const idMessage = document.querySelector("#idMessage");

// 2) 아이디 관련 메시지 작성
const idMessageObj = {};
idMessageObj.normal = "한글,영어,숫자로만 3~10글자";
idMessageObj.invalid = "유효하지 않은 아이디 형식 입니다";
idMessageObj.duplication = "이미 사용중인 아이디 입니다.";
idMessageObj.check = "사용 가능한 아이디 입니다.";

// 3) 아이디 입력 시 마다 유효성 검사
memberId.addEventListener("input", () => {

  // 입력 받은 닉네임
  const inputId = memberId.value.trim();

  // 4) 입력된 아이디이 없을 경우
  if(inputId.length === 0){
    idMessage.innerText = idMessageObj.normal;
    idMessage.classList.remove("confirm", "error");
    checkObj.memberId = false;
    memberId.value = "";
    return;
  }

  // 5) 아이디 유효성 검사(정규 표현식)
  const regEx = /^[a-zA-Z0-9]{3,10}$/; // 한글,영어,숫자로만 3~10글자
  if(regEx.test(inputId) === false){
    idMessage.innerText = idMessageObj.invalid;
    idMessage.classList.remove("confirm");
    idMessage.classList.add("error");
    checkObj.memberId = false;
    return;
    
  }else{
    idMessage.innerText = '유효한 아이디 입니다'
  }
  // 6) 아이디 중복 검사
  fetch("idCheck?memberId=" + inputId)
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("아이디 중복 검사 에러");
  })
  .then(count => {
    if(count == 1){
      idMessage.innerText = idMessageObj.duplication;
      idMessage.classList.remove("confirm");
      idMessage.classList.add("error");
      checkObj.memberId = false;
      return;
    }

    idMessage.innerText = idMessageObj.check;
    idMessage.classList.remove("error");
    idMessage.classList.add("confirm");
    checkObj.memberId = true;

  })
  .catch(err => console.error(err));
});

// 비밀번호 , 비밀번호 확인 일치 불일치 검사


// 닉네임
/* ----- 닉네임 유효성 검사 ----- */
// 1) 닉네임 유효성 검사에 사용되는 요소 얻어오기
const memberNickname = document.getElementById("memberNickname");
const nickMessage = document.querySelector("#nickMessage");

// 2) 닉네임 관련 메시지 작성
const nickMessageObj = {};
nickMessageObj.normal = "한글,영어,숫자로만 3~10글자";
nickMessageObj.invalid = "유효하지 않은 닉네임 형식 입니다";
nickMessageObj.duplication = "이미 사용중인 닉네임 입니다.";
nickMessageObj.check = "사용 가능한 닉네임 입니다.";

// 3) 닉네임 입력 시 마다 유효성 검사
memberNickname.addEventListener("input", () => {

  // 입력 받은 닉네임
  const inputNickname = memberNickname.value.trim();

  // 4) 입력된 닉네임이 없을 경우
  if(inputNickname.length === 0){
    nickMessage.innerText = nickMessageObj.normal;
    nickMessage.classList.remove("confirm", "error");
    checkObj.memberNickname = false;
    memberNickname.value = "";
    return;
  }

  // 5) 닉네임 유효성 검사(정규 표현식)
  const regEx = /^[a-zA-Z0-9가-힣]{3,10}$/; // 한글,영어,숫자로만 3~10글자
  if(regEx.test(inputNickname) === false){
    nickMessage.innerText = nickMessageObj.invalid;
    nickMessage.classList.remove("confirm");
    nickMessage.classList.add("error");
    checkObj.memberNickname = false;
    return;
    
  }else{
    nickMessage.innerText = '유효한 닉네임 입니다'
  }
  // 6) 닉네임 중복 검사
  fetch("/nicknameCheck?nickname=" + inputNickname)
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("닉네임 중복 검사 에러");
  })
  .then(count => {
    if(count == 1){
      nickMessage.innerText = nickMessageObj.duplication;
      nickMessage.classList.remove("confirm");
      nickMessage.classList.add("error");
      checkObj.memberNickname = false;
      return;
    }

    nickMessage.innerText = nickMessageObj.check;
    nickMessage.classList.remove("error");
    nickMessage.classList.add("confirm");
    checkObj.memberNickname = true;

  })
  .catch(err => console.error(err));
});

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

 /* 전화번호 유효성 검사 */

const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

const telMessageObj = {};
telMessageObj.normal = "전화번호를 입력해주세요.(- 제외)";
telMessageObj.invalid = "유효하지 않은 전화번호 형식입니다.";
telMessageObj.check = "유효한 전화번호 형식입니다.";

memberTel.addEventListener("input", () => {
  
  const inputTel = memberTel.value.trim();

  if(inputTel.length === 0){
    telMessage.innerText = telMessageObj.normal;
    telMessage.classList.remove("confirm", "error");
    checkObj.memberTel = false;
    memberTel.value = "";
    return;
  }

  const regEx = /^010[0-9]{8}$/; // 010으로 시작, 이후 숫자 8개(총 11자)

  if(regEx.test(inputTel) === false){
    telMessage.innerText = telMessageObj.invalid;
    telMessage.classList.remove("confirm");
    telMessage.classList.add("error");
    checkObj.memberTel = false;
    return;
  }

  telMessage.innerText = telMessageObj.check;
  telMessage.classList.remove("error");
  telMessage.classList.add("confirm");
  checkObj.memberTel = true;
});
// 인증번호 타이머

/* 회원 가입 form 제출 시 전체 유효성 검사 여부 확인 */
const signUpForm = document.querySelector("#signUpForm");

signUpForm.addEventListener("submit", e => {

  // e.preventDefault(); // 기본 이벤트(form 제출) 막기

  // for(let key in 객체)
  // -> 반복마다 객체의 키 값을 하나씩 꺼내서 key 변수에 저장

  // 유효성 검사 체크리스트 객체에서 하나씩 꺼내서
  // false인 경우가 있는지 검사
  for(let key in checkObj){ 

    if( checkObj[key] === false ){ // 유효하지 않은 경우
      let str; // 출력할 메시지 저장

      switch(key){
        case "memberEmail"     : str = "이메일이 유효하지 않습니다"; break;
        case "memberNickname"  : str = "닉네임이 유효하지 않습니다"; break;
        case "memberPw"        : str = "비밀번호가 유효하지 않습니다"; break;
        case "memberPwConfirm" : str = "비밀번호 확인이 일치하지 않습니다"; break;
        case "memberTel"       : str = "전화번호가 유효하지 않습니다"; break;
        case "authKey"         : str = "이메일이 인증되지 않았습니다"; break;
      }

      alert(str); // 경고 출력

      // 유효하지 않은 요소로 focus 이동
      document.getElementById(key).focus();

      e.preventDefault(); // 제출 막기

      return;
    }
  }

  /* 주소 유효성 검사 */
  // - 모두 작성   또는   모두 미작성
  const addr = document.querySelectorAll("[name = memberAddress]");

  let empty = 0; // 비어있는 input의 개수
  let notEmpty = 0; // 비어있지 않은 input의 개수

  // for ~ of 향상된 for문
  for(let item of addr){
    let len = item.value.trim().length; // 작성된 값의 길이
    
    if(len > 0) notEmpty++; // 비어있지 않은 경우
    else        empty++;    // 비어있을 경우
  }

  // empty, notEmpty 중 3이 하나도 없을 경우
  if( empty < 3 && notEmpty < 3 ){
    alert("주소가 유효하지 않습니다(모두 작성 또는 미작성)");
    e.preventDefault();
    return;
  }

});