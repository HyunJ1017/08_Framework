const goToListBtn = document.querySelector("#goToListBtn");
goToListBtn.addEventListener("click", ()=>{
  location.href = "/board/" + boardCode;
});