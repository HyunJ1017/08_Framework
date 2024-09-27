
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

        // 만약 탈퇴상태인경우 로그인 버튼 비활성화
        if( member.memberDelFl === 'Y' ){
          loginBtn.disabled = true;
        } else {
          // 탈퇴 상태가 아닌경우
          // 만들어진 로그인 버튼에 클릭 이벤트 추가
          loginBtn.addEventListener("click", () => {
            // 포스트 방식 로그인 요청
            // body 태그 마지막에 폼태그를 추가해 제출하는 형식으로 코드 작성
            // 왜? POST 방식 요청을 하고 싶기 때문에(POST는 form, ajax만 가능)
            const form = document.createElement("form");
            const input = document.createElement("input");
            form.append(input);
            document.querySelector("body").append(form);
            form.action = '/directLogin';
            form.method = 'POST';
            input.type = "hidden";
            input.name = "memberNo";
            input.value = member.memberNo;
            form.submit();
          });
        }

      // th > button 만들어서 "비밀번호 초기화" 글자 세팅
      const th5 = document.createElement("th");
      const initBtn = document.createElement("button");
      initBtn.innerText = "비밀번호 초기화";
      th5.append(initBtn);

        /* 비밀번호 초기화 버튼 클릭시 */
        // - Ajax를 이용해서 /resetPw (POST) 요청
        // - 서버로 제출되어야 하는 파라미터값 : memberNo
        // - 서버에서는 pass01!를 암호화해서 memberNo가 일치하는 회원의 비밀번호 update
        // 성공시 alert 비밀번호가 초기화 되었습니다.
        if( member.memberDelFl === 'Y' ){
          initBtn.disabled = true;
        } else {
          initBtn.addEventListener("click", () => {
            fetch("/resetPw", {
              method : "POST",
              headers : {"Content-Type" : "application/json"},
              body : member.memberNo
            })
            .then(response => {
              if(response.ok) return response.text();
              throw new Error("비밀번호 초기화 오류" + response.status)
            })
            .then(zeroOrOne => {
              if(zeroOrOne > 0){
                messageDiv.innerText = "비밀번호가 초기화 되었습니다.";
                popupLayer.classList.remove("display-hidden");
              } else {
                messageDiv.innerText = "비밀번호 초기화에 실패하였습니다.";
                popupLayer.classList.remove("display-hidden");
              }
            })
            .catch(errorMessage => console.error(errorMessage));
          });
        }


      // th > button 만들어서 "탈퇴 상태 변경" 글자 세팅
      const th6 = document.createElement("th");
      const changeBtn = document.createElement("button");
      changeBtn.innerText = "탈퇴 상태 변경";
      th6.append(changeBtn);

        /* 탈퇴상태 버튼 클릭시 */
        // - Ajax를 이용해서 /changeStatus (PUT) 요청
        // - 서버로 제출되어야 하는 파라미터값 : memberNo
        // 성공시 화면에 출력되어 있는 회원 목록 다시 조회해서 출력
        changeBtn.addEventListener("click", () => {
          fetch("/changeStatus", {
            method : "PUT",
            headers : {"Content-Type" : "application/json"},
            body : member.memberNo
          })
          .then(response => {
            if(response.ok) return response.text();
            throw new Error("탈퇴요청오류" + response.status)
          })
          .then(result => {
            if(result > 0){
              selectMemberList();
              messageDiv.innerText = "회원 탈퇴여부 변경 성공";
              popupLayer.classList.remove("display-hidden");

            } else {
              messageDiv.innerText = "회원 탈퇴여부 변경 실패";
              popupLayer.classList.remove("display-hidden");

            }
          })
          .catch(errorMessage => console.error(errorMessage));
        });

      // tr에 요소추가
      tr.append(th1, td2, th3, th4, th5, th6);

      // #memberList에 tr추가
      memberList.append(tr);

    });


  })
  .catch(err => console.error(err));

};


document.addEventListener("DOMContentLoaded", ()=>{
  selectMemberList();
});


//=================================================================================


