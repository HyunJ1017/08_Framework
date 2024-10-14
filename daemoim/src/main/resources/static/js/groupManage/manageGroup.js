
/* 카테고리 변경 */
// 카테고리 변경버튼
const categoryBtn = document.querySelector("#categoryBtn");
const categoryListBtn = document.querySelector("#categoryListBtn");
// 카테고리
const categoryBefore = document.querySelector("#categoryBefore");
const categoryAfter = document.querySelector("#categoryAfter");
// 카테고리리스트
const categoryListBefore = document.querySelector("#categoryListBefore");
const categoryListAfter = document.querySelector("#categoryListAfter");
const CategoryListTr = document.querySelector("#CategoryListTr");


// 카테고리 변경화면 출력하기
categoryBtn.addEventListener("click", ()=>{

  getCategoryList(checkedCategory());

  // 화면 처음창 -> 수정창
  categoryBefore.classList.add("display-none");
  categoryListBefore.classList.add("display-none");

  categoryAfter.classList.remove("display-none");
  categoryListAfter.classList.remove("display-none");

});


// 카테고리 변경화면 출력하기2
categoryListBtn.addEventListener("click", ()=>{

  getCategoryList(checkedCategory());

  // 화면 처음창 -> 수정창
  categoryListBefore.classList.add("display-none");
  categoryListAfter.classList.remove("display-none");

});



// 체크된 카테고리 라디오의 값 얻어오기
const checkedCategory= () => {
  // name="categoryNo"인 라디오 버튼 중에서 체크된 버튼 선택
  const checkedRadio = document.querySelector('input[name="categoryNo"]:checked');
  
  // 체크된 버튼이 있으면 그 value 값을 반환, 없으면 null 반환
  if (checkedRadio) {
    return checkedRadio.value;
  } else {
    return null; // 아무 것도 체크되지 않은 경우
  }
}


// CategoryNo을 넘겨받아 비동기로 카테고리리스트의 화면을 최신화 하기
const getCategoryList = (categoryNo)=> {

  fetch("/groupManage/getCategoryList?categoryNo=" + categoryNo)
  .then(response => {
    if(response.ok)return response.json();
    throw new Error("카테고리 불러오기 오류")
  })
  .then(categoryList => {

    if(categoryList.length === 0) return;

    CategoryListTr.innerHTML = '';

    categoryList.forEach( e => {
      const div = document.createElement("div");
      const label = document.createElement("label");
      label.innerText = e.categoryListName;
      const input = document.createElement("input");
      input.type = 'radio';
      input.name = 'categoryListNo';
      input.value = e.categoryListNo;
      input.id = 'categoryList' + e.categoryListNo;
      input.classList.add("categoryListRadio");
      // input.addEventListener("click", ()=>{createConfirm.categoryList = true});
      if(e.categoryListNo === beforeCategoryListNo){
        input.checked = true;
      }
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

    // alert("e.value : " + e.target.value + ", e.value.type" + typeof Number( e.target.value ));

    getCategoryList( Number( e.target.value ) );
  })
});


/********************************************************************** */
/********************************************************************** */

/* [할일]

1. 모임명 유효성 검사
- 필수입력, 중복X, 500자, (한글, 알파벳, 숫자, 공백)만
2. 모임소개 유효성 검사
- 필수입력, 4000자
3. 이미지 유효성검사
- 10MB제한, 이미지 이름 50자 제한
4. 카테고리 유효성검사
- 필수입력(2개모두)
- 카테고리리스트 글자제한 30자

5. 제출전 확인사항
- 필수입력사항 모두 입력했는지
- 유효성검사 불합격 항목은 없는지


overflow: hidden; -> 텍스트가 너비를 넘기면 ... 처리
*/

//--------------------------------------------------------------

// 유효성검사항목
const detailConfirm = {
  "groupName"      : true ,
  "groupIntroduce" : true ,
  "category"       : false, // 제출할때 검사
  "categoryList"   : false  // 제출할때 검사
}



/********************************************************************** */
/********************************************************************** */


/* 모임이름
1. 모임명 유효성 검사
- 필수입력
- 중복X
- 500자
- 한글, 알파벳, 숫자, 공백 만 입력 가능
- 현재모임명과 동일한 경우 pass
*/

const groupName = document.querySelector("#groupName");
const nameMessage = document.querySelector("#groupNameMessage");
const countName = document.querySelector("#countName");


const nameMessageConfirm = {
  "nomal"    : "현재 모임에서 사용중인 모임명 입니다.",
  "shortage" : "3글자 이상의 이름을 사용해 주십시오.",
  "tooLong"  : "최대 500자까지 입력이 가능합니다.",
  "rules"    : "한글, 알파벳, 숫자, 띄어쓰기 만 입력 가능 합니다.",
  "overlap"  : "동일한 이름을 이미 사용중입니다.",
  "statusOk" : "사용 가능한 이름 입니다."
};

// 모임 이름이 입력될때마다 발생할 이벤트
groupName.addEventListener("input", () => {

  const inputName = groupName.value.trim();
  const inputLength = inputName.length;
  // console.log(inputName);

  // 글자수 표시
  countName.innerText = inputLength + '/500';

  // 3글자 미만일때
  if(inputLength < 3){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.shortage;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }

  // 500글자 넘을 때
  if(inputLength > 500){
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
  fetch("/groupManage/groupNameCheck?inputName=" + inputName)
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
      detailConfirm.groupName = false;
      return;
    }
  })
  .catch(error => console.error(error));
  
  // 사용 가능한 이름일 경우
  nameMessage.innerText = "";
  nameMessage.innerText = nameMessageConfirm.statusOk;
  countName.classList.remove("red");
  nameMessage.classList.remove("red");
  nameMessage.classList.add("green");
  detailConfirm.groupName = true;
  return;

});


/********************************************************************** */
/********************************************************************** */


/* 모임소개
2. 모임소개 유효성 검사
- 필수입력
- 4000자
*/

const groupIntroduce = document.querySelector("#groupIntroduce");
const countIntroduce = document.querySelector("#countIntroduce");
const groupIntroduceMessage = document.querySelector("#groupIntroduceMessage");

groupIntroduce.addEventListener("input", ()=>{

  const inputIntroduce = groupIntroduce.value.trim();
  const inputLength = inputIntroduce.length;

  if(inputLength < 1){
    detailConfirm.groupIntroduce = false;
    groupIntroduceMessage.classList.add("red");
    groupIntroduceMessage.innerText="필수 입력사항 입니다.";
    return;
  }

  if(inputLength > 4000){
    detailConfirm.groupIntroduce = false;
    groupIntroduceMessage.classList.add("red");
    groupIntroduceMessage.innerText="4000자 이내로 입력해 주세요.";
    countIntroduce.innerText = inputLength + '/4000';
    return;
  }

  detailConfirm.groupIntroduce = true;
  groupIntroduceMessage.classList.remove("red");
  groupIntroduceMessage.innerText="모임의 소개글을 입력해 주세요.";
  countIntroduce.innerText ='';

});



/********************************************************************** */
/********************************************************************** */


/* 이미지 입력
3. 이미지 유효성검사
- 10MB제한
- 이미지 이름 50자 제한

java가서만 따로따로 받으면되지 검사 자체는 뭉뚱그려서 검사해도 되지않나...
*/

// 이미지 인풋태그
const inputImageArr = document.getElementsByName("inputImg");

// 이미지 미리보기 창
const imgPreview = document.querySelectorAll(".inputImgPreview");

// 백업용 이미지
const lastImg = [null, null];


/* input에 이미지가 변한경우 */
for(let i=0; i < inputImageArr.length ; i++){

  inputImageArr[i].addEventListener("change", e=>{
    const file = e.target.files[0];
    
    if(file === undefined){
      if(lastImg[i] === null) return;

      const transfer = new DataTransfer();
      transfer.items.add(lastImg[i]);
      inputImageArr[i].files = transfer.files;

      return;
    }

    if(file.size > 1*1024*1024*1){
      alert("너무큰데");
      const transfer = new DataTransfer();
      transfer.items.add(lastImg[i]);
      inputImageArr[i].files = transfer.files;
      return;
    }

    imgPreviewFuntion(file, i); // 미리보기 함수 호출
  })

}

const imgPreviewFuntion = (file, order) => {

  lastImg[order] = file;
  
  // 입력받은 파일을 미리보기창에 url형태로 전달
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.addEventListener("load", e => {
    
    imgPreview[order].src=e.target.result;
    // 메인상단이미지는 미리보기 화면도 바꿈
    if(order === 1){
      imgPreview[2].src=e.target.result;
    }
  })
}