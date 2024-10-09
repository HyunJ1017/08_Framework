
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
const labelArr = document.querySelectorAll(".categoryLabel");
labelArr.forEach(e=>{
  e.addEventListener("click", ()=>{
    getCategoryList(checkedCategory());
  })
});