// 신고 팝업 열기
const reportBtn = document.querySelector("#reportBtn");
reportBtn.addEventListener("click", () => {
    if (!confirm("정말 신고 하시겠습니까?")) {
        return;
    }
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('popup').style.display = 'block';
});

// 신고 팝업 닫기
const closeReportBtn = document.querySelector("#closeReportBtn");
closeReportBtn.addEventListener("click", () => {
    if (!confirm("신고를 취소하시겠습니까?")) {
        return;
    }
    clearForm();
    closePopup();
});


const insertReport = () => {

    
}









// form 제출 처리
const formTag = document.querySelector("#formTag");
formTag.addEventListener("submit", (e) => {
    // e.preventDefault();
    const reportReason = document.getElementById('reportReason');
    const reportDetails = document.getElementById('reportDetails');
    const reasonError = document.getElementById('reasonError');
    const detailsError = document.getElementById('detailsError');

    // 에러 메시지 초기화
    reasonError.style.display = 'none';
    detailsError.style.display = 'none';

    // 폼 검증
    if (reportReason.value === "null") {
        reasonError.textContent = "신고사유를 선택해주세요";
        reasonError.style.display = 'inline';
        reportReason.focus();
        e.preventDefault();
        return;
    }

    if (reportDetails.value.trim().length === 0) {
        detailsError.textContent = "신고내용을 자세하게 적어주세요";
        detailsError.style.display = 'inline';
        reportDetails.focus();
        e.preventDefault();
        return;
    }

    const inputType = document.createElement("input");
    inputType.type='hidden';
    inputType.value=document.querySelector("#reportReason").value;
    inputType.name = 'reportNo';
    formTag.append(inputType);

    const inputNo = document.createElement("input");
    inputNo.type='hidden';
    inputNo.value=reportBtn.dataset.memberNo;
    inputNo.name = 'memberNo';
    formTag.append(inputNo);

    let str = "";
    switch(reportReason.value){
        case "1": str = "욕설 또는 부적절한 언행"; break;
        case "2": str = "사이버범죄 및 범행예고"; break;
        case "3": str = "도배"; break;
        case "4": str = "악의적이거나 타인을 괴롭히는 내용"; break;
        case "5": str = "불쾌한거나 부적절한 이름"; break;
        case "6": str = "기타"; break;
        default : str = "";
    }
    // reason 내용과 details 내용을 \n을 써서 합친 내용이 담긴 
    // reportDetail(DB명과 맞춤)
    const reportDetail =  `${str}\n ${reportDetails.value}`;
    const input112 = document.createElement("input");
    input112.type='hidden';
    input112.value=reportDetail;
    input112.name = 'reportDetail';
    formTag.append(input112);
    
    // try {
    //     alert('신고가 접수되었습니다.');
    //     formTag.submit();
    //     clearForm();
    //     closePopup();
    // } catch (error) {
    //     console.error('Form submission error:', error);
    //     alert('신고 접수 중 오류가 발생했습니다.');
    // }
});

// ----- 기능들 -----


// 신고 사유 선택 시 에러 메시지 제거
const reportReasonSelect = document.getElementById('reportReason');
reportReasonSelect.addEventListener('change', () => {
    const reasonError = document.getElementById('reasonError');
    if (reportReasonSelect.value !== "null") {
        reasonError.style.display = 'none';
    }
});

// 상세 내용 입력 시 에러 메시지 제거
const reportDetailsInput = document.getElementById('reportDetails');
reportDetailsInput.addEventListener('input', () => {
    const detailsError = document.getElementById('detailsError');
    if (reportDetailsInput.value.trim().length > 0) {
        detailsError.style.display = 'none';
    }
});

// 폼 초기화
function clearForm() {
    document.getElementById('reportReason').value = "null";
    document.getElementById('reportDetails').value = "";
    document.getElementById('reasonError').style.display = 'none';
    document.getElementById('detailsError').style.display = 'none';
    
    // hidden input 제거
    const hiddenInputs = formTag.querySelectorAll('input[type="hidden"]');
    hiddenInputs.forEach(input => input.remove());
}

// 팝업 닫기
function closePopup() {
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
}