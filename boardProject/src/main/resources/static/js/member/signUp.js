
/* 다음주소API로 주소 검색하기 */
function findAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ''; // 주소 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
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

// 주소 검색 버튼 클릭시
const searchAddress = document.querySelector("#searchAddress");
searchAddress.addEventListener( "click", findAddress ); // searchAddress 함수코드를 그대로 삽입
//searchAddress.addEventListener( "click", findAddress() ); searchAddress 함수를 실행


//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


/* 회원 가입 유효성 검사 */

/* 필수 입력 항목의 유효성 검사여부를 체크하기 위한 객체(체크리스트) */
const checkObj = {
  "memberEmail"     : false,
  "authKey"         : false,
  "memberPw"        : false,
  "memberPwConfirm" : false,
  "memberNickName"  : false,
  "memberTel"       : false
}


//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


/* 이메일 유효성 검사
 1) 이메일 유효성 검사에 필요한 요소 얻어오기
 2) 이메일 메시지를 미리 작성
 3) 이메일이 입력될 대 마다 유효성 검사를 수행

 - 입력된 이메일이 없을경우
 - 이메일 형식이 맞는지 검사(정규 표현식을 이용한 검사)
 - 이메일 중복 검사(AJAX)

 이메일 상태 메시지 변경
 이메일 상태 메시지 색상classList 관리
 유효성검사 체크리스트 수정
 추가 데이터수정(HTML의 공백문자 제거 등)
 유효성검사 불합격시 return
*/

// 1) 이메일 유효성 검사에 필요한 요소 얻어오기
const memberEmail  = document.querySelector("#memberEmail");
const emailMessage = document.querySelector("#emailMessage");

// 2) 이메일 메시지를 미리 작성
const emailMessageObj       = {};
emailMessageObj.nomal       = '메일을 받을 수 있는 이메일을 입력해주세요.';
emailMessageObj.invaild     = '알맞은 이메일 형식으로 작성해 주세요.';
emailMessageObj.duplication = '이미 사용중인 이메일 입니다.';
emailMessageObj.check       = '사용 가능한 이메일 입니다.';

// 3) 이메일이 입력될 대 마다 유효성 검사를 수행
memberEmail.addEventListener("input", e => {
  // 입력된 값 얻어오기
  const inputEmail = memberEmail.value.trim();

  // 4) 입력된 이메일이 없을경우
  if( inputEmail.length === 0 ){

    // 이메일 메세지를 nomal 상태 메시지로 변경
    emailMessage.innerText = emailMessageObj.nomal;

    // #emailMessage에 생상 관련 클래스를 모두 제거
    emailMessage.classList.remove("confirm", "error");

    // checkObj에서 memberEmail을 false로 변경
    checkObj.memberEmail = false;
    
    memberEmail.value = ""; // 잘못 입력된 값(띄어쓰기) 제거
    
    return;
  }
  
  // 5) 이메일 형식이 맞는지 검사(정규 표현식을 이용한 검사)
  
  // 이메일 형식 정규표현식 객체
  const regEx = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  
  if(regEx.test(inputEmail) === false){
    // 입력값이 이메일 형식이 아닌경우

    emailMessage.innerText = emailMessageObj.invaild;
    emailMessage.classList.add("error");
    emailMessage.classList.remove("confirm");
    checkObj.memberEmail = false;
    return;
  }

  // 6) 이메일 중복 검사(AJAX)
  fetch("/member/emailCheck?email=" + inputEmail)
  .then( response => {
    if(response.ok){  // HTTP 응답 상태 코드 200번
      return response.text(); // 응답결과를 text로 파싱
    }

    throw new Error( "이메일 중복 검사 오류 : " + response.status );
  } )
  .then(count => {

    if( count > 0 ){
      // 중복되는 이메일이 있을 시
      emailMessage.innerText = emailMessageObj.duplication;
      emailMessage.classList.add("error");
      emailMessage.classList.remove("confirm");
      checkObj.memberEmail = false;
      return;
    } else {
      // 중복되는 이메일이 없을 시
      emailMessage.innerText = emailMessageObj.check;
      emailMessage.classList.add("confirm");
      emailMessage.classList.remove("error");
      checkObj.memberEmail = true;
      return;
    }
  })
  .catch( err => console.error(err) );

});


//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


/* 닉네임 유효성 검사
 1) 닉네임 유효성 검사에 사용되는 요소 얻어오기
 2) 닉네임 메시지를 미리 작성
 3) 닉네임이 입력될 때 마다 유효성 검사를 수행

 - 입력된 닉네임이 없을경우
 - 닉네임 형식이 맞는지 검사(정규 표현식을 이용한 검사)
 - 닉네임 중복 검사(AJAX)

 닉네임 상태 메시지 변경
 닉네임 상태 메시지 색상classList 관리
 유효성검사 체크리스트 수정
 추가 데이터수정(HTML의 공백문자 제거 등)
 유효성검사 불합격시 return
*/

// 1) 닉네임 유효성 검사에 사용되는 요소 얻어오기
const memberNickname = document.querySelector("#memberNickname");
const nickMessage    = document.querySelector("#nickMessage");

// 2) 닉네임 메시지를 미리 작성
const nickMessageObj = {};
nickMessageObj.normal = "한글,영어,숫자로만 3~10글자";
nickMessageObj.invaild = "유효하지 않은 닉네임 형식 입니다";
nickMessageObj.duplication = "이미 사용중인 닉네임 입니다.";
nickMessageObj.check = "사용 가능한 닉네임 입니다.";

// 3) 닉네임이 입력될 때 마다 유효성 검사를 수행
memberNickname.addEventListener("input", () => {

  // 입력받은 닉네임
  const inputNickname = memberNickname.value.trim();

  // 4) 입력받은 닉네임이 없을 경우
  if( inputNickname.length === 0 ){
    nickMessage.innetText = nickMessageObj.normal;
    nickMessage.classList.remove("confirm", "error");
    memberNickname.value = '';
    checkObj.memberNickName = false;
    return;
  }

  // 5) 닉네임 유효성 검사(정규표현식)
  const regEx = /^[a-zA-Z0-9가-힣]{3,10}$/; // 한글,영어,숫자로만 3~10글자
  if(regEx.test(inputNickname) === false){
    nickMessage.innerText = nickMessageObj.invaild;
    nickMessage.classList.add("error");
    nickMessage.classList.remove("confirm");
    checkObj.memberNickName = false;
    return;
  }
  
  // 6) 닉네임 중복 검사
  fetch("/member/duplication?nickname=" + inputNickname)
  .then(response => {
    if( response.ok ) return response.text();
    throw new Error("닉네임 중복 검사 오류 : " + response.status )
  })
  .then(result => {
    
    if( result > 0 ){
      // 중복되는 닉네임이 존재할 때
      nickMessage.innerText = nickMessageObj.duplication;
      nickMessage.classList.add("error");
      nickMessage.classList.remove("confirm");
      checkObj.memberNickName = false;
      return;
    } else {
      // 중복되는 닉네임이 없을 때
      nickMessage.innerText = nickMessageObj.check;
      nickMessage.classList.add("confirm");
      nickMessage.classList.remove("error");
      checkObj.memberNickName = true;
      return;
    }

  })
  .catch(err => console.error(err));
  
});


//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


/* 전화번호 유효성 검사
1) 전화번호 유효성 검사에 사용되는 요소 얻어오기
2) 전화번호 메시지를 미리 작성
3) 전화번호 입력될 때 마다 유효성 검사를 수행

- 입력된 전화번호 없을경우
- 전화번호 형식이 맞는지 검사(정규 표현식을 이용한 검사)
- 전화번호 중복 검사(AJAX)

전화번호 상태 메시지 변경
전화번호 상태 메시지 색상classList 관리
유효성검사 체크리스트 수정
추가 데이터수정(HTML의 공백문자 제거 등)
유효성검사 불합격시 return
*/

// 1) 전화번호 유효성 검사에 사용되는 요소 얻어오기
const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

// 2) 전화번호 메시지를 미리 작성
const telMessageObj ={};
telMessageObj.normal = "전화번호를 입력해주세요.(- 제외)";
telMessageObj.invaild = "유효하지 않은 전화번호 형식 입니다";
telMessageObj.check = "사용 가능한 전화번호 입니다.";

// 3) 전화번호 입력될 때 마다 유효성 검사를 수행
memberTel.addEventListener("input", ()=>{
  
  const inputTel = memberTel.value.trim();
  
  if(inputTel.length === 0){
    telMessage.innetText = telMessageObj.normal;
    telMessage.classList.remove("confirm", "error");
    memberTel.value = '';
    checkObj.memberTel = false;
    return;
  }
  
  const regEx = /^010[0-9]{8}$/; // 010으로 시작, 이후 숫자 8개(총 11자)
  
  if(regEx.test(inputTel) === false){
    telMessage.innerText = telMessageObj.invaild;
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


//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


/* 비밀번호 유효성 검사 */

const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");

const pwMessageObj = {};
pwMessageObj.normal = "영어,숫자,특수문자 1글자 이상, 6~20자 사이.";
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
  const specialCharCheck = /[\!\@\#\_\-]/.test(inputPw); // 특수문자 포함

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
  
  // 비밀번호 확인 작성된 상태에서 비밀번호가 입력된 경우
  if( memberPwConfirm.value.trim().length > 0 ){
    checkPw(); // 같은지 비교하는 함수 실행
  }
});

/* ---------- 비밀번호, 비밀번호 확인 같은지 검사하는 함수 ---------- */
function checkPw(){
  
  // 같을경우
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
/* ----------         비밀번호 확인이 입력 되었을 때        ---------- */
memberPwConfirm.addEventListener("input", () => {

  // 비밀번호 input에 작성된 값이 유효한 형식일때만 비교
  if(checkObj.memberPw === true){
    checkPw();
    return;
  }

  // 비밀번호 input에 작성된 값이 유효하지 않을경우
  checkObj.memberPwConfirm = false;

});


//=============================================================================================
//=============================================================================================


/*---------- 이메일 인증 ----------*/

/*
1. 인증번호를 작성된 이메일로 발송하기
- 1. 작성된 이메일이 유효하기 않은 경우
- 2. 비동기로 서버에서 작성된 이메일로 인증코드 발송(AJAX)
- 3. 이메일 발송 메시지 출력 + 5분 타이머

2. 인증 번호를 입력하고 인증하기 버튼을 클릭한 경우
     (추가) 타이머가 00:00인 경우 클릭방지
- 1. 인증번호 6자리가 입력이 되었는지 확인
- 2. 입력된 이메일과 인증번호를 비동기로 서버로 전달하여
-    Redis에 저장된 이메일(key), 인증번호(value)와 일치하는지 확인
- 3. 일치하지 않는 경우 alert("인증번호가 일치하지 않습니다.")
- 4. 일치하지 하는 경우 타이머 멈춤 + 메세지 출력 + checkObj true
*/

// 1. 인증번호를 작성된 이메일로 발송하기

// 인증번호 받기 버튼
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");

// 인증 관련 메시지 출력 sapn
const authKeyMessage = document.querySelector("#authKeyMessage");

const initTime = "05:00"; // 인증 초기시간
const initMin  = 4;       // 초기값 5분에서 1초 감소된 후 분
const initSec  = 59;      // 초기값 5분에서 1초 감소된 후 초
let min        = initMin; // 실제 줄어든 시간 분
let sec        = initSec; // 실제 줄어든 시간 초
let authTimer;            // 타이머 역할의 setInterval을 저장할 변수
                          // 타이머를 멈추는 clearInterval 수행을 위해 필요

// 인증 번호 받기 클릭시
sendAuthKeyBtn.addEventListener("click", () => {

  // 초기화
  checkObj.authKey = false;
  authKeyMessage.innerText = '';
  if( authTimer != undefined ){
    clearInterval(authTimer); // 이전 인증타이머 초기화
  }

  // 1.) 작성된 이메일이 유효하기 않은 경우
  if(checkObj.memberEmail === false){
    alert("유효한 이메일 작성 후 클릭하세요");
    return;
  }
  // 2.) 비동기로 서버에서 작성된 이메일로 인증코드 발송(AJAX)
  fetch("/email/sendAuthKey", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : memberEmail.value
    // Post방식으로 입력된 이메일을 body에 담아 제출
  })

  .then(response => {
    if(response.ok) return response.text();
    throw new Error("이메일인증발송 오류" + response.status)
  })

  .then(result => {
    console.log(result);
  })

  .catch(err => console.error(err));
  // fetch문 종료


  /* 메일이 비동기로 보내지는 동안 아래 JS 코드 수행 */

  // 3.) 이메일 발송 메시지 출력 + 5분 타이머

  alert("인증번호가 발송되었습니다.");

  authKeyMessage.innerText = initTime; // 05:00 문자열 출력

  authKeyMessage.classList.remove("confirm", "error"); // 검정글씨

  // 1초가 지날 때 마다 함수 내부 내용이 실행되는 setInterval 작성
  // authTimer = setInterval(() => {}, 1000);
  authTimer = setInterval(() => {

    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;
    
    // 00분 0초 정지
    if( min === 0 && sec === 0) {
      checkObj.authKey = false;
      clearInterval(authTimer); // 1초마다 동작하는 secInterval 멈춤
      authKeyMessage.classList.add("error");  // 빨간글씨
      authKeyMessage.classList.remove("confirm");
    }

    if( sec === 0 ){    // 출력된 초가 0인 경우(1분지남)
      sec = 60;
      min--;            // q분 감소
    }

    sec--;              // 1초가 지날 때 마다 xec값 1씩 감소

  }, 1000);


});

/* 전달받은 숫자가 10 미만(한자리수) 인 경우 앞에 영을 붙여서 반환하는 함수 */
function addZero(num){
  if(num<10) return "0" + num;
  else       return num;
}

//------------------------------------------------------------------

// 2. 인증 번호를 입력하고 인증하기 버튼을 클릭한 경우
// 1.) 인증번호 6자리가 입력이 되었는지 확인
// 2.) 입력된 이메일과 인증번호를 비동기로 서버로 전달하여
//     Redis에 저장된 이메일(key), 인증번호(value)와 일치하는지 확인
// 3.) 일치하지 않는 경우 alert("인증번호가 일치하지 않습니다.")
// 4.) 일치하지 하는 경우 타이머 멈춤 + 메세지 출력 + checkObj true
// (추가) 타이머가 00:00인 경우 클릭방지

const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");
const authKey         = document.querySelector("#authKey");

checkAuthKeyBtn.addEventListener("click", () => {
  
  // (추가) 타이머가 00:00인 경우 클릭방지
  if(min===0 && sec===0){
    alert("인증번호 입력 제한시간을 초과 하였습니다.");
    return;
  }
  
  // 1.) 인증번호 6자리가 입력이 되었는지 확인
  if(authKey.value.trim().length < 6){
    alert("인증번호가 잘못 입력 되었습니다.");
    return;
  }

  // 2.) 입력된 이메일과 인증번호를 비동기로 서버로 전달하여
  //     Redis에 저장된 이메일(key), 인증번호(value)와 일치하는지 확인
  // AJAX로 여러 데이터를 서버로 전달하고 싶을 땐
  // JSON 형태로 값을 전달함

  // 서버로 전달할 데이터를 저장한 객체
  const obj = {
    "email" : memberEmail.value,  // 입력한 이메일
    "authKey" : authKey.value     // 입력한 인증번호
  };

  fetch("/email/checkAuthKey",{
    method : "POST",
    headers : {"Content-Type":"application/json"},
    body : JSON.stringify(obj)
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("인증키 체크 오류" + response.status)
  })
  .then(result => {
    // console.log( result );

    // 3.) 일치하지 않는 경우 alert("인증번호가 일치하지 않습니다.")
    if(result === "false"){
      alert("인증번호가 일치하지 않습니다.");
      checkObj.authKey = false;
      return;
    }

    // 4.) 일치하지 하는 경우 타이머 멈춤 + 메세지 출력 + checkObj true
    clearInterval(authTimer);
    authKeyMessage.innerText = '인증되었습니다.';
    authKeyMessage.classList.add("confirm");
    authKeyMessage.classList.remove("error");
    checkObj.authKey = true;
  })
  .catch(err => console.error(err));

});



//=============================================================================================
//=============================================================================================



/* 회원가입 form 제출 시 전체 유효성 검사 실시 */

const signUpForm = document.querySelector("#signUpForm");
signUpForm.addEventListener("submit", e=>{

  // for(let key in 객체)
  // -> 객체의 키 값을 하나씩 꺼내서 key 변수에 저장

  for(let key in checkObj){
    if( checkObj[key] === false ){
      let str;

      switch(key){
        case 'memberEmail'     : str = '이메일이 유효하지 않습니다.'; break;
        case 'authKey'         : str = '이메일이 인증되지 않았습니다.'; break;
        case 'memberPw'        : str = '비밀번호가 유효하지 않습니다.'; break;
        case 'memberPwConfirm' : str = '비밀번호가 일치하지 않습니다.'; break;
        case 'memberNickName'  : str = '사용할 수 없는 닉네임 입니다.'; break;
        case 'memberTel'       : str = '전화번호가 유효하지 않습니다.'; break;
      }

      alert(str);

      // 유효하지 않은 요소로 포커스 이동
      document.getElementById(key).focus();

      e.preventDefault();

      return;
    }
  }

//--------------------------------------------------------------------

  /* 주소 유효성검사 */

  // 모두작성 또는 모두 미작성
  const addr = document.querySelectorAll("[name = memberAddress]")

  let empty = 0;
  let notEmpty = 0;

  for(let item of addr){// 멤버주소의 우편번호, 주소, 상세주소
    let len = item.value.trim().length;
    if(len>0) notEmpty++;
    else empty++;
  }

  // 둘다 3이 아닌경우 ( "모두작성 또는 모두 미작성"이 아닌경우 )
  if( notEmpty !== 3 && empty !== 3 ){
    alert("주소가 유효하지 않습니다.");
    e.preventDefault();
    return;
  }
  
});

