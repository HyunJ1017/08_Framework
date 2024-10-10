/*

이미지 미리보기

1) 이미지가 있음
-> BOARD_IMG 테이블 UPDATE

2) 기존에 이미지가 없음
-> BOARD_IMG 테이블 INSERT

3) 기본에 이미지가 있는데 그대로 둠(수정X)
-> file이 "선택된 파일 없음" 제출
-> 기존 이미지가 유지되도록 설정

4) 기존에 이미지가 있는데 X 버튼 눌러서 삭제
-> file이 "선택된 파일 없음" 제출
-> DB에서 해당 이미지 삭제(DELETE)

*/

// 기존에 존재하던 이미지의 순서(order)를 기록할 배열
const orderList = [];

// X 버튼이 눌러져 삭제되는 이미지의
// 순서(order)를 기록하는 Set
const deleteOrderList = new Set();
// Set : 중복된 값을 저장 못하게 하는 객체(Java Set 똑같음)
// Set을 사용하는 이유 : 
// X를 누를때마다 삭제된 번호를 Set에 넣을건데 하나만 저장하기 위해서

// input type="file" 태그들
const inputImageList  = document.getElementsByClassName("inputImage");
// X버튼들
const deleteImageList = document.getElementsByClassName("delete-image");
// 마지막으로 선택된 파일을 저장할 배열
const lastValidFiles = [null, null, null, null, null];


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
  // DataURL : 파일 전체 "데이터"가 브라우저가 해석할 수 있는 긴 주소형태의 "문자열"
  reader.readAsDataURL(file);

  reader.addEventListener("load", e => {
    previewList[order].src = e.target.result;
    // e.target.result == 파일이 변환된 주소 형태 문자열

    // 이미지가 성공적으로 읽어진 경우
    // deleteOderList 에서 해당 이미지 순서를 삭제
    // =>왜? 이전에 x버튼을 눌러 삭제 기록이 있을수도 있기 때문에
    deleteOrderList.delete(order);
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

    // 기존에 i번째 이미지가 있는 상태에서 X버튼이 눌러 졌을 때
    if(orderList.includes(i)){
      // 삭제목록에 추가(DELETE 수행)
      deleteOrderList.add(i);
      // orderList 에 없는(원래없던)걸 삭제해도
      // deleteOrderList에는 추가되지 않음
    }

  });

} // for end



// -----------------------------------------------------

/* 제목, 내용 미작성 시 제출 불가 */
const form = document.querySelector("#boardUpdateFrm");
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

  // 제출 전에 form태그 내부에 deleteOrderList를 담아줘야함
  const input = document.querySelector("input");
  input.type = 'hidden';
  input.name = 'deleteOrderList';
  // Set => Array로 변경하여 input에 추가
  // 배열을 input value에 대입하면 자동으로 배열.toString() 호출
  // -> [1,2,3] => "1,2,3" 변환
  input.value = Array.from(deleteOrderList);

  form.append(input);
})
/*

> Array.from(deleteOrderList)
= (3) [2, 4, 0]

> Array.from(deleteOrderList).toString()
= '2,4,0'

> typeof Array.from(deleteOrderList).toString()
= 'string'

*/