$(function() {
  // Datepicker를 #calendar에 적용
  $("#calendar").datepicker();
});

const schedule = document.querySelector("#schedulePage");

schedule.addEventListener("click", () => {
  location.href = `/board/boardSchedule`;
})