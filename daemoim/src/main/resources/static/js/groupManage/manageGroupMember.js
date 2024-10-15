const params = new URLSearchParams(location.search);
let cp    = 1;
let order = 1;
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
