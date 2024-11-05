// 비밀번호 , 비밀번호 확인 일치 불일치 검사

/* 비밀번호 유효성 검사 */

const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");

const pwMessageObj = {};
pwMessageObj.normal = "영어,숫자,특수문자 (!,@,#,$,%,^,&,*)1글자 이상입력해 주세요"+"\n" +"(최소 6 글자 최대 20글자) ";
pwMessageObj.invaild = "유효하지 않은 비밀번호 형식입니다.";
pwMessageObj.vaild = "유효한 비밀번호 형식입니다.";
pwMessageObj.error = "비밀번호가 일치하지 않습니다.";
pwMessageObj.check = "비밀번호가 일치 합니다.";


memberPw.addEventListener("input", () => {
  
  const inputPw = memberPw.value.trim();

  if(inputPw.length === 0){ // 비밀번호 미입력
    pwMessage.innerText = pwMessageObj.normal;
    pwMessage.classList.remove("confirm", "error");
    checkObj.memberPw = false;
    memberPw.value = "";
    return;
  }

  // 비밀번호 정규표현식 검사
  const lengthCheck = inputPw.length >= 6 && inputPw.length <= 20;
  const letterCheck = /[a-zA-Z]/.test(inputPw); // 영어 알파벳 포함
  const numberCheck = /\d/.test(inputPw); // 숫자 포함
  const specialCharCheck = /[\!\@\#\$\%\^\&\*]/.test(inputPw); // 특수문자 포함

  // 조건이 하나라도 만족하지 못하면
  if ( !(lengthCheck && letterCheck && numberCheck && specialCharCheck) ) {
    pwMessage.innerText = pwMessageObj.invaild;
    pwMessage.classList.remove("confirm");
    pwMessage.classList.add("error");
    checkObj.memberPw = false;
    return;
  }


  pwMessage.innerText = pwMessageObj.vaild;
  pwMessage.classList.remove("error");
  pwMessage.classList.add("confirm");
  checkObj.memberPw = true;


  // 비밀번호 확인이 작성된 상태에서
  // 비밀번호가 입력된 경우
  if(memberPwConfirm.value.trim().length > 0){
    checkPw(); // 같은지 비교하는 함수 호출
  }

});


/* ----- 비밀번호, 비밀번호 확인 같은지 검사하는 함수 ----- */
function checkPw(){

  // 같은 경우
  if(memberPw.value === memberPwConfirm.value){
    pwMessage.innerText = pwMessageObj.check;
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true;
    return;
  }

  // 다른 경우
  pwMessage.innerText = pwMessageObj.error;
  pwMessage.classList.add("error");
  pwMessage.classList.remove("confirm");
  checkObj.memberPwConfirm = false;
}


/* ----- 비밀번호 확인이 입력 되었을 때  ----- */
memberPwConfirm.addEventListener("input", () => {

  // 비밀번호 input에 작성된 값이 유효한 형식일때만 비교
  if(checkObj.memberPw === true){
    checkPw();
    return;
  }

  // 비밀번호 input에 작성된 값이 유효하지 않은 경우
  checkObj.memberPwConfirm = false;
});