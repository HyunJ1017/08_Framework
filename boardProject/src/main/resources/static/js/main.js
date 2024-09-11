// 쿠키에 저장된 여러 값 중 key가 일치하는 value 반환하기
function getCookie(key){

  // 1. cookie 전부 얻어오기 ( String )
  const cookies = document.cookie; // "K=V;K=V;K=V;K=V;..."
  
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

  const saveEmail = getCookie("saveEmail"); // 쿠키에 저장된 Email 얻어오기

  // 저장된 이메일이 없는경우
  if(saveEmail == undefined) return;

  const memberEmail = document.querySelector("#loginForm input[name=memberEmail]");

  const checkbox = document.querySelector("#loginForm input[name=saveEmail]");

  // 로그인이 되어있는 상태면 종료
  if(memberEmail == null) return;

  // 이메일 입력란에 저장된 이메일을 출력
  memberEmail.value = saveEmail;
  checkbox.checked = true;
});