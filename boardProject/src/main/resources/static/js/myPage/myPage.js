


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

/* 주소 검색버튼 클릭시 */
if(document.querySelector("#findAddressBtn") !== null){
  const findAddressBtn = document.querySelector("#findAddressBtn");
  findAddressBtn.addEventListener("click", findAddress);
}
// => 함수명만 작성하는 경우 : 함수반환

// => 함수명() 작성하는 경우 : 함수에 정의된 내용을 실행


/********************************************************************************* */
/********************************************************************************* */


// 입력값이 유효한 형태인지 표시하는 객체(체크리스트)
const checkObj = {
  "memberNickname" : true,
  "memberTel" : true
}

/****** 닉네임 유효성 검사(Validation) ******/

/* 닉네임검사 */
// - 3글자 이상
// - 중복X
const memberNickname = document.querySelector("#memberNickname");

// 기존 닉네임
const originalNickname = memberNickname?.value;

memberNickname?.addEventListener("input", e=>{
  // input이벤트: 입력과 관련된 모든 동작, 입력, 붙여넣기, 엔터 등등(JS를 이용한 값 세팅 제외)
  
  // trim() : 양쪽 공백 제거
  const inputValue = memberNickname.value.trim();

  if( inputValue.length < 3 ){ // 3글자 미만인경우
    memberNickname.classList.remove("green");
    memberNickname.classList.add("red");

    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;

    return;
  }

  // 입력된 닉네임이 기존 닉네임과 같을 경우
  if( inputValue === originalNickname ){ // 3글자 미만인경우
    memberNickname.classList.remove("green");
    memberNickname.classList.remove("red");

    // 닉네임이 유효하다고 기록
    checkObj.memberNickname = true;

    return;
  }

  /* 닉네임 유효성 검사 */
  // - 영어 또는 숫자 또는 한글만 작성 가능
  // - 3글자 ~ 10글자
  const lengthCheck = inputValue.length >= 3 && inputValue.length <= 10;
  const validCharactersCheck = /^[a-zA-Z0-9가-힣]+$/.test(inputValue); // 영어, 숫자, 한글만 허용
  //  ^ : 시작
  //  $ : 끝
  // [] : 한 칸(한 문자)에 들어갈 수 있는 문자 패턴 기록
  //  + : 1개 이상

  if( !( lengthCheck && validCharactersCheck ) ){
    memberNickname.classList.remove("green");
    memberNickname.classList.add("red");

    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;
    return;
  }

  // 비동기로 입력된 닉네임이 데이터베이스에 존재하는지 확인하는 Ajax코드(fetch() API) 작성
  // GET방식 요청, 왜 상대경로로 안보냄?
  fetch("checkNickname?input=" + inputValue)
  .then(response => {
    if(response.ok){
      return response.text();
    }
    throw new Error("중복검사 실패" + response.status);
  })

  .then(result => {

    if(result > 0){ // 중복인 경우
      memberNickname.classList.add("red");
      memberNickname.classList.remove("green");
      checkObj.memberNickname = false;  // 체크리스트에 false 기록
      return;
    }
    // 중복이 아닌경우
    memberNickname.classList.add("green");
    memberNickname.classList.remove("red");
    checkObj.memberNickname = true;  // 체크리스트에 false 기록
  })

  .catch(err => console.log(err));

});


/********************************************************************************* */
/********************************************************************************* */


/* 비밀번호 변경 유효성 검사

1.  현재 비밀번호
    새 비밀번호
    새 비밀번호 확인
    3가지 입력여부 체크

2. 새 비밀번호가 알맞은 형태로 작성 되었는가 확인
  - 영어(대소문자 가리지 X) 1글자 이상
  - 숫자 1글자 이상
  - 특수문자( ! @ # _ - ) 1글자 이상
  - 최소 6글자 최대 20글자

3. 새 비밀번호, 새 비밀번호 확인이 같은지 체크
  = 오타방지

4. 이름X, 반복X, 연속X, 생일X 등등...
 */

// 비밀번호 변경

// 비밀번호 변경 form태그
const changePw = document.querySelector("#changePw");

// 비밀번호 "변경하기" 버튼 클릭시 또는 내부 엔터 입력 시
// == submit (제출)
changePw?.addEventListener("submit", e => {

  // e.preventDefault(); // 기본 이벤트 막기
  // form의 기본 이벤트인 제출 막기
  // 유효성 검사 조건이 만족되지 않을 때 수행할꺼임

  // 입력요소 모두 얻어오기
  const currentPw    = document.querySelector("#currentPw");
  const newPw        = document.querySelector("#newPw");
  const newPwConfirm = document.querySelector("#newPwConfirm");

  // 현재 비밀번호,  새 비밀번호,  새 비밀번호 확인
  // 3가지 입력여부 체크
  let str; // undefined 상태
  if( newPwConfirm.value.trim().length == 0 ){
    str = "새 비밀번호 확인을 입력해 주세요";
  }
  if( newPw.value.trim().length == 0 ){
    str = "새 비밀번호를 입력해 주세요";
  }
  if( currentPw.value.trim().length == 0 ){
    str = "현재 비밀번호를 입력해 주세요";
  }

  if( str !== undefined ){ // 입력되지 않은 값이 존재할 때
    messageDiv.innerText = str;
    popupLayer.classList.remove("display-hidden");
    e.preventDefault();
    return;
  }

  // 2. 새 비밀번호가 알맞은 형태로 작성 되었는가 확인
  // - 영어(대소문자 가리지 X) 1글자 이상
  // - 숫자 1글자 이상
  // - 특수문자 (! @ # _ -) 1글자 이상
  // - 최소 6글자 최대 20글자

  /* 정규 표현식(regEx) */
  // https://developer.mozilla.org/ko/docs/Web/JavaScript/Guide/Regular_expressions

  // - 문자열에서 특정 문자 조합을 찾기 위한 패턴

  // ex) 숫자가 3개 이상 작성된 문자열 조합 찾기
  // "12abc" -> X
  // "444"   -> O
  // "1q2w3e" -> O

    /* [JS 정규 표현식 객체 생성 방법]
  1. /정규표현식/
  2. new RegExp("정규표현식");
  */

  const lengthCheck = newPw.value.length >= 6 && newPw.value.length <= 20;
  const letterCheck = /[a-zA-Z]/.test(newPw.value); // 영어 알파벳 포함
  const numberCheck = /\d/.test(newPw.value); // 숫자 포함
  const specialCharCheck = /[!@#_-]/.test(newPw.value); // 특수문자 포함

  // 조건이 하나라도 만족하지 못하면
  if ( !( lengthCheck && letterCheck && numberCheck && specialCharCheck ) ) {
    messageDiv.innerText = "영어,숫자,특수문자 1글자 이상, 6~20글자 사이로 입력해 주세요";
    popupLayer.classList.remove("display-hidden");
    e.preventDefault();
    return;
  }

  // 3. 새 비밀번호, 새 비밀번호 확인이 같은지 체크

  if( newPw.value !== newPwConfirm.value ){
    messageDiv.innerText = "새 비밀번호가 일치하지 않습니다";
    popupLayer.classList.remove("display-hidden");
    e.preventDefault();
    return;
  }

});


/********************************************************************************* */
/********************************************************************************* */


/* JS에서 전화번호를 입력받아 유효성 검사하는 코드 생성
- 010으로 시작
- 11글자
- "-" 기호 제외
*/

const memberTel = document.querySelector("#memberTel");
memberTel?.addEventListener("input", e => {

  // 입력된 전화번호
  const inputTel = memberTel.value.trim();

  // 1) 전화번호 정규식 검사
  const phoneRegex = /^010\d{8}$/;

  // 입력받은 전화번호가 유효한 형식이 아닌 경우
  if( !phoneRegex.test(inputTel) ){
    memberTel.classList.add("red");
    memberTel.classList.remove("green");
    checkObj.memberTel = false;
    return;
  }

  // 유효한 경우
  memberTel.classList.add("green");
  memberTel.classList.remove("red");
  checkObj.memberTel = true;

});


/********************************************************************************* */
/********************************************************************************* */


/* 내정보 form 제출 시 */
const updateInfo = document.querySelector("#updateInfo");

updateInfo?.addEventListener("submit", e => {

  e.preventDefault();
  // checkObj에 작성된 값 검사하기
  // -> 닉네임, 전화번호 유효한지 검사

  // for ~ in 구문 : JS객체의 key값을 하나씩 접근하는 반복문

  for ( let key in checkObj ){

    // 닉네임, 전화번호 중 유효하지 않은 값이 있을 경우
    if( checkObj[key] === false ){

      let str=" 유효하지 않습니다."

      switch(key){
        case "memberNickname" : str = "닉네임이" + str; break;
        case "memberTel"      : str = "전화번호가" + str; break;
      }

      // 000이 유효하지 않습니다.
      messageDiv.innerText = str;
      popupLayer.classList.remove("display-hidden");
      
      document.getElementById(key).focus();
      return;
    }
  
  }
  
  /* 주소 유효성검사 */
  // 모두작성 또는 모두 미작성
  // const postcode = document.querySelector("#postcode").value.trim();
  // const address = document.querySelector("#address").value.trim();
  // const detailAddress = document.querySelector("#detailAddress").value.trim();
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
    messageDiv.innerText = "주소가 유효하지 않습니다.";
    popupLayer.classList.remove("display-hidden");
    return;
  }
  
  updateInfo.submit();
});


/********************************************************************************* */
/********************************************************************************* */


/* 회원탈퇴 유효성 검사 */
const secession = document.querySelector("#secession");

secession?.addEventListener("submit", async e => {
  e.preventDefault();

  // 1) 비밀번호 입력 확인
  const memberPw = document.querySelector("#memberPw")
  if(memberPw.value.trim().length === 0){
    messageDiv.innerText = "비밀번호를 입력해 주세요.";
    popupLayer.classList.remove("display-hidden");
    return;
  }

  // 2) 체크 되었는지 검사
  const agree = document.querySelector("#agree");

  if(agree.checked === false){
    messageDiv.innerText = "탈퇴를 원하시면 동의를 체크해 주세요.";
    popupLayer.classList.remove("display-hidden");
    return;
  }

  // 3) confirm을 이용해서 탈퇴할건지 확인
  const result = await showConfirmPopup("정말 탈퇴 하시겠습니까?");
  if(result){
    secession.submit();
  } else {
    messageDiv.innerText = "탈퇴취소";
    popupLayer.classList.remove("display-hidden");
    return;
  }
});


/********************************************************************************* */
/********************************************************************************* */


/* 프로필 이미지 미리보기, 삭제하기 */

// 프로필 이미지 업로드 상태에 따라서 어떤상태인지 구분하는 값
// -1 : 프로필 이미지를 바꾼적이 없음(초기상태)
//  0 : 프로필 이미지를 삭제( X 버튼을 클릭 )
//  1 : 새 이미지를 선택
let statusCheck = -1;


let lastValidFile = null; // 마지막으로 선택한 파일을 저장할 변수

// 프로필 보기가 출력될 img
const profileImg = document.querySelector("#profileImg");

// 프로필 이미지를 선택할 input
const imageInput = document.querySelector("#imageInput");

// 기본 이미지로 변경할 x버튼
const deleteImage = document.querySelector("#deleteImage");

if(imageInput != null){// 프로필 변경 화면인 경우

  
  /* *** 미리보기 함수 *** */
  // file : input type="file"에서 선택된 파일
  const updatePreview = (file) => {

    // 프로필 이미지 상태 구분용 변수 수정
    statusCheck = 1;

    lastValidFile = file; // 선택된 파일 임시저장

    // JS에서 제공하는 파일을 읽어오는 객체
    const reader = new FileReader();

    // 파일을 읽어오는데 DataURL 형식으로 읽어옴
    // DataURL : 파일 전체 "데이터"가 브라우저가 해석할 수 있는 긴 주소형태의 "문자열"로 변환이 됨
    reader.readAsDataURL(file);

    // 선택된 파일이 다 익식되었을때
    reader.addEventListener("load", e => {

      profileImg.src=e.target.result;
      // 파일리더의 결과( (주소처럼생긴)문자열 )의 결과(주소)를 참조주소값에 대입

    })
  }

  /* input type="file" 태그가 선택한 값이 변한 경우 수행 */
  imageInput.addEventListener("change", e => {

    const file = e.target.files[0];

    // 선택된 파일이 없을 경우
    if(file === undefined){

      // 이전 선택한 파일 유지하는 코드를 가져오시겠다
      //-> 이전 선택한 파일을 저장할 전역변수 (lastVaildFile) 선언

      // 이전에 선택한 파일이 없는 경우
      // == 현재 페이지에 들어와서 프로필 이미지를 바꾼적이 없는경우
      if(lastValidFile === null) return;

      // 이전에 선택한 파일이 있을경우
      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(lastValidFile);
      // -> lastVaildFile을 요소로 포함한 FileList가 생성이 됨
      // dataTransfer가 가지고 있는 files 필드에 lastVaildFile 추가
      
      // input의 files 변수에 lastVaildFile이 추가된 files 대입
      imageInput.files = dataTransfer.files;

      // 이전 선택된 파일로 미리보기 되돌리기(없어도 되긴 함)
      updatePreview(lastValidFile);

      // 선택된 파일이 없으면 돌아가고, 있으면 미리보기함수를 호출
      return;
    }

    // 미리보기 함수 호출
    updatePreview(file);

  })


  /* X버튼 클릭시 기본 이미지로 변환 */

  deleteImage.addEventListener("click", ()=>{
    
    // 미리보기를 기본이미지로 변경
    // updatePreview(userDefaultImage);
    profileImg.src=userDefaultImage;

    // input태그와 마지막 선택된 파일을 저장하는 lastValidFile에 저장된 값을 모두 삭제
    imageInput.value = '';
    lastValidFile = null;

    // 프로필 이미지 상태 구분용 변수 수정
    statusCheck = 0;
  })

}

/* 프로필 화면에서 변경하기 버튼이 클릭된 경우 */
const profileForm = document.querySelector("#profile");
profileForm?.addEventListener("submit", e => {

  let flag = true; // true인 경우 제출 불가능

  // 미변경 시 제출 불가
  if(statusCheck === -1) flag = true;

  // 기존 프로필 이미지 X -> 새 이미지 선택
  if(loginMemberProfileImg === null && statusCheck === 1) flag = false;
  
  // 기존 프로필 이미지 O -> X버튼을 눌러 삭제
  if(loginMemberProfileImg !== null && statusCheck === 0) flag = false;
  
  // 기존 프로필 이미지 O -> 새 이미지 선택
  if(loginMemberProfileImg !== null && statusCheck === 1) flag = false;



  if(flag) {
    e.preventDefault;
    messageDiv.innerText = "이미지 변경 후 클릭하세요";
    popupLayer.classList.remove("display-hidden");
  }


})