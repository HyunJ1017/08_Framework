const urlParts = window.location.href.split("/");

document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');
  let popup = document.getElementById('popup'); // 팝업 엘리먼트
  let dialog = document.getElementById('dialog'); // 다이얼로그 엘리먼트
  let overlay = document.getElementById('overlay'); // 오버레이
  let eventDetails = []; // 선택된 날짜의 모든 이벤트를 저장할 배열

  // FullCalendar 초기화
  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    events: '/schedules/list/' + urlParts[urlParts.length - 1],  // 서버에서 SCHEDULE 데이터를 가져옴
    dayMaxEventRows: true,  // 날짜 칸 안에서 최대 이벤트 행을 제한함
    moreLinkClick: 'popover',  // "더 보기" 버튼을 팝업으로 표시하지 않음

    // 날짜 클릭 시
    dateClick: function(info) {
      const dateStr = info.dateStr; // 클릭한 날짜 (YYYY-MM-DD 포맷)
      eventDetails = calendar.getEvents().filter(event => event.startStr === dateStr); // 해당 날짜의 이벤트들 필터링

      if (eventDetails.length > 0) {
        // 팝업에 이벤트 목록을 추가
        let eventListHtml = `<h3>${dateStr} 일정 목록</h3><ul>`;
        eventDetails.forEach((event, index) => {
          eventListHtml += `<li><a href="#" data-index="${index}">${event.title}</a></li>`;
        });
        eventListHtml += `</ul>`;
        document.getElementById('popup-content').innerHTML = eventListHtml;

        // 팝업과 오버레이 활성화
        popup.classList.add('active');
        overlay.classList.add('active');
      }
    },
    
    // 이벤트 클릭 시 (팝업 내에서 세부 사항 보여주기)
    eventClick: function(info) {
      info.jsEvent.preventDefault(); // 기본 클릭 동작을 방지
      // 다이얼로그에 이벤트 세부 정보 삽입
      dialog.querySelector('#detail').innerHTML =
        `<h3>${info.event.title}</h3>
        <div>${info.event.extendedProps.description || 'No description available.'}</div>`;

      // 다이얼로그 열기
      dialog.classList.add('active');
      overlay.classList.add('active');
    }
  });

  calendar.render();

  // 팝업에서 일정 클릭 시, 해당 일정의 세부 정보를 다이얼로그에 표시
  document.getElementById('popup-content').addEventListener('click', function(e) {
    if (e.target.matches('a')) {
      e.preventDefault();
      const index = e.target.getAttribute('data-index');
      const selectedEvent = eventDetails[index];

      // 다이얼로그에 선택한 일정의 세부 정보 삽입
      document.getElementById('detail').innerHTML =
        `<h3>${selectedEvent.title}</h3>
        <div>${selectedEvent.extendedProps.description || 'No description available.'}</div>`;

      // 다이얼로그 열기
      dialog.classList.add('active');
      popup.classList.remove('active'); // 팝업 닫기
    }
  });

  // 팝업 닫기 버튼
  document.getElementById('popup-close').addEventListener('click', () => {
    popup.classList.remove('active');
    overlay.classList.remove('active');
  });

  // 다이얼로그 닫기 버튼
  document.getElementById('dialog-close').addEventListener('click', () => {
    dialog.classList.remove('active');
    overlay.classList.remove('active');
  });

  // 오버레이 클릭 시 팝업과 다이얼로그 닫기
  overlay.addEventListener('click', () => {
    popup.classList.remove('active');
    dialog.classList.remove('active');
    overlay.classList.remove('active');
  });
});
