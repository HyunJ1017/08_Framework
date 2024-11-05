const form = document.getElementById('boardWriteFrm');
const imageInput = document.getElementById('imageInput');
const imageList = document.getElementById('imageList');
const imageCount = document.getElementById('imageCount');
const addImageBtn = document.querySelector('.add-image-btn');
let imageArray = []; // 이미지 저장할 배열

const MAX_IMAGES = 20;
const MAX_FILE_SIZE = 10 * 1024 * 1024; // 파일 크기 제한 (10MB)


// 이미지 미리보기 생성 함수
function createPreviewElement(file, index) {
  // const submitInput = document.createElement("input");
  // submitInput.type = "file";





  const preview = document.createElement('div');
  preview.className = 'image-preview';
  preview.dataset.index = index;

  const previewWrapper = document.createElement('div');
  previewWrapper.className = 'preview-wrapper';

  const img = document.createElement('img');
  img.className = 'preview';

  const deleteBtn = document.createElement('button');
  deleteBtn.className = 'delete-image';
  deleteBtn.type = 'button'; // 명시적으로 button 타입 지정
  deleteBtn.innerHTML = '×';
  deleteBtn.onclick = (e) => {
    e.preventDefault();
    e.stopPropagation();
    confirmM("사진을 삭제하시겠습니까?")
      .then(result => {
        if (!result) return;
        removeImage(index);
      })
  };

  const fileName = document.createElement('div');
  fileName.className = 'file-name';
  fileName.textContent = file.name;

  const reader = new FileReader();
  reader.onload = (e) => {
    img.src = e.target.result;
  };
  reader.readAsDataURL(file);

  previewWrapper.appendChild(img);
  previewWrapper.appendChild(deleteBtn);
  previewWrapper.appendChild(fileName);
  preview.appendChild(previewWrapper);

  return preview;
} // createPreviewElement end


// 이미지 제거 함수
function removeImage(indexToRemove) {
  imageArray = imageArray.filter((_, index) => index !== indexToRemove);
  refreshImageDisplay();
  updateImageCount();
}


// 이미지 디스플레이 새로고침
function refreshImageDisplay() {

  // 추가 버튼을 제외한 모든 프리뷰 제거
  const previews = imageList.querySelectorAll('.image-preview');
  previews.forEach(preview => preview.remove());

  // 이미지 재렌더링
  imageArray.forEach((file, index) => {
    const preview = createPreviewElement(file, index);
    imageList.insertBefore(preview, addImageBtn);
  });
}


// 이미지 개수 업데이트
function updateImageCount() {
  imageCount.textContent = imageArray.length;
  addImageBtn.style.display = imageArray.length >= MAX_IMAGES ? 'none' : 'flex';
}


// 파일 유효성 검사
function validateFile(file) {
  if (file.size > MAX_FILE_SIZE) {
    alert(`${file.name}의 크기가 10MB를 초과합니다.`);
    return false;
  }
  if (!file.type.startsWith('image/')) {
    alert(`${file.name}은(는) 이미지 파일이 아닙니다.`);
    return false;
  }
  return true;
}


// 파일 입력 처리
function handleFileSelect(e) {
  const files = Array.from(e.target.files);
  const remainingSlots = MAX_IMAGES - imageArray.length;

  if (files.length > remainingSlots) {
    alert(`이미지는 최대 ${MAX_IMAGES}장까지만 업로드 가능합니다.`);
    files.length = remainingSlots;
  }

  const validFiles = files.filter(validateFile);
  if (validFiles.length > 0) {
    imageArray = [...imageArray, ...validFiles];
    refreshImageDisplay();
    updateImageCount();
  }

  // 입력 초기화 (같은 파일을 다시 선택할 수 있도록)
  e.target.value = '';
}


// 폼 제출 처리
function handleSubmit(e) {
  e.preventDefault();

  const title = form.querySelector('[name="boardTitle"]').value.trim();

  if (title.length === 0) {
    alert('제목을 작성해주세요.');
    form.querySelector('[name="boardTitle"]').focus();
    return;
  }

  if (imageArray.length === 0) {
    alert('사진을 등록해주세요.');
    return;
  }

  const formData = new FormData(form);
  // 기존 이미지 필드 제거 (중복 방지)
  formData.delete('images');
  // 이미지 배열의 파일들을 추가
  imageArray.forEach(file => {
    formData.append('images', file);
  });

  fetch(form.action + '2', {
    method: 'POST',
    body: formData
  })
    .then(response => {
      if (response.ok) return response.text();
      throw new Error("게시글 등록 중 오류가 발생했습니다.");
    })
    .then(result => {
      if (result !== null) {
        location.href = result;
      } else {
        alert(data.message || '게시글 등록에 실패했습니다.');
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });
}


// 취소 버튼 처리
function handleCancel(e) {
  e.preventDefault();
  confirmM("작성을 취소하시겠습니까?")
    .then(result => {
      if (!result) return;
    })
  history.back();
}

// 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', () => {
  imageInput.addEventListener('change', handleFileSelect);
  form.addEventListener('submit', handleSubmit);
  document.getElementById('cancelBtn').addEventListener('click', handleCancel);
});