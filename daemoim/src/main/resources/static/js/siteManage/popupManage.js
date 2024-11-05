

// 이미지 입력시
// input태그
const popupImage = document.getElementById("popupImage");
const popupLeft = document.getElementById("popupLeft");
const popupTop = document.getElementById("popupUp");
const inputImgPreview = document.querySelector(".inputImgPreview");

// 백업용 이미지
let lastImg = null;

// 이미지 크기 확인용
let imgWidthCheck = 0;
let imgHeightCheck = 0;


/* input에 이미지가 변한경우 */
popupImage?.addEventListener("change", e=>{
  
  const file = popupImage.files[0];

  if(file === undefined){
    if(lastImg === null) return;
    backupLoad(i);
    return;
  }

  if(file.size > 1*1024*1024*1){
    alert("파일크기가 10MB를 초과합니다");
    backupLoad(i);
    return;
  }

  imgPreviewFunction(file);

}) // inputImage event end

const backupLoad = () => {
  const transfer = new DataTransfer();
  if(lastImg == null) return;
  transfer.items.add(lastImg);
  popupImage.files = transfer.files;
};

const imgPreviewFunction = (file) => {

  lastImg = file;
  
  // 입력받은 파일을 미리보기창에 url형태로 전달
  const reader = new FileReader();
  reader.readAsDataURL(file);

  reader.addEventListener("load", e => {
    
    inputImgPreview.style.top = (popupTop.value * 7.81) + 'px';
    inputImgPreview.style.left = (popupLeft.value * 16) + 'px';
    inputImgPreview.src=e.target.result;
    inputImgPreview.classList.remove("display-none");
    imgWidthCheck = inputImgPreview.naturalWidth;
    imgHeightCheck = inputImgPreview.naturalHeight;
  });
  
}; // imgPreviewFuntion() end

popupLeft?.addEventListener("change", ()=>{
  const left = popupLeft.value;
  inputImgPreview.style.left = (left * 16) + 'px';
})
popupTop?.addEventListener("change", ()=>{
  const top = popupTop.value;
  inputImgPreview.style.top = (top * 7.81) + 'px';
})

/********************************************************** */

// 선택 삭제버튼 이벤트 추가
const addPopupBtns = () => {
  const selectPopupArr = document.querySelectorAll(".selectPopup");
  const deletePopupArr = document.querySelectorAll(".deletePopup");
  selectPopupArr.forEach(btn => {
    const popupNo = btn.dataset.popupNo;
    btn.addEventListener("click", ()=>{
      selectPopup(popupNo);
    });
  })

  deletePopupArr.forEach(btn => {
    const popupNo = btn.dataset.popupNo;
    btn.addEventListener("click", ()=>{
      deletePopup(popupNo);
    });
  })
};


// 팝업 선택
const selectPopup = (popupNo) => {
  fetch("/popup/selectPopup?popupNo=" + popupNo)
  .then(response=>{
    if(response.ok) return response.json();
    throw new Error("팝업선택오류")
  })
  .then(popup => {
    // 결과 null이면 종료
    if(popup === null ) return;
    
    // 전달받은 이미지 표시
    const inputImgPreview = document.getElementById("inputImgPreview");
    inputImgPreview.src = popup.popupLocation + popup.popupRename;
    
    // 이미지가 로드된 후 창 크기 변경
    inputImgPreview.onload = function() {

      // const popupView = document.querySelector(".popup-view");

      // 이미지 크기를 비례하여 창 크기 설정
      inputImgPreview.naturalWidth = inputImgPreview.naturalWidth * 0.6
      inputImgPreview.naturalHeight = inputImgPreview.naturalHeight * 0.6;
      // popupView.style.width = `${popupImage.naturalWidth}px`;
      // popupView.style.height = `${popupImage.naturalHeight}px`;

      // 창 위치설정
      inputImgPreview.style.top = (popup.popupUp * 7.81) + 'px';
      inputImgPreview.style.left = (popup.popupLeft * 16) + 'px';

      inputImgPreview.classList.remove("display-none");

    }

  })
  .catch(err => {console.log(err)});
};


// 팝업 삭제
const deletePopup = (popupNo) => {
  fetch("/popup/deletePopup?popupNo=" + popupNo)
  .then(response=>{
    if(response.ok) return response.json();
    throw new Error("팝업삭제오류")
  })
  .then(popupList => {

    const popupListTr = document.querySelector("#popupListTr");
    popupListTr.innerHTML = "";

    if(popupList.length < 1){
      const tr = document.createElement("tr");
      const td = document.createElement("td");
      td.innerText = '현재 팝업 목록이 없습니다.';
      td.colspan = '5';
      tr.append(td);
      popupListTr.append(tr);
      return;
    }

    popupList.forEach(popup => {
      const tr = document.createElement("tr");
      const td1 = document.createElement("td");
      td1.innerText = popup.popupName;
      const td2 = document.createElement("td");
      td2.innerText = popup.popupLeft;
      const td3 = document.createElement("td");
      td3.innerText = popup.popupUp;
      const td4 = document.createElement("td");
      const button4 = document.createElement("button");
      button4.dataset.popupNo = popup.popupNo;
      button4.classList.add("selectPopup");
      button4.innerText = '선택';
      td4.append(button4);
      const td5 = document.createElement("td");
      const button5 = document.createElement("button");
      button5.dataset.popupNo = popup.popupNo;
      button5.classList.add("deletePopup");
      button5.innerText = '삭제';
      td5.append(button5);
      tr.append(td1, td2, td3, td4, td5);
      popupListTr.append(tr);
    })

    addPopupBtns();

  })
  .catch(err => {console.log(err)});
};
















/********************************************************** */
document.addEventListener("DOMContentLoaded", ()=>{
  // form 제출 막기
  const inputForm = document.querySelector("#inputForm");
  inputForm.addEventListener("submit", e=>{

    if(popupImage.files[0] === undefined) e.preventDefault();

    if(((popupLeft.value * 16) + imgWidthCheck) > 1600){
      e.preventDefault();
      alert("이미지가 너무 오른쪽에 붙어있습니다.");
    }

    if(((popupTop.value * 7.81) + imgHeightCheck) > 781){
      e.preventDefault();
      alert("이미지가 너무 바닥에 붙어있습니다.");
    }

  })

  // 버튼에 이벤트 추가
  addPopupBtns();
})