const outGroupButtons = document.querySelectorAll(".outGroupBtn");

outGroupButtons.forEach(button => {
  button.addEventListener("click", () => {
    const groupNo = button.dataset.groupNo;
    const userInput = prompt("정말로 모임에서 나가시겠습니까? '네'를 입력해주세요.");

    if (userInput === "네") {
      fetch("/myPage/myGroup", {  
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body:  groupNo 
      })
      .then(response => {
        if (response.ok) return response.json();
        throw new Error("요청 실패");
      })
      .then(result => {
        if (result.status === "error") {
          alert(result.message);
        } else {
          alert(result.message);
          window.location.href = "/myPage/myGroup";
        }
      })
      .catch(err => console.error(err));
    } else {
      alert("모임 탈퇴가 취소되었습니다.");
    }
  });
});
