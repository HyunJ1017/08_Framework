document.addEventListener('DOMContentLoaded', () => {
    const groupContainer = document.getElementById('groupContainer');   
    const searchInput = document.getElementById('searchQuery');        
    let selectedCategory = new URLSearchParams(window.location.search).get('type') || 'allCategory';

    console.log("초기 selectedCategory:", selectedCategory); // 초기 설정된 카테고리 확인

    searchInput.addEventListener('input', () => {
        getGroups();  
    });

    // 각 카테고리 버튼에 대한 클릭 이벤트 리스너 추가
    document.querySelectorAll('.category-button').forEach(button => {
        button.addEventListener('click', (event) => {
            selectedCategory = event.target.getAttribute('data-type');
            console.log("변경된 selectedCategory:", selectedCategory); // 카테고리 변경 시 확인
            getGroups(); // 카테고리 변경 시 getGroups 호출
        });
    });

    const getGroups = () => {
        const query = searchInput.value || '';  
        const url = `/category/groups?type=${selectedCategory}&query=${query}`;

        console.log("fetch 요청 URL:", url); // 요청 URL 확인

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',  
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('네트워크 오류 발생');
            return response.json();  
        })
        .then(data => {
            console.log("서버 응답 데이터:", data); // 서버 응답 데이터 확인
            renderGroups(data);
        })
        .catch(error => {
            console.error('에러 발생:', error);
        });
    };

    const renderGroups = (groups) => {
        groupContainer.innerHTML = '';  
    
        groups.forEach(group => {
            console.log("그룹:", group); // 각 그룹 데이터 확인
            const groupHTML = `
                <div class="category-box" data-group-no="${group.groupNo}">
                <div class="icon">
                    <img src="${group.groupMainImg ? group.groupMainImg : '/images/default.png'}" alt="${group.groupName} 아이콘" />
                </div>
                <div class="details">
                    <div class="location">${group.categoryName}</div> <!-- location을 가장 위로 이동 -->
                    <a href="/groupMain/${group.groupNo}" class="categoryGroup-name" style="color: black; text-decoration: none;">
                        ${group.groupName}
                    </a>
                    <div class="group-name2">${group.groupIntroduce}</div>
                    <div class="participants">
                        <i class="fa-solid fa-users"></i>
                        <div>${group.currentPopulation} / ${group.maxPopulation}</div>
                    </div>
                </div>
            </div>
            `;
            groupContainer.insertAdjacentHTML('beforeend', groupHTML);
        });

        document.querySelectorAll('.category-box').forEach(box => {
        box.addEventListener('click', (event) => {
            if (event.target.tagName !== 'A') {
                const groupNo = box.getAttribute('data-group-no');
                window.location.href = `/groupMain/${groupNo}`;
            }
        });
    });


    };

    getGroups(); 
});

