
/* 페이지 로딩이 완료된 후 a태그 클릭 재설정 */
document.addEventListener("DOMContentLoaded", () => {
  
  document.querySelector("#logoLink").addEventListener('click', function(e) {
    e.preventDefault(); // 바로 페이지 이동하지 않도록 방지
    const logo = document.getElementById('homeLogo');
    
    // 로고에 확대 클래스 추가
    logo.classList.add('expand');
  
    // 애니메이션이 끝난 후 페이지 이동
    setTimeout(function() {
      window.location.href = '/'; // 메인 페이지로 이동
    }, 600); // CSS 애니메이션 시간과 일치시킴
  });
  
});