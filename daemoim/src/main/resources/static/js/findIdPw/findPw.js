const checkObj = {
  "findPwMemberEmail": false,
  "memberId": false,
};

const findPwMemberId = document.querySelector("#findPwMemberId");
const findPwMemberEmail = document.querySelector("#findPwMemberEmail");
const emailMessage = document.getElementById("findPwEmailMessage");

const emailMessageObj = {
  normal: "",
  invalid: "알맞은 이메일 형식으로 작성해 주세요.",
  duplication: "아이디와 이메일이 일치하지 않습니다.",
  check: "존재하는 이메일입니다."
};

// 아이디 유효성 검사
findPwMemberId.addEventListener("input", () => {
  const inputId = findPwMemberId.value.trim();
  const regEx = /^[a-zA-Z0-9]{3,10}$/;

  if (inputId.length === 0 || !regEx.test(inputId)) {
    checkObj.memberId = false;
    return;
  }
  checkObj.memberId = true;
});

// 이메일 유효성 검사
findPwMemberEmail.addEventListener("input", () => {
  const inputEmail = findPwMemberEmail.value.trim();
  const inputId = findPwMemberId.value.trim();
  const emailRegEx = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  if (inputEmail.length === 0 || !emailRegEx.test(inputEmail)) {
    emailMessage.innerText = emailMessageObj.invalid;
    emailMessage.classList.add("error");
    emailMessage.classList.remove("confirm");
    checkObj.findPwMemberEmail = false;
    return;
  }

  // 아이디와 이메일이 유효한지 서버로 확인 요청
  fetch(`/findPwEmail?id=${encodeURIComponent(inputId)}&email=${encodeURIComponent(inputEmail)}`)
    .then(response => {
      if (response.ok) return response.text();
      throw new Error("아이디와 이메일 중복 검사 요청 에러");
    })
    .then(count => {
      if (count === "0") {
        emailMessage.innerText = emailMessageObj.duplication;
        emailMessage.classList.add("error");
        emailMessage.classList.remove("confirm");
        checkObj.findPwMemberEmail = false;
      } else {
        emailMessage.innerText = emailMessageObj.check;
        emailMessage.classList.add("confirm");
        emailMessage.classList.remove("error");
        checkObj.findPwMemberEmail = true;

        // 아이디와 이메일이 유효하다고 확인되면 입력 필드를 비활성화
        findPwMemberId.disabled = true;
        findPwMemberEmail.disabled = true;
      }
    })
    .catch(err => console.error("Error: 아이디와 이메일 중복 검사 에러", err));
});



/*----- 이메일 인증 -----*/

//[1] 인증 번호를 작성된 이메일로 발송하기

// 인증 번호 받기 버튼
const sendFindPwAuthKeyBtn = document.querySelector("#sendFindPwAuthKeyBtn");

// 인증 관련 메시지 출력 span
const authKeyMessage = document.querySelector("#authKeyMessage");

const initTime = "05:00"; // 인증 초기 시간 지정
const initMin = 4; // 초기값 5분에 1초 감소된 후 분
const initSec = 59; // 초기값 5분에 1초 감소된 후 초

// 실제 줄어둔 시간(분/초)를 저장할 변수
let min = initMin;
let sec = initSec;

let authTimer; // 타이머 역할의 setInterval을 저장할 변수
      // -> 타이머를 멈추는 clearInterval 수행을 위해 필요


// 인증 번호 받기 버튼 클릭 시
sendFindPwAuthKeyBtn.addEventListener("click", () => {

  checkObj.authKey = false; // 인증 안된 상태로 기록
  authKeyMessage.innerText = ""; // 인증 관련 메시지 삭제

  if(authTimer != undefined){
    clearInterval(authTimer); // 이전 인증 타이머 없애기
  }

  // 1) 작성된 이메일이 유효하지 않은 경우
  if(checkObj.findPwMemberEmail === false){
    alert("유효한 이메일 작성 후 클릭하세요");
    return;
  }


  // 2) 비동기로 서버에서 작성된 이메일로 인증코드 발송(AJAX)
  fetch("/email/sendFindIdAuthKey", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : findPwMemberEmail.value

    // POST 방식으로 
    // /email/sendAuthKey 요청을 처리하는 컨트롤러에
    // 입력된 이메일을 body에 담아서 제출
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("이메일 발송 실패");
  })
  .then(result => {
    console.log(result);
  })
  .catch(err => console.error(err));
  alert("인증 번호가 발송 되었습니다");
  authKeyMessage.innerText = initTime; // 05:00 문자열 출력
  authKeyMessage.classList.remove("confirm", "error");// 검정 글씨

  // 1초가 지날 때 마다 함수 내부 내용이 실행되는 setInterval 작성
  authTimer = setInterval(() => {
    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    // 0분 0초인 경우
    if(min === 0 && sec === 0){
      checkObj.authKey = false; // 인증 못했다고 기록
      clearInterval(authTimer); // 1초마다 동작하는 setInterval 멈춤
      authKeyMessage.classList.add("error"); // 빨간 글씨
      authKeyMessage.classList.remove("confirm");
      return;
    }

    if(sec === 0){ // 출력된 초가 0인 경우(1분 지남)
      sec = 60;
      min--; // 분 감소
    }
    sec--; // 1초가 지날 때 마다 sec값 1씩 감소

  }, 1000);
});


/* 전달 받은 숫자가 10 미만(한 자리 수) 인 경우 
 앞에 0을 붙여서 반환하는 함수 */
function addZero(num){
  if(num < 10)  return "0" + num;
  else          return num;
}


//----------------------------------------------------------------

/* 인증 번호를 입력하고 인증하기 버튼을 클릭한 경우 */
const authKey = document.getElementById("authKey");
const checkPwAuthKeyBtn = document.querySelector("#checkPwAuthKeyBtn");

checkPwAuthKeyBtn.addEventListener("click", () => {

  // + (추가 조건) 타이머 00:00인 경우 버튼 클릭 막기
  if(min === 0 && sec === 0){
    alert("인증 번호 입력 제한 시간을 초과하였습니다");
    return;
  }
  
  // 1) 인증 번호 6자리가 입력이 되었는지 확인
  if(authKey.value.trim().length < 6){
    alert("인증 번호가 잘못 입력 되었습니다");
    return;
  }

  // 2) 입력된 이메일과 인증 번호를 비동기로 서버에 전달하여
  // Redis에 저장된 이메일, 인증번호와 일치하는지 확인

  /* AJAX로 여러 데이터를 서버로 전달하고 싶을 땐
    JSON 형태로 값을 전달해야 한다! */

  // 서버로 제출할 데이터를 저장한 객체 생성
  const obj = {
    "email" : findPwMemberEmail.value, // 입력한 이메일
    "authKey" : authKey.value    // 입력한 인증 번호
  };
  
  // JSON.stringify(객체) : 객체 -> JSON 변환(문자열화)
  
  fetch("/email/checkfindIdAuthKey", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("인증 에러");
  })
  .then(result => {
    console.log("인증 결과 : " , result, typeof result);

    // 3) 일치하지 않는 경우
    if(result == 'false'){
      alert("인증 번호가 일치하지 않습니다");
      checkObj.authKey = false;
      return;
    }else{

      // 4) 일치하는 경우
      // - 타이머 멈춤
      clearInterval(authTimer);

      // + "인증 되었습니다" 화면에 초록색으로 출력
      authKeyMessage.innerText = "인증 되었습니다";
      authKeyMessage.classList.add("confirm");
      authKeyMessage.classList.remove("error");

      
      checkObj.authKey = true; // 인증 완료 표시

      location.href = "findIdPw/findPwChangePage?findPwMemberId=" + findPwMemberId.value; 
  
    }
  })
  .catch(err => console.error(err));

});