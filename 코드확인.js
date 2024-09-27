
// confirm 조각 확인용
async function performAction() {
  try {
    const result = await showConfirmPopup("정말로 삭제하시겠습니까?");
    if (result) {
      alert("삭제 작업을 진행합니다.");
      // 삭제 작업 실행
    }
  } catch (error) {
    alert("취소되었습니다.");
    // 취소 시의 처리
  }
}

// 버튼 클릭으로 액션 실행
document.querySelector("#someActionButton").addEventListener("click", performAction);