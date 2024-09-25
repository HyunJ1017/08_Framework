// 쿠키에 저장된 여러 값 중 key가 일치하는 value 반환하기
function getCookie(key){

  // 1. cookie 전부 얻어오기 ( String )
  const cookies = document.cookie; // "K=V;K=V;K=V;K=V;..." // console.log(cookies);
  
  // 2. "/"을 구분자 삼아서 배열 형태로 쪼개기(split)
  const arr = cookies.split("/"); // [K=V,  K=V, K=V...]

  // 3. 쪼개진 배열 요소를 하나씩 꺼내서 JS객체에 K:V 형태로 추가
  const cookieObj = {};

  for(let entry of arr){
    // entry = 'K=V'
    const temp = entry.split("=");
    cookieObj[temp[0]] = temp[1];

  }

  return cookieObj[key];
}


document.addEventListener("DOMContentLoaded", () => {

  const saveEmail = getCookie("saveEmail"); // 쿠키에 저장된 Email 얻어오기 // console.log(saveEmail);

  // 저장된 이메일이 없는 경우
  if(saveEmail == undefined) return;

  const memberEmail = document.querySelector("#loginForm input[name=memberEmail]");

  const checkbox = document.querySelector("#loginForm input[name=saveEmail]");

  // 로그인이 되어있는 상태면 종료
  if(memberEmail == null) return;

  // 이메일 입력란에 저장된 이메일을 출력
  memberEmail.value = saveEmail;
  checkbox.checked = true;
});


//=================================================================================
//=================================================================================


/* 메인페이지 회원 목록 비동기 조회 함수 */

// 표를 출력 할 Tbody 태그
const memberList = document.querySelector("#memberList");

const selectMemberList = () => {
  
  // 1 비동기로 모든 회원의 회원번호, 이메일, 탈퇴상태 조회하기
  fetch("/selectMemberList")
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("멤버조회오류" + response.status)
  })
  .then(list => {
    // console.log(memberList);

    // 1.) #MemberList 기존 내용 없애기
    memberList.innerHTML = '';

    // 2.) 조회 결과인 list를 반복접근해서 html코드 넣기
    list.forEach( member => {
      // member = 조회된 list에서 하나씩 꺼낸 요소

      // tr요소 만들기
      const tr = document.createElement("tr");

      // th에 회원 번호 세팅
      const th1 = document.createElement("th");
      th1.innerText = member.memberNo;

      // td에 회원 이메일 세팅
      const td2 = document.createElement("th");
      td2.innerText = member.memberEmail;

      // th에 회원 탈퇴여부 세팅
      const th3 = document.createElement("th");
      th3.innerText = member.memberDelFl;

      // th에 버튼 든들고 login 글자 세팅
      const th4 = document.createElement("th");
      const loginBtn = document.createElement("button");
      loginBtn.innerText = '로그인';
      th4.append(loginBtn);

      // th > button 만들어서 "비밀번호 초기화" 글자 세팅
      const th5 = document.createElement("th");
      const initBtn = document.createElement("button");
      initBtn.innerText = "비밀번호 초기화";
      th5.append(initBtn);

      // th > button 만들어서 "탈퇴 상태 변경" 글자 세팅
      const th6 = document.createElement("th");
      const changeBtn = document.createElement("button");
      initBtn.innerText = "탈퇴 상태 변경";
      th6.append(changeBtn);

      // tr에 요소추가
      tr.append(th1, td2, th3, th4, th5, th6);

      // #memberList에 tr추가
      memberList.append(tr);

    });


  })
  .catch(err => console.error(err));

};