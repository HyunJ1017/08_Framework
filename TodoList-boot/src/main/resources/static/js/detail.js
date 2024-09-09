const goToList    = document.querySelector("#goToList");
const completeBtn = document.querySelector("#completeBtn");
const updateBtn   = document.querySelector("#updateBtn");
const deleteBtn   = document.querySelector("#deleteBtn");
// const listNo      = ( document.querySelector("#listNo") ).value;
// 주소에서 값 얻어오기
const pathname = location.pathname;
const listNo = pathname.substring( pathname.lastIndexOf('/') + 1 );


// 목록으로 버튼이 클릭된 경우
goToList.addEventListener("click", () => {
  location.href = "/";

});

// 삭제 버튼 클릭시
deleteBtn.addEventListener("click", () => {
  // 1. 정말 삭제할 것인지 confirm()을 이용해서 확인
  if( !confirm("정말 삭제 하시겠습니까?") ){
    // 취소 클릭시
    return;

  }

  // 2. confirm() 확인 클릭시
  //   /todo/delete/인덱스번호 GET 방식으로 요청 보내기
  location.href = "/todo/delete/" + listNo;

});


// 수정버튼 클릭시 수정할 수 있는 화면을 요청
updateBtn.addEventListener("click", () => {
  // GET 방식 요청
  location.href = "/todo/update/" + listNo;

});

/*******************  과제버튼  ******************* */

const insertSub = document.querySelector("#insertSub");
const insertAnswer = document.querySelector("#insertAnswer");

insertSub.addEventListener("click", ()=>{
  insertAnswer.innerHTML=
        '<h4>과제 추가</h4>'+
        '<form action="/todo/addSub" method="post" id="addForm">'+
        '<div>'+
        '제목 : <input type="text" name="subjectTitle"> '+
        '</div>'+
        '<div>'+
        '<textarea name="subjectDetail" rows="3" cols="50" placeholder="상세 내용"></textarea>'+
        '</div>'+
        '<input type="hidden" name="listNo" value =' + listNo + '>'+
        '<button>추가</button>'+
        '<button type="button" id="closeSubAdd">닫기</button>'+
        '</form>' ;

  const closeSubAdd = document.querySelector("#closeSubAdd");

  closeSubAdd.addEventListener("click", () => {
    insertAnswer.innerHTML = '';
  });

  const addForm = document.querySelector("#addForm");
  const title = document.querySelector("[name=subjectTitle]");
  const detail = document.querySelector("[name=subjectDetail]");
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

  if(input2.length > 1000){ //내용이 한계치보다 초과되어 입력되었을때
    e.preventDefault(); // form 제출 이벤트 막기
    alert("현재 글자수 : " + input2.length + "\n1000자 이하만 작성이 가능합니다");
    detail.focus();
  }

})
});
