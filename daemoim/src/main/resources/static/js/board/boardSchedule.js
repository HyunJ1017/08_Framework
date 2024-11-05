function attendSchedule(scheduleNo, groupNo) {
  fetch('/board/attendSchedule', {
      method: 'POST',

      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: new URLSearchParams({scheduleNo: scheduleNo, groupNo: groupNo})
  })
  .then(response => {
          return response.text();
  }).then(result => {
    if(result == 0) alert("이미 참석 중입니다");
    else{
      alert("참석 완료");
      location.reload();
    }

  })


  .catch(error => {
      console.error('참석 요청 중 오류 발생:', error);
  });
}

function cancelSchedule(scheduleNo, groupNo) {
  fetch('/board/cancelSchedule', {
    method: 'POST',
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    body: new URLSearchParams({scheduleNo: scheduleNo, groupNo: groupNo})
  })
  .then(response => {
    console.log(response);
    location.reload();
  })
  .catch(error => {
  console.error('참석 요청 중 오류 발생:', error);
  });
}

document.querySelectorAll('.attend').forEach(button => {
  button.addEventListener('click', () => {

    // 현재 클릭된 버튼의 schedule-box 부모 요소 찾기
    const scheduleBox = button.closest('.schedule-box');
      
    // 해당 schedule-box 내부의 .input 요소에서 scheduleNo 가져오기
    const scheduleNo = scheduleBox.querySelector('.input').innerText;

    const groupNo = scheduleBox.querySelector('.input2').innerText;

    console.log(scheduleNo);
    console.log(groupNo);
      
    // attendSchedule 함수 호출
    attendSchedule(scheduleNo, groupNo);
  });
});

document.querySelectorAll('.cancel').forEach(button => {
  button.addEventListener('click', () => {
    // 현재 클릭된 버튼의 schedule-box 부모 요소 찾기
    const scheduleBox = button.closest('.schedule-box');
      
    // 해당 schedule-box 내부의 .input 요소에서 scheduleNo 가져오기
    const scheduleNo = scheduleBox.querySelector('.input').innerText;

    const groupNo = scheduleBox.querySelector('.input2').innerText;

    console.log(scheduleNo);
    console.log(groupNo);

    cancelSchedule(scheduleNo, groupNo);
  });
});

document.addEventListener("DOMContentLoaded", function() {
  const popup = document.getElementById("popup");
  const createScheduleBtn = document.getElementById("createScheduleBtn");
  const closePopupBtn = document.getElementById("closePopup");
  const createScheduleForm = document.getElementById("createScheduleForm");
  const urlParts = window.location.href.split("/");

  // 팝업 열기
  createScheduleBtn.addEventListener("click", () => {
      popup.classList.remove("hidden");
  });

  // 팝업 닫기
  closePopupBtn.addEventListener("click", () => {
      popup.classList.add("hidden");
  });

  // 폼 제출 시 서버로 POST 요청 보내기
  createScheduleForm.addEventListener("submit", function(event) {
      event.preventDefault(); // 폼의 기본 제출 방지

      // 폼 데이터 가져오기
      const scheduleData = {
          scheduleDate: document.getElementById("scheduleDate").value,
          location: document.getElementById("location").value,
          cost: document.getElementById("cost").value,
          groupNo: urlParts[urlParts.length - 1]
      };

      console.log(scheduleData);

      // 서버로 POST 요청 보내기
      fetch('/board/createSchedule', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(scheduleData)
      })
      .then(response => response.json())
      .then(result => {
        popup.classList.add("hidden"); // 팝업 닫기
        createScheduleForm.reset(); // 폼 초기화
        if (result > 0) {
              alert("일정 생성이 완료되었습니다.");
              location.reload();
          } else {
              throw new Error("일정 생성 실패");
          }
      })
      .catch(error => {
          console.error("오류 발생:", error);
          alert("일정 생성 중 오류가 발생했습니다.");
      });

  });
});

