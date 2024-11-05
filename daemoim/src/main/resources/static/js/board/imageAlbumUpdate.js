const form = document.getElementById('boardUpdateFrm');
const imageInput = document.getElementById('imageInput');
const imageList = document.getElementById('imageList');
const imageCount = document.getElementById('imageCount');
const addImageBtn = document.querySelector('.add-image-btn');
let imageArray = []; // 새로 추가할 이미지 저장 배열
const deleteOrderList = new Set(); // 삭제된 기존 이미지의 번호

const MAX_IMAGES = 20;
const MAX_FILE_SIZE = 10 * 1024 * 1024; // 파일 크기 제한 (10MB)

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  initializeExistingImages();
  updateImageCount();
});

// 기존 이미지 초기화
function initializeExistingImages() {
  const existingImages = document.querySelectorAll('.existing-image');
  
  existingImages.forEach(wrapper => {
    const deleteBtn = wrapper.querySelector('.delete-image');
    const imageNo = wrapper.querySelector('input[name="existingImages"]').value;

    deleteBtn.addEventListener('click', () => {
      confirmM("사진을 삭제하시겠습니까?")
        .then(result => {
          if(!result) return;
          deleteOrderList.add(imageNo);
          wrapper.remove();
          updateImageCount();
        });
    });
  });
}

// 이미지 미리보기 생성
function createPreviewElement(file) {
  const wrapper = document.createElement('div');
  wrapper.className = 'image-wrapper';
  wrapper.style.position = 'relative';
  wrapper.style.display = 'inline-block';
  wrapper.style.margin = '10px';

  // 이미지 요소 생성
  const img = document.createElement('img');
  img.className = 'preview';
  img.style.width = '200px';  // 적절한 크기로 조정
  img.style.height = '200px'; // 적절한 크기로 조정
  img.style.objectFit = 'cover';

  // 삭제 버튼 생성
  const deleteBtn = document.createElement('button');
  deleteBtn.className = 'delete-image';
  deleteBtn.innerHTML = '×';
  deleteBtn.type = 'button';
  deleteBtn.style.position = 'absolute';
  deleteBtn.style.top = '5px';
  deleteBtn.style.right = '5px';
  deleteBtn.style.background = 'rgba(0, 0, 0, 0.5)';
  deleteBtn.style.color = 'white';
  deleteBtn.style.border = 'none';
  deleteBtn.style.borderRadius = '50%';
  deleteBtn.style.width = '25px';
  deleteBtn.style.height = '25px';
  deleteBtn.style.cursor = 'pointer';
  deleteBtn.style.display = 'flex';
  deleteBtn.style.alignItems = 'center';
  deleteBtn.style.justifyContent = 'center';
  deleteBtn.style.fontSize = '18px';
  deleteBtn.style.zIndex = '10';

  // 파일명 표시 요소
  const fileName = document.createElement('div');
  fileName.className = 'file-name';
  fileName.textContent = file.name;
  fileName.style.marginTop = '5px';
  fileName.style.fontSize = '12px';
  fileName.style.textAlign = 'center';

  // 이미지 미리보기 설정
  const reader = new FileReader();
  reader.onload = (e) => {
    img.src = e.target.result;
  };
  reader.readAsDataURL(file);

  
  const submitInput = document.createElement("input");
  submitInput.type = 'file';
  submitInput.name = 'images';
  submitInput.classList.add("display-none");
  const transfer = new DataTransfer();
  transfer.items.add(file);
  submitInput.files = transfer.files;
  form.append(submitInput);

  // 삭제 버튼 이벤트
  deleteBtn.addEventListener('click', () => {
    confirmM("추가한 사진을 삭제하시겠습니까?")
      .then(result => {
        if(!result) return;
        imageArray = imageArray.filter(f => f !== file);
        wrapper.remove();
        submitInput.remove();
        updateImageCount();
      });
  });

  // 요소들을 wrapper에 추가
  wrapper.appendChild(img);
  wrapper.appendChild(deleteBtn);
  wrapper.appendChild(fileName);

  return wrapper;
}

// 이미지 개수 업데이트
function updateImageCount() {
  const existingImagesCount = imageList.querySelectorAll('.existing-image').length;
  const totalCount = existingImagesCount + imageArray.length;
  imageCount.textContent = totalCount;
  addImageBtn.style.display = totalCount >= MAX_IMAGES ? 'none' : 'flex';
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
  const existingImagesCount = imageList.querySelectorAll('.existing-image').length;
  const remainingSlots = MAX_IMAGES - (existingImagesCount + imageArray.length);

  if (files.length > remainingSlots) {
    alert(`이미지는 최대 ${MAX_IMAGES}장까지만 업로드 가능합니다.`);
    return;
  }


  const validFiles = files.filter(validateFile);
  if (validFiles.length > 0) {
    validFiles.forEach(file => {
      const preview = createPreviewElement(file);
      imageList.insertBefore(preview, addImageBtn);
      imageArray.push(file);
    });
    updateImageCount();
  }

  // 입력 초기화
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

  const totalImageCount = imageList.querySelectorAll('.existing-image').length + imageArray.length;
  if (totalImageCount === 0) {
    alert('최소 1장 이상의 사진이 필요합니다.');
    return;
  }

  const formData = new FormData(form);
  
  // 삭제된 이미지 번호 추가
  deleteOrderList.forEach(imageNo => {
    formData.append('deleteList', imageNo);
  });

  // 새로운 이미지 파일들을 추가
  imageArray.forEach(file => {
    formData.append('images', file);
  });

  form.submit();
  
  // fetch(form.action, {
  //   method: 'POST',
  //   body: formData
  // })
  //   .then(response => {
  //     if(response.ok) return response.text();
  //     throw new Error("게시글 수정 중 오류가 발생했습니다.");
  //   })
  //   .then(result => {
  //     if (result !== null) {
  //       location.href = result;
  //     } else {
  //       alert('게시글 수정에 실패했습니다.');
  //     }
  //   })
  //   .catch(error => {
  //     console.error('Error:', error);
  //     alert(error.message);
  //   });
}

// 취소 버튼 처리
function handleCancel() {
  confirmM("수정을 취소하시겠습니까?\n변경사항이 저장되지 않습니다.")
    .then(result => {
      if(!result) return;
      history.back();
    });
}

// 이벤트 리스너 등록
imageInput.addEventListener('change', handleFileSelect);
form.addEventListener('submit', handleSubmit);
document.getElementById('writeBtn').addEventListener('click', handleSubmit);
document.getElementById('cancelBtn').addEventListener('click', handleCancel);