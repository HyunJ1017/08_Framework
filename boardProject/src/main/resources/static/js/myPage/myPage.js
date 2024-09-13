
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
const findAddressBtn = document.querySelector("#findAddressBtn");
findAddressBtn.addEventListener("click", findAddress);
// => 함수명만 작성하는 경우 : 함수반환

// => 함수명() 작성하는 경우 : 함수에 정의된 내용을 실행


/********************************************************************************* */

// 입력값이 유효한 형태인지 표시하는 객체(체크리스트)
const checkObj = {
  "memberNickname" : true
}

/****** 닉네임 유효성 검사(Validation) ******/

/* 닉네임검사 */
// - 3글자 이상
// - 중복X
const memberNickname = document.querySelector("#memberNickname");

memberNickname.addEventListener("input", e=>{
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
