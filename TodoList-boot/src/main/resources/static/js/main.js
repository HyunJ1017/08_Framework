/* 제목이 작성되지 않은경우 #addForm의 제출 막기*/

const addForm = document.querySelector("#addForm");
const title = document.querySelector("[name=todoTitle]");
const detail = document.querySelector("[name=todoDetail]");

// addForm이 제출될려고 할때
addForm.addEventListener("submit", e=>{
  // e: 이벤트객체

  // 제목 입력값 가져와서 양쪽 입력값 제외
  const input = title.value.trim();

  if(input.length === 0){ //제목이 입력되지 않았을때
    e.preventDefault(); // form 제출 이벤트 막기
    alert("제목을 입력 해 주세요");
    title.focus();
    return;
  }

  const input2 = detail.value;
  if(input2.length === 0){ //내용이 입력되지 않았을때
    e.preventDefault(); // form 제출 이벤트 막기
    alert("내용을 입력 해 주세요");
    detail.focus();
  }
  if(input2.length > 1000){ //내용이 한계치보다 초과되어 입력되었을때
    e.preventDefault(); // form 제출 이벤트 막기
    alert("현재 글자수 : " + input2.length + "\n1000자 이하만 작성이 가능합니다");
    detail.focus();
  }

})

/* 완료여부로 정렬하기 */
const completeOrder = document.querySelector("#completeOrder");

completeOrder.addEventListener("click", () => {
  const order = document.querySelector("[name=order]");
  location.href = "/todo/orderby/" + order.value;
});



/* 버튼 */

const detailBtn = document.querySelectorAll(".detailBtn");
const detailBtnAnswer = document.querySelectorAll(".detailBtnAnswer");
const todoDetail = document.querySelectorAll(".todoDetail");
const detailHref = document.querySelectorAll(".detailHref");

for( let i = 0 ; i < detailBtn.length ; i++){

  detailBtn[i].addEventListener("click", ()=>{
    if(detailBtn[i].innerText == 'v'){
      detailBtn[i].innerText = '접기';
      detailBtnAnswer[i].innerHTML = '<td colspan="4" >' + todoDetail[i].value + '</td><td><button class="goBtn">go</button></td>';
      const goBtn = document.querySelector(".goBtn");
      goBtn.addEventListener("click", ()=>{
        location.href = detailHref[i].value;
      });

    } else {
      detailBtn[i].innerText = 'v';
      detailBtnAnswer[i].innerHTML = '';
    }
  })
}