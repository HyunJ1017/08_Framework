
// 탈퇴 페이지
const withdrawal = document.querySelector("#withdrawal");






withdrawal?.addEventListener("submit", e=>{

/*   // 비밀번호 입력 확인 
  const memberPw = document.querySelector("#memberPw");
  if(memberPw.value.trim().length === 0){// 미입력시 
    alert("비밀번호를 입력해 주세요");
    e.preventDefault();
    return;
  } */

  //2) 체크 되었는지 검사 
  const agree = document.querySelector("#agree");

  if(agree.checked === false){// 체크가 되어있지 않은 경우 
    alert("탈퇴를 원하시면 동의를 체크해 주세요");
    e.preventDefault();
    return;
  }






  //3) confirm을 이용해서 탈퇴할건지 확인
  if(confirm("탈퇴하시겠습니까??") === false){// 취소 클릭시 
    alert("취소되었습니다");
    e.preventDefault();
    return;


  }

});

