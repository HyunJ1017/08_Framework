const params = new URLSearchParams(location.search);
let cp    = 1;
let order = 0;
if(params.get("cp") !== null){cp = params.get("cp")};
if(params.get("order") !== null){order = params.get("order")};

/* 페이지네이션 내부의 a태그들 클릭시 해당 페이지로 이동 */
const paginations = document.querySelectorAll(".pagination a");
paginations?.forEach( (e) => {
  e.addEventListener("click", btn => {
    btn.preventDefault();
    // console.log(btn.target.innerText);
    // console.log("cp : " + cp);
    // console.log("order : " + order);

    const i = btn.target.innerText;

    switch(i){
      case '<'  : cp = pagination.prevPage; break;
      case '<<' : cp = 1; break;
      case '>>' : cp = pagination.maxPage; break;
      case '>'  : cp = pagination.nextPage; break;
      default   : cp = i; break;
    }

    // console.log( cp + "페이지 이동 눌림");

    let url = location.pathname + `?cp=${cp}&order=${order}`;
    location.href = url;

  });
});


//------------------------------------------------------------------
//------------------------------------------------------------------


/* 오름차순 1아이디 2닉네임 3가입일 4탈퇴여부 5 벤
내림차순 -1아이디 -2닉네임 -3가입일 -4탈퇴여부 5벤 */

// 화면정렬 변경하기
const orderBtnArr = document.querySelectorAll(".orderBtn");
orderBtnArr.forEach( (orderBtn, index) => {
  orderBtn.addEventListener("click", ()=>{
    // console.log(index);
    const check = index + 1;

    if(order == check || order == check * -1){
      order = (order * (-1));
    } else {
      order = check;
    }

    // console.log("order : " + order);

    let url = location.pathname + `?cp=${cp}&order=${order}`;
    location.href = url;

  })
})

//----------------------------------------------------------------
/* 강퇴 버튼 */

const removeMemberArr = document.querySelectorAll(".removeMember");

removeMemberArr.forEach( (btn) => {
  btn.addEventListener("click", ()=>{

    const btnMemberNo = btn.dataset.memberNo;

    // alert(btnMemberNo + "번 멤버 탈퇴 클릭됨");
    if(confirm("해당 회원을 모임에서 강퇴 하시겠습니까?") == false) return;

    memberRemove(btnMemberNo);

  });
});

const memberRemove = (memberNo) => {

  fetch("/groupMember", {
    method : "DELETE",
    headers : {"Content-Type" : "application/json"},
    body : memberNo
  })
  .then( response => {
    if(response.ok) return response.text();
    throw new Error("멤버삭제 비동기통신 실패")
  })
  .then( result => {
    /*
     0 : 실패
     1 : 성공
     2 : GROUP_NO 오류
     3 : 모임장 불일치
    */
    switch(result){
      case '0' : alert("회원을 강퇴에 실패 하였습니다."); break;
      case '1' : alert("회원을 강퇴 시켰습니다."); break;
      case '2' : alert("모임번호를 불러오는데 실패하였습니다."); break;
      case '3' : alert("모임장만 강퇴할 수 있습니다."); location.href="/"; break;
      default : alert("알 수 없는 오류가 발생하였습니다.");
    }

    /* 화면 초기화 함수 만들기 ********************************************************/
  })
  .catch( err => console.error(err) );

};

//----------------------------------------------------------------
/* 권한위임 버튼 */

const delegateArr = document.querySelectorAll(".delegate");

delegateArr.forEach( (btn) => {
  btn.addEventListener("click", ()=>{
    
    if(confirm("모임장 권한을 이임하시겠습니까?") === false) return;

    // 클릭버튼에서 얻어온 회원번호
    const btnMemberNo = btn.dataset.memberNo;

    let memberNickname;

    // 위임하려는 회원정보 얻어오기
    fetch("/groupMember?memberNo=" + btnMemberNo)
    .then( response => {
      if(response.ok) return response.text();
      throw new Error("멤버조회 비동기통신 실패")
    })
    .then( result => {

      memberNickname = result;

      if(confirm( "[ " + memberNickname + " ] 회원에게 모임장의 권한을 양도하는것이 맞습니까?") === false) return;
      
      // body태그에 form태그 만들어서 제출
      const form = document.createElement("form");
      form.action = "/groupMemberManage/changeLeader";
      form.method = "POST";
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "nextLeader";
      input.value = btnMemberNo;
      form.append(input);
      document.querySelector("body").append(form);
      form.submit();

    })
    .catch( err => console.error(err) );


  });
});