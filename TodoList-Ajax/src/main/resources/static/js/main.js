/* 할 일 추가 관련 요소를 얻어와 변수에 저장 */
const addBtn     = document.querySelector("#addBtn");
const todoTitle  = document.querySelector("#todoTitle");
const todoDetail = document.querySelector("#todoDetail");

// 추가버튼 (#addBtn) 클릭시
addBtn.addEventListener("click", () => {

  // 클릭된 순간 화면에 작성되어있는 제목, 내용 얻어오기
  const title  = todoTitle.value;
  const detail = todoDetail.value;

  // Ajax (insert는) POST 방식으로 요청하기 위한 fetch() API 코드 작성

  // HTTP 통신 시
  // - haeders : 요청관련정보(어떤데이터, 어디서 요청 ...)
  // - body : POST/PUT/DELETE 요청 시 전달할 데이터
  
  fetch("/todo/add", { // 지정된 주소로 비동기 요청(ajax)
    method : "POST", // 데이터 전달 방식을 POST로 지정
    headers: {"Content-Type": "application/json"}, // 요청 데이터의 형식을 JSON으로 지정
    body : JSON.stringify( {"todoTitle":title, "todoDetail":detail} ) // JS객체를 JSON 형태로 변환하여 Body에 추가
    })
    .then(response => response.text() ) // 요청에 대한 응답 객체(response)를 필요한 형태로 파싱
    // controller 반환값을 text 형태로 변환해서 다음 then 매개변수로 반환함

    .then(result => {
      console.log("result : ", result)

      // 비동기통신 응답결과가 1(삽입성공)인 경우
      if(result > 0){
        alert("할 일 추가 성공");
        
        // 추가하려고 작성한 값 화면에서 지우기
        todoTitle.value = '';
        todoDetail.value = '';

        // ***** 비동기로 전체할일갯수를 조회해 화면에 반영하는 함수
        getTotalCount();

        // ***** 비동기로 테이블 출력 최신화하기
        selectTodoList();
        
      } else {
        alert("할 일 추가 실패....");
        
      }

    }); // 첫 번째 then에서 파싱한 데이터를 이용한 동작 작성

});


/******************************************************************************************** */

/* 전체 Todo갯수 비동기 Ajax를 이용하여 조회하기 */

// 함수
function getTotalCount(){
  
  // fetch() API 작성 ( fetch : 가져오다 )
  
  /* GET방식 fetch */
  fetch("/todo/totalCount") // 비동기 요청 수행 -> Promise 객체 반환
  .then(response => {
    console.log(response); // 작동 확인
    
    // 비동기 요청에 대한 응답에 문제가 없을경우
    // if(response.status === 200)
    if(response.ok){
      return response.text();
    }
    
    // 예외 강제발생시
    throw new Error("비동기_통신_실패")
  })
  
  // 첫 번째 then에서 반환된 값을 매개 변수에 저장한 후 수행되는 구문
  .then(totalCount => {
    console.log(totalCount); // 작동확인
    
    // #totalCount 요소에 내용으로 비동기통신 결과를 대입
    document.querySelector("#totalCount").innerText = totalCount;
  })
  
  // 첫 번째 then에서 던져진 error를 잡앗 처리하는 구문
  .catch(e => console.error(e));
}

/******************************************************************************************** */

/* 완료된 Todo갯수 비동기 Ajax를 이용하여 조회하기 */

// 함수
function completeCount(){
  
  /* 첫 번째 then
  - 비동기 요청의 결과(응답)에 따라 어떤 코드를 수행할지 제어
  - 매개변수 response : 
  응답 결과, HTTP 상태 코드, 요청 주소 등이 담겨 있는 객체
  
  * 두번째 then
  - 비동기 요청으로 얻어온 값을 이용해서 수행될 js코드를 작성하는 구문
  */
 
 fetch("/todo/completeCount") // 비동기 요청해서 결과 데이터 응답 받아오겠다
 .then(response => {
   
   if(response.ok){  // 비동기통신 성공시
    return response.text(); // response에서 응답결과를 text형태로 변환해서 반환, 2번째 then의 매개변수로 사용
  }
  
  // 요청 실패시 에러(예외) 던지기
  throw new Error("완료된 할 일 개수 조회 실패")
  
})

.then(count => {
  console.log("완료된 할 일 갯수 : " + count);
  
  document.querySelector("#completeCount").innerText = count;
  
})

.catch( e => {console.log(e)} );

}

/******************************************************************************************** */

/* 비동기로 할일 목록 전체를 조회하기 */

function selectTodoList(){
  
  fetch("/todo/todoList")
  .then(response => {
    if(response.ok){
      return response.text();
    }
    throw new Error("목록조회 실패 : " + response.status)
  })
  .then( result => {
    //JSON(List 형태) -> JS 객체배열
    const todoList = JSON.parse(result);
    console.log(todoList);
    console.log("todoList 타입 : " + typeof todoList); // => object
    
    // 조회한 list를 #tbody에 요소추가
    
    const tbody = document.querySelector("#tbody");
    tbody.innerHTML = '';
    
    // JS 향상된 for문 1/4 : for(요소 of 배열){}
    for( let todo of todoList){
      
      // 1) todoNo가 들어갈 th테그
      const todoNo = document.createElement("div");
      todoNo.classList.add("block", "listNo");
      todoNo.innerText = todo.listNo;
      
      // 2) todoTitle이 들어갈 td, a 요소 생성
      const todoTitle = document.createElement("div");
      todoTitle.classList.add("block", "todoTitle");
      const a = document.createElement("a");
      a.innerText = todo.todoTitle;
      a.href = `/todo/detail/${todo.listNo}`;
      a.style.color = todo.color;
      todoTitle.append(a);
      
      // a요소가 클릭되었을때
      a.addEventListener("click", e => {
        // e : 이벤트객체
        // e.preventDefault() : 요소 기본 이벤트 막기
        e.preventDefault();
        
        // 할일 상세조회 비동기요청
        // e.target.herf : 클릭된 a태그의 href 불러오기
        selectTodo(e.target.href);
      });
      
      // 3) 완료여부
      const todoComplete = document.createElement("div");
      todoComplete.classList.add("block", "todoComplete");
      todoComplete.innerText = todo.complete;
      
      // 4) tr을 만들어 1,2,3, 에서 만든 요소 자식으로 추가
      const div = document.createElement("div");
      div.classList.add("flexRow", "tbodyRow");
      div.append( todoNo, todoTitle, todoComplete )

      // 6) tbody에 tr 추가
      tbody.append(div);
      
    }
    
  })
  .catch( e => console.log(e));
  
}

/******************************************************************************************** */

/* 페이지 로딩이 완료된 후 a태그 클릭 막기 */
document.addEventListener("DOMContentLoaded", () => {
  
  document.querySelectorAll("#listTable a").forEach( (a) => {
    // a : 반복마다 하나씩 꺼내져서 저장되는 변수
    a.addEventListener("click", e => {
      e.preventDefault();
      console.log("Detaile화면열기");
      selectTodo(e.target.href);
    })
  })

});


/******************************************************************************************** */

/******************************************************************************************** */
/* 

 > a 태그 클릭
 > selectTodo()호출
   > 팝업레이어 보이게하고
   > 내용,제목에 데이터셋
     > 서브목록불러오기 호출
       > 완료여부바꾸기 버튼기능 만들기 호출
       > 업데이트만들기 함수 호출
  
*/
/**
 * 비동기로 할 일 상세 조회하여 팝업 레이어에 출력하기
 * @param url : /todo/detail/10 형태
*/
function selectTodo(url){
  // console.log("이벤트 시작");

  fetch(url)
  .then(response => {
    if(response.ok) { // 요청응답 성공시

      //return response.json();
      // -> response.text() + JSON.parse(response|result) 합친 메서드
      // console.log("응답 정상");
      return response.json();
    }

    throw new Error("상세조회실패, " + response.status)
  })
  .then( todo => {
    console.log(todo);

    /* 팝업 레이어에 조회된 todo 내용 추가하기 */
        /* 팝업 레이어에 조회된 todo 내용 추가하기 */
    const popupTodoNo = document.querySelector("#popupTodoNo");
    const popupTodoTitle = document.querySelector("#popupTodoTitle");
    const popupComplete = document.querySelector("#popupComplete");
    const popupRegDate = document.querySelector("#popupRegDate");
    const popupTodoContent = document.querySelector("#popupTodoContent");
    const popupColor = document.querySelector("#popupColor");

    popupTodoNo.innerText = todo.listNo;
    popupTodoTitle.innerText = todo.todoTitle;
    popupTodoTitle.dataset.listNo = todo.listNo;
    popupComplete.innerText = todo.complete;
    popupRegDate.innerText = todo.regDate;
    popupTodoContent.innerText = todo.todoDetail;
    popupTodoContent.dataset.listNo = todo.listNo;
    popupColor.value = todo.color;
    
    // 팝업 레이어 보이게 하기
    // popup-hidden 클래스는 안보이게 해둠
    // 클래스중 popup-hidden 을 제거
    document.querySelector("#popupLayer").classList.remove("popup-hidden");
    
    // 서브리스트목록 호출
    selectSub(todo.listNo);

  })
  .catch( err => console.log(err));

  // clickHrSubCheck = 'off';
  // document.querySelectorAll(".subListViewer").forEach(e => e.remove());
  
  // popupLayer.style.width='510px';
  // subSection.style.width='0';
  // subSection.classList.add('popup-hidden');
}


/******************************************************************************************** */

// 팝업레이어 닫기버튼 클릭시 만들기
document.querySelector("#popupClose").addEventListener("click", ()=>{
  document.querySelector("#popupLayer").classList.add("popup-hidden");
})

/******************************************************************************************** */

// 완료 여부 변경 버튼 클릭시
const changeComplete = document.querySelector("#changeComplete");
changeComplete.addEventListener("click", ()=>{

  // 팝업 레이어에 작성된 todoNo, todoComplete 값 얻어오기
  const listNo = document.querySelector("#popupTodoNo").innerText;
  
  // 비동기로 완료여부 변경
  /* ajax(비동기) 요청시 사용 가능하 ㄴ데이터 전달 방식
  (REST API와 관련됨) 
  
  GET    : 조회(SELECT)
  POST   : 삽입 (INSERT)
  PUT    : 수정 (UPDATE)
  DELETE : 삭제 (DELETE)
  */

  fetch("/todo/completeChange", {
   method : "PUT",
   headers : {"Content-Type" : "application/json"},
   body : listNo
  })
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("완료여부변경실패" + response.status)
  })
  .then(result => {
    console.log(result);

    // 정상 시행시 완료여부 값 반대로 변경
    const complete = document.querySelector("#popupComplete");
    complete.innerText = complete.innerText == 'O' ? 'X' : 'O';

    // 완료된 할일 갯수를 비동기로 초기화하는 함수 호출
    completeCount();

    // 할일 목록을 비동기로 초기화하는 함수 호출
    selectTodoList();
  })
  .catch(err=>console.error(err));
  
});

/******************************************************************************************** */

/* 삭제 하기 */
/*
버튼누르면
물어보고
지우고
창닫고
목록, 전체갯수, 완료갯수 최신화해주고
*/
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn.addEventListener("click", ()=>{

  if(!confirm("삭제할까요?")) return;

  // 삭제할 할일 번호 얻어오기
  const listNo = document.querySelector("#popupTodoNo").innerText;

  // 비동기로 삭제요청하기
  fetch("/todo/deleteTodo", {
    method : "DELETE",
    headers : {"Content-Type" : "application/json"},
    body : listNo
  })
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("삭제실패 : " + response.status)
  })
  .then(result => {
    // 팝업 닫기
    document.querySelector("#popupLayer").classList.add("popup-hidden");
    // 전체목록 수, 완료갯수, 전체목록 재조회
    getTotalCount();
    completeCount();
    selectTodoList();
  })
  .catch(err => console.log(err));

  
});



/******************************************************************************************** */
/* 서브화면출력함수 */
function selectSub(listNo){
  /* 기존에 있던 내용 지우기 */
  document.querySelectorAll(".subListViewer").forEach(e => e.remove());
  
  fetch("/sub/selectSub/" + listNo)
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("불러오기 실패 " + response.status);
  })
  .then(subList => {
    // console.log(subList);
    
    /* 반복출력 */
    for(let sub of subList){
      const article1 = document.createElement("article");
      article1.classList.add("flexRow", "subListViewer");
      article1.innerHTML =
          '<div class="block subjectNum">' + sub.subjectNo + '</div>'
        + '<div class="block subjectTitle canUpdate2" data-subject-no=' + sub.subjectNo + '>' + sub.subjectTitle + '</div>'
        + '<div class="block subjectComplete" data-subject-no="' + sub.subjectNo + '">' + sub.complete + '</div>';
        
      const article2 = document.createElement("article");
      article2.classList.add("flexRow", "subListViewer");
      if(sub.subjectDetail === null){
        article2.innerHTML = 
        '<div class="subjectDetail canUpdate2" data-subject-no=' + sub.subjectNo + '>' + '> ' + '</div>';
      } else {
        article2.innerHTML =
        '<div class="subjectDetail canUpdate2" data-subject-no=' + sub.subjectNo + '> ' + sub.subjectDetail + '</div>';
      }
        
      subSection.append( article1, article2 );
      document.querySelectorAll(".subjectTitle").forEach(e=> e.addEventListener("click", makeUpdateDiv1 ));
      document.querySelectorAll(".subjectDetail").forEach(e=> e.addEventListener("click", makeUpdateDiv2 ));
    }
    subCompleteChange();
  })
  .catch( err => console.log(err) );

    
};

/******************************************************************************************** */
/* 서브리스트 추가하기 */
const addBtn2 = document.querySelector("#addBtn2");

addBtn2.addEventListener("click", () => {
  
  const listNo = document.querySelector("#popupTodoNo").innerText;
  const subjectTitle = document.querySelector("#subjectTitle").value;
  const subjectDetail = document.querySelector("#subjectDetail").value;
  
  console.log(listNo + ', ' + subjectTitle + ', ' + subjectDetail);
  
  fetch("/sub/insert", {
    method : "POST",
    headers: {"Content-Type": "application/json"},
    body : JSON.stringify( { "listNo":listNo, "subjectTitle":subjectTitle, "subjectDetail":subjectDetail} )
  })
  .then( response => {
    if(response.ok) return response.text();
    throw new Error("추가 통신실패" + response.status);
  })
  .then(result => {
    console.log(result);
    
    if(result > 0){
      document.querySelector("#subjectTitle").value = '';
      document.querySelector("#subjectDetail").value = '';
      selectSub(listNo);
    } else {
      alert("추가실패");
    }
  })
  .catch(err => console.log( err ));
});


/******************************************************************************************** */
/* 완료여부수정 */

function subCompleteChange(){
  document.querySelectorAll(".subjectComplete").forEach( c => {
    c.addEventListener("click", ()=>{
      // alert(c.dataset.subjectNo)
      const subNo = c.dataset.subjectNo;
      
      fetch("/sub/completeChange", {
        method : "PUT",
        headers : {"Content-Type" : "application/json"},
        body : subNo
      })
      .then(response => {
        if(response.ok) return response.json();
        throw new Error("완료여부변경실패" + response.status)
      })
      .then(result => {
        console.log(result);

        // 정상 시행시 완료여부 값 반대로 변경
        c.innerText = c.innerText == 'O' ? 'X' : 'O';
        
      })
      .catch(err=>console.error(err));

    } );
  });
}


/******************************************************************************************** */
/* 수정할수있는거 다 수정하기 */
/* 하나씩만 열꺼니까 페이지 재요청 후 수정창열기 */
/******************************************************************************************** */

/* todo 제목박스 수정 */
const popupTodoTitle = document.querySelector("#popupTodoTitle");
/* todo 글자색 수정 */
const popupColor = document.querySelector("#popupColor");
/* todo 세부내용 수정 */
const popupTodoContent = document.querySelector("#popupTodoContent");
/* sub 제목 수정 */
/* sub 내용 수정 */

/******************************************************************************************** */
/* todoTitle 클릭시 수정창 만들기 */
popupTodoTitle.addEventListener("click", popupTodoTitleEvent);

function popupTodoTitleEvent() {

  const title = popupTodoTitle.innerText;
  // const listNo = document.querySelector("#popupTodoNo").innerText;

  /* 창 열기 전 화면 새로고침 */
  // selectTodo("/todo/detail/" + listNo);

  /* 내용 바꾸고 */
  /* 내용에 기존 제목 넣고 */
  popupTodoTitle.innerHTML =
    '<div class="flexColumn">' +
    '<input class="flexGrow" id="title" value=' + title + '>' +
    '<div><button id="popupTodoTitleBtn">수정</button><button class="closeBtn">닫기</button></div>' +
    '</div>';

  /* 버튼에 이벤트 추가하고 */
  const popupTodoTitleBtn = document.querySelector("#popupTodoTitleBtn");
  popupTodoTitleBtn.removeEventListener("click", popupTodoTitleBtnEvent);
  popupTodoTitleBtn.addEventListener("click", popupTodoTitleBtnEvent);
  
  popupTodoTitleBtn.classList.remove("canUpdate");
  popupTodoTitle.removeEventListener("click", popupTodoTitleEvent);
}

/* todoTitle에 넣을 이벤트 */
function popupTodoTitleBtnEvent() {

  /* 누르면 수정할 항목 읽어오기*/
  const obj = {};
  obj.listNo = document.querySelector("#popupTodoNo").innerText;
  obj.todoTitle = document.querySelector("#title").value; 

  /* 비동기 요청 */
  fetch("/todo/updateTodo", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
    .then(response => {
      if (response.ok) return response.text();
      throw new Error("수정 실패: " + response.status);
    })
    .then(result => {
      console.log(result); // 1 | 0

      if (result > 0) { // 수정 성공
        alert("수정 성공");
      } else { // 실패
        alert("수정 실패");
      }

      // 현재 목록 최신화
      selectTodoList();
      // 디테일 화면 최신화
      selectTodo("/todo/detail/" + obj.listNo);
    })
    .catch(err => console.error(err));
}


/******************************************************************************************** */
/******************************************************************************************** */
/* TodoContent 클릭시 수정창 만들기 */
popupTodoContent.addEventListener("click", popupTodoContentEvent);

function popupTodoContentEvent() {

  const content = popupTodoContent.innerText;
  // const listNo = document.querySelector("#popupTodoNo").innerText;

  /* 창 열기 전 화면 새로고침 */
  // selectTodo("/todo/detail/" + listNo);

  /* 내용 바꾸고 */
  /* 내용에 기존 제목 넣고 */
  popupTodoContent.innerHTML =
    '<div class="flexColumn">' +
    '<textarea id="insertDetail" rows="3" cols="35">' + content + '</textarea>' +
    '<div><button id="popupTodoContentBtn">수정</button><button class="closeBtn">닫기</button></div>' +
    '</div>';

  /* 버튼에 이벤트 추가하고 */
  const popupTodoContentBtn = document.querySelector("#popupTodoContentBtn");
  popupTodoContentBtn.removeEventListener("click", popupTodoContentBtnEvent);
  popupTodoContentBtn.addEventListener("click", popupTodoContentBtnEvent);
  
  popupTodoContentBtn.classList.remove("canUpdate");
  popupTodoContent.removeEventListener("click", popupTodoContentEvent);
}

/* todoTitle에 넣을 이벤트 */
function popupTodoContentBtnEvent() {

  /* 누르면 수정할 항목 읽어오기*/
  const obj = {};
  obj.listNo = document.querySelector("#popupTodoNo").innerText;
  obj.todoDetail = document.querySelector("#insertDetail").value;
  alert(document.querySelector("#insertDetail").value);

  /* 비동기 요청 */
  fetch("/todo/updateTodo", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
    .then(response => {
      if (response.ok) return response.text();
      throw new Error("수정 실패: " + response.status);
    })
    .then(result => {
      console.log(result); // 1 | 0

      if (result > 0) { // 수정 성공
        alert("수정 성공");
      } else { // 실패
        alert("수정 실패");
      }

      // 현재 목록 최신화
      selectTodoList();
      // 디테일 화면 최신화
      selectTodo("/todo/detail/" + obj.listNo);
    })
    .catch(err => console.error(err));
}


/******************************************************************************************** */


function makeUpdateDiv1(e){
  const updateSubNo = document.querySelector("#updateSubNo");
  const updateValue = document.querySelector("#updateValue");
  const updateSubject = document.querySelector("#updateSubject");
  updateSubNo.innerText = e.target.dataset.subjectNo;
  updateValue.innerText = 'title';
  updateSubject.value = e.target.innerText;
}
function makeUpdateDiv2(e){
  const updateSubNo = document.querySelector("#updateSubNo");
  const updateValue = document.querySelector("#updateValue");
  const updateSubject = document.querySelector("#updateSubject");
  updateSubNo.innerText = e.target.dataset.subjectNo;
  updateValue.innerText = 'detaile';
  updateSubject.value = e.target.innerText;
  
}

const subupdateBtn  = document.querySelector("#subupdateBtn");
const updateSubNo   = document.querySelector("#updateSubNo");
const updateValue   = document.querySelector("#updateValue");
const updateSubject = document.querySelector("#updateSubject");

subupdateBtn.addEventListener("click", () => {
  if(document.querySelector("#updateSubNo").innerText == '' ) return;
  
  const obj = {};
  
  obj.updateSubNo = updateSubNo.innerText;
  obj.updateValue = updateValue.innerText;
  obj.updateSubject = updateSubject.value;
  console.log(obj);

  fetch("/sub/update", {
    method : "PUT",
    headers: {"Content-Type": "application/json"},
    body : JSON.stringify(obj)
  })
  .then( response => {
    if(response.ok) return response.text();
    throw new Error( "서브통신에러, " + response.status )
  })
  .then( result => {
    if(result > 0){
      alert("수정완료");
      updateSubNo.innerText = '';
      updateValue.innerText = '';
      updateSubject.value = '';
    } else {
      alert("유감...");
    }

  })
  .catch(err => console.log(err) );

});