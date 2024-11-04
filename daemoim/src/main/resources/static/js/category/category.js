document.addEventListener('DOMContentLoaded', function() {
    const categoryButtons = document.querySelectorAll('.tab-link');  // 탭 버튼들
    const categoryContents = document.querySelectorAll('.category-content');  // 카테고리 콘텐츠들
    let activeContent = document.querySelector('.category-content.active'); // 현재 활성화된 콘텐츠
  
    // URL에서 query parameter 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const categoryType = urlParams.get('type') || 'allCategory';  // 기본값은 'allCategory'
  
    // 모든 카테고리 콘텐츠 숨기기
    categoryContents.forEach(content => content.classList.remove('active', 'slide-in', 'slide-out'));
  
    // URL에서 받아온 카테고리의 콘텐츠를 보여줌
    const targetContent = document.getElementById(categoryType);
    if (targetContent) {
        targetContent.classList.add('active', 'slide-in');
        activeContent = targetContent;
    }
  
    // 탭 클릭 시 카테고리 전환 처리
    categoryButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();  // 기본 동작 방지
  
            const targetId = this.getAttribute('data-target');
            const targetContent = document.getElementById(targetId);
  
            if (targetContent && targetContent !== activeContent) {
                // 현재 보이는 콘텐츠를 슬라이드 아웃
                activeContent.classList.remove('slide-in');
                activeContent.classList.add('slide-out');
  
                // 슬라이드 아웃이 끝난 후 콘텐츠 숨김
                setTimeout(() => {
                    activeContent.classList.remove('active', 'slide-out');
                    activeContent.style.visibility = 'hidden';
                    activeContent.style.opacity = '0';
                }, 500); // 애니메이션 시간이 0.5초이므로 슬라이드 완료 후 실행
  
                // 새로운 콘텐츠를 슬라이드 인
                targetContent.classList.add('active', 'slide-in');
                targetContent.style.visibility = 'visible';
                targetContent.style.opacity = '1';
  
                // 현재 활성화된 콘텐츠 업데이트
                activeContent = targetContent;
            }
  
            // 모든 버튼에서 active 클래스 제거
            categoryButtons.forEach(btn => btn.classList.remove('active'));
            // 클릭된 버튼에 active 클래스 추가
            this.classList.add('active');
        });
    });
});
