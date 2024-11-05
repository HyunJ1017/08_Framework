function getCookie(key){

  const cookies = document.cookie; 
  
  const arr = cookies.split(";"); 

  const cookieObj = {}; 

  for(let entry of arr){
   
    const temp = entry.split("="); 

    cookieObj[temp[0]] = temp[1];
  }

  return cookieObj[key];
}

document.addEventListener("DOMContentLoaded", () => {

  const saveId = getCookie("saveId"); 

  if(saveId == undefined) return; 

  const memberId
    = document.querySelector("#signinform input[name=memberId]");

  const checkbox 
    = document.querySelector("#signinform input[name=saveId]");

  // 로그인 상태인 경우 함수 종료
  if(memberId == null) return;

  // 이메일 입력란에 저장된 이메일 출력
  memberId.value = saveId;

  // 이메일 저장 체크박스를 체크 상태로 바꾸기
  checkbox.checked = true;
});
