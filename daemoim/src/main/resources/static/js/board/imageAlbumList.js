

let currentPage = 1;
const photosPerPage = 4;
let allPhotos = [];

function createPhotoElement(photo) {
  return `
                <div class="photo-item" data-id="${photo.id}">
                    <img src="https://via.placeholder.com/300x300?text=Photo+${photo.id}" alt="${photo.title}">
                    <div class="photo-info">
                        <h3>${photo.title}</h3>
                        <p>댓글: ${photo.commentCount} | 날짜: ${photo.date} | 좋아요: ${photo.likes}</p>
                    </div>
                </div>
            `;
}

function loadPhotos() {
  // *** 수정 필요 ***
  // 실제 구현에서는 여기서 서버에 API 요청을 보내야
  // 이 예제에서는 더미 데이터를 사용
  const dummyData = Array(photosPerPage).fill().map((_, index) => ({
    id: (currentPage - 1) * photosPerPage + index + 1,
    title: `사진 ${(currentPage - 1) * photosPerPage + index + 1}`,
    commentCount: Math.floor(Math.random() * 10),
    date: new Date().toLocaleDateString(),
    likes: Math.floor(Math.random() * 100)
  }));

  allPhotos = [...allPhotos, ...dummyData];

  const gallery = document.getElementById('photo-gallery');
  dummyData.forEach(photo => {
    gallery.insertAdjacentHTML('beforeend', createPhotoElement(photo));
  });

  currentPage++;

  // 새로 추가된 요소들에 대해 클릭 이벤트 리스너를 추가합니다.
  addClickListeners();
}

function addClickListeners() {
  const photoItems = document.querySelectorAll('.photo-item');
  photoItems.forEach(item => {
    item.addEventListener('click', function () {
      const photoId = this.getAttribute('data-id');
      window.location.href = `boardDetail.html?id=${photoId}`;
    });
  });
}

function searchPhotos(position) {
  const searchTerm = document.getElementById(`${position}-search`).value.toLowerCase();
  const filteredPhotos = allPhotos.filter(photo =>
    photo.title.toLowerCase().includes(searchTerm)
  );

  const gallery = document.getElementById('photo-gallery');
  gallery.innerHTML = ''; // 기존 갤러리 내용을 지움

  filteredPhotos.forEach(photo => {
    gallery.insertAdjacentHTML('beforeend', createPhotoElement(photo));
  });

  addClickListeners();
}

document.getElementById('load-more').addEventListener('click', loadPhotos);

// 초기 로드
loadPhotos();

// 게시글 등록 버튼 클릭 이벤트
document.getElementById('add-post-btn').addEventListener('click', function () {
  alert('게시글 등록 페이지로 이동합니다.');
  // 여기에 게시글 등록 페이지로 이동하는 로직을 추가하세요.
});
