/* 선택된 이미지 미리보기 관련 요소 모두 얻어오기 */
const previewList     = document.getElementsByClassName("preview");
const inputImageList  = document.getElementsByClassName("inputImage");
const deleteImageList = document.getElementsByClassName("delete-image");

//const previewList = document.querySelectorAll(".preview");

/* [querySelector(), querySelectorAll()의 문제점]
- 호출되었을 시점의 요소 형태 그대로를 얻어옴

[getElementsByClassName()]
- 요소를 얻어와서 실시간으로 변화되는 상태를 계속 추적함
*/


// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];
// const lastValidFiles = [null]; => 이렇게 작성해도 문제없음

/** 미리보기 함수
 * @param {*} file : <input type="file"> 에서 선택된 파일
 * @param {*} order : 이미지 순서
 */
const updatePreview = (file, order) => {

  // 선택된 파일이 지정된 크기를 초과한 경우 선택 막기
  const maxSize = 1024 * 1024 * 10; // 10MB를 byte 단위로 작성
  
  if(file.size > maxSize){ // 파일 크기 초과시
    messageDiv.innerText = "10MB 이하의 이미지만 선택해 주세요";
    popupLayer.classList.remove("display-hidden");

    // 미리보기는 return해서 멈추는데
    // input에 아직 파일이 남아있음
    if(lastValidFiles[order] === null){
      inputImageList[order].value = ''; // 선택파일삭제
      return;
    }
    
    // 이전 선택된 파일이 있을때
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(lastValidFiles[order]);
    inputImageList[order].files = dataTransfer.files;

    return;
  }

  // 선택된 이미지 백업
  lastValidFiles[order] = file;

  // JS에서 제공하는 파일을 읽어오는 객체
  const reader = new FileReader();

  // 파일을 읽어오는데 DataURL 형식으로 읽어옴
  // DataURL : 파일 전체 "데이터"가 브라우저가 해석할 수 있는 긴 주소형태의 "문자열"로 변환이 됨
  reader.readAsDataURL(file);

  reader.addEventListener("load", e => {
    previewList[order].src = e.target.result;
    // e.target.result == 파일이 변환된 주소 형태 문자열
  })

};

// -------------------------------------------------------

/* input태그, x버튼에 이벤트 리스너 추가 */
for(let i = 0 ; i < inputImageList.length ; i++ ){

  // input 태그에 이미지 선택시 미리보기 함수 호출
  inputImageList[i].addEventListener("change", e => {

    const file = e.target.files[0];

    if(file === undefined) {
      // 선택취소하여 input태그가 비어버린 경우
      
      // 이전에 선택한 파일이 없는 경우
      if(lastValidFiles[i] === null) return;

      // 이전에 선택한 파일이 있을경우
      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(lastValidFiles[i]);
      // -> lastVaildFile을 요소로 포함한 FileList가 생성이 됨
      // dataTransfer가 가지고 있는 files 필드에 lastVaildFile 추가
      
      // input의 files 변수에 lastVaildFile이 추가된 files 대입
      inputImageList[i].files = dataTransfer.files;

      // 이전 선택된 파일로 미리보기 되돌리기(없어도 되긴 함)
      updatePreview(lastValidFiles[i], i);

      // 선택된 파일이 없으면 돌아가고, 있으면 미리보기함수를 호출
      return;

    }

    updatePreview(file, i);

  });

  /* x버튼 클릭시 미리보기, 산택된 파일 제거 */
  deleteImageList[i].addEventListener("click", () => {

    previewList[i].src      = '';   // 미리보기 제거
    inputImageList[i].value = '';   // 선택된 파일 삭제
    lastValidFiles[i]       = null; // 백업 초기화

  });

} // for end

// -----------------------------------------------------

/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardWriteFrm");
form.addEventListener("submit", e => {
  const boardTitle = document.querySelector("[name='boardTitle']");
  const boardContnet = document.querySelector("[name='boardContent']");

  if(boardTitle.value.trim().length === 0){
    messageDiv.innerText = "제목을 작성해 주세요";
    popupLayer.classList.remove("display-hidden");
    boardTitle.focus();
    e.preventDefault();
    return;
  }
  if(boardContnet.value.trim().length === 0){
    messageDiv.innerText = "내용을 작성해 주세요";
    popupLayer.classList.remove("display-hidden");
    boardContnet.focus();
    e.preventDefault();
    return;
  }
})