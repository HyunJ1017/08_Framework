/* ============= 유효성 검사(Validation) ============= */

// 입력 값이 유효한 형태인지 표시하는 객체(체크리스트)
const checkObj = {
  "memberNickname" : true
}


/* 닉네임 검사 */
// - 3글자 이상
// - 중복 X
const memberNickname = document.querySelector("#memberNickname");


// 객체?.속성 --> ? : 안전 탐색 연산자
// - 객체가 null 또는 undefined가 아니면 수행

// 기존 닉네임
const originalNickname = memberNickname?.value;

memberNickname?.addEventListener("input", () => {
  // input이벤트 : 입력과 관련된 모든 동작 (JS를 이용한 값세팅 제외)

  // 입력된 값 얻어오기(양쪽 공백 제거)
  const inputValue = memberNickname.value.trim();

  if(inputValue.length < 3){ // 3글자 미만

    // 클래스 제거
    memberNickname.classList.remove("green");
    memberNickname.classList.remove("red");

    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;
    return;
  }


  // 입력된 닉네임이 기존 닉네임과 같을 경우
  if(originalNickname === inputValue){
     // 클래스 제거
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


  // 조건이 하나라도 false인 경우
  if( (lengthCheck && validCharactersCheck) === false ){
    memberNickname.classList.remove("green");
    memberNickname.classList.add("red");

    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;
    return;
  }


  // 비동기로 입력된 닉네임
  // DB에 존재하는지 확인하는 Ajax 코드(fetch() API) 작성

  // get방식 요청 (쿼리스트링으로 파라미터 전달)
  fetch("/myPage/checkNickname?input=" + inputValue)
  .then(response => {
    if(response.ok){ // 응답 상태코드 200(성공)인 경우
      return response.text(); // 응답 결과를 text형태로 변환 
    }

    throw new Error("중복 검사 실패 : " + response.status);
  })

  .then(result => {
    // result == 첫 번째 then에서 return된 값

    if(result > 0){ // 중복인 경우
      memberNickname.classList.add("red");
      memberNickname.classList.remove("green");
      checkObj.memberNickname = false; // 체크리스트에 false 기록
      return;
    }

    // 중복이 아닌 경우
    memberNickname.classList.add("green");
    memberNickname.classList.remove("red");
    checkObj.memberNickname = true; // 체크리스트에 true 기록

  })
  .catch(err => console.error(err));
});

let statusCheck = -1;


let lastVaildFile = null; // 마지막으로 선택된 파일을 저장할 변수

// 미리보기가 출력될 img
const profileImg = document.querySelector("#profileImg");

// 프로필 이미지를 선택할 input
const imageInput = document.querySelector("#imageInput");

// 기본 이미지로 변경할 x버튼
const deleteImage = document.querySelector("#deleteImage");


if(imageInput != null){ // 프로필 변경 화면인 경우
  
  /** 미리 보기 함수 
   * @param file : input type="file"에서 선택된 파일
  */
 const updatePreview = (file) => {
   
    lastVaildFile = file; // 선택된 파일을 lastVaildFile 복사
   
    // JS에서 제공하는 파읽을 읽어오는 객체
    const reader = new FileReader();

    // 파일을 읽어 오는데
    // DataURL 형식으로 읽어옴
    // DataURL: 파일 전체 데이터가 브라우저가 해석할 수 있는
    //          긴 주소형태 문자열로 변환
    reader.readAsDataURL(file);


    // 선택된 파일이 다 인식 되었을 때
    reader.addEventListener("load", e => {

      profileImg.src = e.target.result;
      // e.target.result == 파일이 변환된 주소 형태 문자열
     
      statusCheck = 1;
    })
  }


  /* input type="file" 태그가 선택한 값이 변한 경우 수행 */
  imageInput.addEventListener("change", e => {

    // 선택된 파일 1개를 얻어옴
    const file = e.target.files[0];

    // 선택된 파일이 없을 경우
    if(file === undefined){
      
      /* 이전 선택한 파일 유지하는 코드 */
      // -> 이전 선택한 파일을 저장할 전역 변수(lastVaildFile) 선언

      // 이전에 선택한 파일이 없는 경우
      // == 현재 페이지 들어와서 프로필 이미지 바꾼적이 없는 경우
      if(lastVaildFile === null) return;

      // 이전에 선택한 파일이 있을 경우
      const dataTransfer = new DataTransfer();

      dataTransfer.items.add(lastVaildFile);
      // DataTranfer가 가지고 있는 files 필드에 
      // lastVaildFile 추가
      //-> lastVaildFile을 요소로 포함한 FileList 생성됨

      //input의 files 변수에 lastVvaildFile이 추가된 files 대입
     imageInput.files = dataTransfer.files;

     
     // 이전 선택된 파일로 미리보기 되돌리기
     updatePreview(lastVaildFile);


      return;
    }

    // 선택된 파일이 있을 경우
    updatePreview(file); // 미리보기 함수 호출
  })


  

}

const profileForm = document.querySelector("#profile");

profileForm?.addEventListener("submit", e => {

  let flag = true;// true인 경우 제출 불가능

  // 미변경 시 제출 불가
  if(statusCheck === -1) flag =true;

  // 기존 프로필 이미지 X -> 새 이미지 선택 
  if(loginMemberProfileImg === null && statusCheck === 1) flag = false; 

  // 기존 프로필 이미지  O -> X버튼 눌러 삭제
  if(loginMemberProfileImg !== null && statusCheck === 0) flag =false;

  // 기존 프로필 이미지 O -> 새이미지
  if(loginMemberProfileImg !== null && statusCheck === 1) flag =false;

  
  if(flag === true){
    e.preventDefault();
    alert("이미지 변경 후 클릭하세요");
  }

})


const textarea = document.getElementById("memberIm");

textarea.addEventListener("input", () => {
  const maxCols = 20; // 가로 글자 수 제한
  const maxRows = 5; // 세로 줄 수 제한

  // 텍스트를 줄별로 나누기
  let lines = textarea.value.split("\n");

  // 각 줄에서 가로 글자 수 초과 시 자동 줄바꿈 추가
  lines = lines.flatMap(line => {
    const wrappedLines = [];
    while (line.length > maxCols) {
      wrappedLines.push(line.slice(0, maxCols));
      line = line.slice(maxCols);
    }
    wrappedLines.push(line);
    return wrappedLines;
  });

  // 줄 수가 초과할 경우 제한하고 경고 메시지 띄우기
  if (lines.length > maxRows) {
    alert("5 줄을 초과할 수 없습니다.");
    lines = lines.slice(0, maxRows); // 최대 줄 수까지만 유지
  }

  // 텍스트 박스에 다시 설정
  textarea.value = lines.join("\n");
});
