/* 기본 */

const createConfirm = {
  "groupName"      : false,
  "groupIntroduce" : false,
  "category"       : false,
  "groupLimitAge"  : true // 초기값이 '제한없음'
};


/********************************************************************** */
/********************************************************************** */


/* 모임이름 */

// 모임 이름 input 태그
const groupName = document.querySelector("#groupName");
// 모임 이름 유효성검사 메세지 표시
const nameMessage = document.querySelector("#groupNameMessage");
const nameMessageConfirm = {
  "nomal"    : "사용할 모임명을 입력 해 주세요",
  "shortage" : "3글자 이상의 이름을 사용해 주십시오.",
  "tooLong"  : "최대 500자까지 입력이 가능합니다.",
  "rules"    : "한글, 알파벳, 숫자, 띄어쓰기 만 입력 가능 합니다.",
  "overlap"  : "동일한 이름을 이미 사용중입니다.",
  "statusOk" : "사용 가능한 이름 입니다."
};

// 모임 이름이 입력될때마다 발생할 이벤트
groupName.addEventListener("input", () => {

  const inputName = groupName.value.trim();
  // console.log(inputName);

  // 입력한 글자가 없을때
  if(inputName.length == 0){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.nomal;
    nameMessage.classList.remove("red");
    nameMessage.classList.remove("green");
    createConfirm.groupName = false;
    return;
  }

  // 3글자 미만일때
  if(inputName.length < 3){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.shortage;
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    createConfirm.groupName = false;
    return;
  }

  // 500글자 넘을 때
  if(inputName.length > 500){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.tooLong;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }
  
    
  // 입력한거 확인
  const pattern = /^[가-힣a-zA-Z0-9\s]+$/;
  if(pattern.test(inputName) === false){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.rules;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }

  // 중복되는 이름일때
  fetch("groupNameCheck?inputName=" + inputName)
  .then(response=>{
    if(response.ok) return response.text();
    throw new Error("모임이름검사 오류 : " + response.sratus)
  })
  .then(result => {
    if(result > 0){
      // 중복되는 경우가 있는 경우
      nameMessage.innerText = "";
      nameMessage.innerText = nameMessageConfirm.overlap;
      nameMessage.classList.add("red");
      nameMessage.classList.remove("green");
      createConfirm.groupName = false;
      return;
    }
  })
  .catch(error => console.error(error));
  
  // 사용 가능한 이름일 경우
  nameMessage.innerText = "";
  nameMessage.innerText = nameMessageConfirm.statusOk;
  nameMessage.classList.remove("red");
  nameMessage.classList.add("green");
  createConfirm.groupName = true;
  return;

});


/********************************************************************** */
/********************************************************************** */


/* 모임소개 */

// 모임소개 택스트에어리어
const groupIntroduce = document.querySelector("#groupIntroduce");

/* 작성 여부만 확인 */
groupIntroduce.addEventListener("input", ()=>{

  const inputIntroduce = groupIntroduce.value.trim();

  if(inputIntroduce.length > 0){
    createConfirm.groupIntroduce = true;
    return;
  } else {
    createConfirm.groupIntroduce = false;
  }

});



/********************************************************************** */
/********************************************************************** */


/* 모임 이미지 */

// 미리보기 이미지 표시할 곳
const inputImgPreview = document.querySelector("#inputImg");
// 이미지 선택
const imgInput = document.querySelector("#imgInput");
// 이미지 선택시 선택파일의 이름을 표시
const imgName = document.querySelector("#imgName");

// 마지막으로 선택했던 이미지
let lastInputImg = null;

// 이미지를 선택하여 미리보기 함수를 호출할 경우
const inputPreview = (img) => {

  lastInputImg = img;

  // JS에서 제공하는 파일을 읽어오는 객체
  const reader = new FileReader();

  // 파일을 읽어오는데 DataURL 형식으로 읽어옴
  // DataURL : 파일 전체 "데이터"가 브라우저가 해석할 수 있는 긴 주소형태의 "문자열"로 변환이 됨
  reader.readAsDataURL(img);

  // 선택된 파일이 다 인식되었을때
  reader.addEventListener("load", e => {

    inputImgPreview.src=e.target.result;
    // 파일리더의 결과( (주소처럼생긴)문자열 )의 결과(주소)를 참조주소값에 대입

    imgName.innerText = img.name;
  })
};

// 이미지 input에 이미지가 선택, 취소 된 경우
imgInput.addEventListener("change", e => {
  const img = e.target.files[0];

  // 이미지 유효성 검사
  let flag = false;

  // 업로드한 이미지 크기 확인
  if(img?.size > 1024*1024*1){
    alertM("10MB 이하의 이미지를 선택해 주세요");
    flag = true;
  }

  // input 태그를 취소한 경우
  if(img === undefined){
    flag = true;
  }

  if(flag){

    // 선택한적 없이 취소한 경우
    if(lastInputImg === null) {
      returnImig(); // 입력이미지 삭제
      return;
    }

    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(lastInputImg);
    // -> lastVaildFile을 요소로 포함한 FileList가 생성이 됨
    // dataTransfer가 가지고 있는 files 필드에 lastVaildFile 추가
    
    // input의 files 변수에 lastVaildFile이 추가된 files 대입
    imgInput.files = dataTransfer.files;

    // 이전 선택된 파일로 미리보기 되돌리기(없어도 되긴 함)
    inputPreview(lastInputImg);

    // 선택된 파일이 없으면 돌아가고, 있으면 미리보기함수를 호출
    return;
  }

  // 미리보기 함수 호출
  inputPreview(img);
});

// 선택취소버튼 클릭시
const deleteImg = document.querySelector("#deleteImg");
deleteImg.addEventListener("click", () => {

  returnImig(); // 이미지 삭제함수
});

const returnImig = () => {
  // 미리보기를 기본이미지로 변경
  // inputImg : 전역변수선언 해둠
  // updatePreview(groupDefaultImage);
  inputImgPreview.src=groupDefaultImage; 

  // input태그와 마지막 선택된 파일을 저장하는 lastValidFile에 저장된 값을 모두 삭제
  imgInput.value = ''; // 기본이미지는 없으면 기본이 뜨니까 파일 저장해 줄 필요 없음
  lastInputImg = null;
}

/********************************************************************** */
/********************************************************************** */


/* 카테고리 */


const CategoryListTr = document.querySelector(".CategoryListTr");
const categoryText = document.querySelector(".categoryText");
// 카테고리 라디오버튼 아무거나 선택하면 true


// 체크된 카테고리 라디오의 값 얻어오기
const checkedCategory = () => {
  // name="categoryNo"인 라디오 버튼 중에서 체크된 버튼 선택
  const checkedRadio = document.querySelector('input[name="categoryNo"]:checked');
  
  // 체크된 버튼이 있으면 그 value 값을 반환, 없으면 null 반환
  if (checkedRadio) {
    return checkedRadio.value;
  } else {
    return null; // 아무 것도 체크되지 않은 경우
  }
}

// 체크된 카테고리 리스트 라디오의 값 얻어오기(제출전확인용)
const checkedCategoryList = () => {
  // name="categoryNo"인 라디오 버튼 중에서 체크된 버튼 선택
  const checkedRadio = document.querySelector('input[name="categoryListNo"]:checked');
  
  // 체크된 버튼이 있으면 그 value 값을 반환, 없으면 null 반환
  if (checkedRadio) {
    return checkedRadio.value;
  } else {
    return null; // 아무 것도 체크되지 않은 경우
  }
}

// CategoryNo을 넘겨받아 비동기로 카테고리리스트의 화면을 최신화 하기
const getCategoryList = (categoryNo)=> {

  fetch("getCategoryList?categoryNo=" + categoryNo)
  .then(response => {
    if(response.ok)return response.json();
    throw new Error("카테고리 불러오기 오류")
  })
  .then(categoryList => {

    if(categoryList.length === 0) return;

    categoryText.innerText = '작은 카테고리';
    CategoryListTr.innerHTML = '';

    categoryList.forEach( e => {
      const div = document.createElement("div");
      const label = document.createElement("label");
      label.innerText = e.categoryListName;
      label.classList.add("categoryLabel");
      const input = document.createElement("input");
      input.type = 'radio';
      input.name = 'categoryListNo';
      input.value = e.categoryListNo;
      input.id = 'categoryList' + e.categoryListNo;
      input.classList.add("categoryListRadio");
      //input.addEventListener("click", ()=>{createConfirm.categoryList = true});
      label.htmlFor = 'categoryList' + e.categoryListNo;
      div.append(input, label);
      CategoryListTr.append(div);
    })

  })
  .catch( err => console.error(err));
};

// 카테고리 변경시 카테고리 리스트 불러오기
const radioArr = document.querySelectorAll(".categoryRadio");
radioArr.forEach(e=>{
  e.addEventListener("change", e=>{

    // alertM("e.value : " + e.target.value + ", e.value.type" + typeof Number( e.target.value ));

    getCategoryList( Number( e.target.value ) );
    createConfirm.category = true;
  })
});

/********************************************************************** */
/********************************************************************** */


/* 가입제한 */
/* 
// 나이 체크버튼
const limitAgeCheck = document.querySelector("#limit-age");
const limitAgeSec = document.querySelector("#limit-ageSec");
// 두 입력태그
const floorAgeInput = document.querySelector('[name="floorAge"]');
const roofAgeInput  = document.querySelector('[name="roofAge"]');
// 두 텍스트 태그 (회색 <=> 검정)
const floorAgeText  = document.querySelector("#floorAgeText");
const roofAgeText  = document.querySelector("#roofAgeText");
// 나이입력 유효성결과 결과메세지창 (검정 <=> 빨강)
const limitAgeMessage  = document.querySelector("#limit-ageMessage");
const ageMessages = {
  "nomal"     : "최소나이와 최대나이 중 필요한 나이를 입력 하세요",
  "floorOver" : "최소나이가 최대나이보다 크게 입력되었습니다.",
  "roofOver"  : "최대나이가 최소나이보다 작게 입력되었습니다."
}
// 성별 체크버튼
const limitGenderCheck = document.querySelector("#limit-gender");
const limitGenderSec = document.querySelector("#limit-genderSec");
const genderRadios = document.querySelectorAll('input[name="gender"]');

// 나이제한 체크시 나이제한 입력창 보여주기
limitAgeCheck.addEventListener("click", () => {
  
  if( limitAgeCheck.checked ){
    limitAgeSec.classList.remove("display-none");
    createConfirm.groupLimitAge = false;
    floorAgeInput.value = '';
    floorAgeText.classList.add("grey");
    roofAgeInput.value = '';
    roofAgeInput.classList.add("grey");
    limitAgeMessage.innerText = ageMessages.nomal;
    limitAgeMessage.classList.remove("red");
  } else {
    limitAgeSec.classList.add("display-none")
    createConfirm.groupLimitAge = true;
  }
  
});


limitGenderCheck.addEventListener("click", () => {
  
  if( limitGenderCheck.checked ){
    limitGenderSec.classList.remove("display-none");
  } else {
    limitGenderSec.classList.add("display-none");
    
    // 라디오 버튼을 "모두"로 초기화
    genderRadios[2].checked = false;
    genderRadios[1].checked = false;
    genderRadios[0].checked = true;
  }
  
});

// 성별제한 체크시 성별선택창 보여주기

// 나이제한 입력시

공통 :
입력시 '세 이상', '세 이하' 글씨가 검정색으로 변하게
지우게되면 다시 회색으로

최소나이에 입력시
1. 최소나이가 최대나이보다 크면 오류
2. 최소나이에 입력한 값이 없을때 최대나이도 없다면 오류

최대나이에 입력시
1. 최대나이가 최소나이보다 작으면 오류
2. 최대나이에 입력한 값이 없을때 최소나이도 없다면 오류
//


// 최소나이 입력시
floorAgeInput.addEventListener("input", ()=>{

  // 최소나이를 지웠을때
  if(floorAgeInput.value.length === 0){
    
    // 최소나이에 입력한 값이 없을때 최대나이도 없다면 오류
    if(roofAgeInput.value.length === 0){
      createConfirm.groupLimitAge = false;
      limitAgeMessage.innerText = ageMessages.nomal;
      limitAgeMessage.classList.add("red");
      return;
    } else {
      // '세 이상' 글씨를 다시 회색으로
      floorAgeText.classList.add("grey");
      // 최소>최대나이여서 오류중이었는데 지우면 정상화 되야함
      createConfirm.groupLimitAge = true;
      limitAgeMessage.innerText = ageMessages.nomal;
      limitAgeMessage.classList.remove("red");
      return;
    }
  } else {
    // 입력한거면 '세 이상' 글씨를 검은색으로, 이후에 유효성검사 계속
    floorAgeText.classList.remove("grey");
  }

  const roof = Number(roofAgeInput.value);
  const floor = Number(floorAgeInput.value);
  // 최대나이에 입력한 값이 있다면 최소나이보다 큰가
  if(roofAgeInput.value.length !== 0 ){
    // 최소나이가 더 크다면
    if(floor > roof){
      createConfirm.groupLimitAge = false;
      limitAgeMessage.innerText = ageMessages.floorOver;
      limitAgeMessage.classList.add("red");
      return;
    }
  }
  
  // 정상일경우
  floorAgeText.classList.remove("grey");
  createConfirm.groupLimitAge = true;
  limitAgeMessage.innerText = ageMessages.nomal;
  limitAgeMessage.classList.remove("red");
  return;
  
});

// 최대나이 입력시
roofAgeInput.addEventListener("input", ()=>{

  
  // 최대나이를 지웠을때
  if(roofAgeInput.value.length === 0){
    
    // 최대나이에 입력한 값이 없을때 최소나이도 없다면 오류
    if(floorAgeInput.value.length === 0){
      createConfirm.groupLimitAge = false;
      limitAgeMessage.innerText = ageMessages.nomal;
      limitAgeMessage.classList.add("red");
      return;
    } else {
      // '세 이하' 글씨를 다시 회색으로
      roofAgeText.classList.add("grey");
      // 최대<최소나이여서 오류중이었는데 지우면 정상화 되야함
      createConfirm.groupLimitAge = true;
      limitAgeMessage.innerText = ageMessages.nomal;
      limitAgeMessage.classList.remove("red");
      return;
    }
  } else {
    // 입력한거면 '세 이하' 글씨를 검은색으로, 이후에 유효성검사 계속
    roofAgeText.classList.remove("grey");
  }
  
  const roof = Number(roofAgeInput.value);
  const floor = Number(floorAgeInput.value);
  // 최소나이에 입력한 값이 있다면 최대나이보다 작은가
  if(floorAgeInput.value.length !== 0 ){
    // 최소나이가 더 크다면
    if(floor > roof){
      createConfirm.groupLimitAge = false;
      limitAgeMessage.innerText = ageMessages.roofOver;
      limitAgeMessage.classList.add("red");
      return;
    }
  }
  
  // 정상일경우
  roofAgeText.classList.remove("grey");
  createConfirm.groupLimitAge = true;
  limitAgeMessage.innerText = ageMessages.nomal;
  limitAgeMessage.classList.remove("red");
  return;
  
});

 */
/********************************************************************** */
/********************************************************************** */


/* 제출될 때 */

// 제출 될 form태그
const submitGroupCreate = document.querySelector("#submitGroupCreate");

// 제출할 때 유효성검사 확인
submitGroupCreate.addEventListener("submit", e => {
  
  // 제출 막음
  //e.preventDefault();

  if(!createConfirm.groupName ){
    alertM("모임명이 잘못 입력되었습니다.");
    e.preventDefault();
    return;
  }
  if(!createConfirm.groupIntroduce ){
    alertM("모임 소개글을 입력해 주세요.");
    e.preventDefault();
    return;
  }
  if(!createConfirm.category ){
    alertM("카테고리를 선택해 주셔야 합니다.");
    e.preventDefault();
    return;
  }
  if(checkedCategoryList() === null ){
    alertM("세부카테고리를 선택해 주셔야 합니다.");
    e.preventDefault();
    return;
  }
/*   if(!createConfirm.groupLimitAge ){
    alertM("옳바른 나이제한을 입력해 주세요.");
    e.preventDefault();
    return;
  } */

  alertM("모임이 생성되었습니다.");

});

/********************************************************************** */
/********************************************************************** */

document.querySelector("#comeback").addEventListener("click", () => {
  location.href = "/";
})