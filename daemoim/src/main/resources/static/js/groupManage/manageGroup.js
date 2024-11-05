
/* 카테고리 변경 */
// 카테고리 변경버튼
const categoryBtn = document.querySelector("#categoryBtn");
const categoryListBtn = document.querySelector("#categoryListBtn");
// 카테고리
const categoryBefore = document.querySelector("#categoryBefore");
const categoryAfter = document.querySelector("#categoryAfter");
// 카테고리리스트
const categoryListBefore = document.querySelector("#categoryListBefore");
const categoryListAfter = document.querySelector("#categoryListAfter");
const CategoryListTr = document.querySelector("#CategoryListTr");


// 카테고리 변경화면 출력하기
categoryBtn?.addEventListener("click", ()=>{

  getCategoryList(checkedCategory());

  // 화면 처음창 -> 수정창
  categoryBefore.classList.add("display-none");
  categoryListBefore.classList.add("display-none");

  categoryAfter.classList.remove("display-none");
  categoryListAfter.classList.remove("display-none");

  detailConfirm.category = false;
  detailConfirm.categoryList = false;

});


// 카테고리 변경화면 출력하기2
categoryListBtn?.addEventListener("click", ()=>{

  getCategoryList(checkedCategory());

  // 화면 처음창 -> 수정창
  categoryListBefore.classList.add("display-none");
  categoryListAfter.classList.remove("display-none");

  detailConfirm.categoryList = false;

});

// 체크된 카테고리 라디오의 값 얻어오기
const checkedCategory = () => {
  // name="categoryNo"인 라디오 버튼 중에서 체크된 버튼 선택
  const checkedRadio = document.querySelector('input[name="categoryNo"]:checked');
  
  // 체크된 버튼이 있으면 그 value 값을 반환, 없으면 null 반환
  if (checkedRadio) {
    return checkedRadio.value;
  } else {
    return null; // 아무 것도 체크되지 않은 경우
  }
}

// 체크된 카테고리 리스트 라디오의 값 얻어오기(제출전확인용)
const checkedCategoryList = () => {
  // name="categoryNo"인 라디오 버튼 중에서 체크된 버튼 선택
  const checkedRadio = document.querySelector('input[name="categoryListNo"]:checked');
  
  // 체크된 버튼이 있으면 그 value 값을 반환, 없으면 null 반환
  if (checkedRadio) {
    return checkedRadio.value;
  } else {
    return null; // 아무 것도 체크되지 않은 경우
  }
}


// CategoryNo을 넘겨받아 비동기로 카테고리리스트의 화면을 최신화 하기
const getCategoryList = (categoryNo)=> {

  fetch("/groupManage/getCategoryList?categoryNo=" + categoryNo)
  .then(response => {
    if(response.ok)return response.json();
    throw new Error("카테고리 불러오기 오류")
  })
  .then(categoryList => {

    if(categoryList.length === 0) return;

    CategoryListTr.innerHTML = '';

    categoryList.forEach( e => {
      const div = document.createElement("div");
      const label = document.createElement("label");
      label.innerText = e.categoryListName;
      const input = document.createElement("input");
      input.type = 'radio';
      input.name = 'categoryListNo';
      input.value = e.categoryListNo;
      input.id = 'categoryList' + e.categoryListNo;
      input.classList.add("categoryListRadio");
      // input.addEventListener("click", ()=>{createConfirm.categoryList = true});
      if(e.categoryListNo === beforeCategoryListNo){
        input.checked = true;
      }
      label.htmlFor = 'categoryList' + e.categoryListNo;
      div.append(input, label);
      CategoryListTr.append(div);
    })

  })
  .catch( err => console.error(err));
};


// 카테고리 변경시 카테고리 리스트 불러오기
const radioArr = document.querySelectorAll(".categoryRadio");
radioArr.forEach(e=>{
  e.addEventListener("change", e=>{

    // alertM("e.value : " + e.target.value + ", e.value.type" + typeof Number( e.target.value ));

    getCategoryList( Number( e.target.value ) );
  })
});


/********************************************************************** */
/********************************************************************** */

/* [할일]

1. 모임명 유효성 검사
- 필수입력, 중복X, 500자, (한글, 알파벳, 숫자, 공백)만
2. 모임소개 유효성 검사
- 필수입력, 4000자
3. 이미지 유효성검사
- 10MB제한, 이미지 이름 50자 제한
4. 카테고리 유효성검사
- 필수입력(2개모두)
- 카테고리리스트 글자제한 30자

5. 제출전 확인사항
- 필수입력사항 모두 입력했는지
- 유효성검사 불합격 항목은 없는지


overflow: hidden; -> 텍스트가 너비를 넘기면 ... 처리
*/

//--------------------------------------------------------------

// 유효성검사항목
const detailConfirm = {
  "groupName"      : true ,
  "groupIntroduce" : true ,
  "category"       : true, // 제출할때 검사
  "categoryList"   : true  // 제출할때 검사
}

const updateForm = document.querySelector("#updateForm");
const submitDiv = document.querySelector(".submitDiv");
submitDiv?.addEventListener("click", e => {

  if(detailConfirm.groupName === false){
    alertM("모임명을 확인해 주세요.");
    groupName.focus();
    return;
  }
  
  if(detailConfirm.groupIntroduce === false){
    alertM("모임소개를 확인해 주세요.");
    groupIntroduce.focus();
    return;
  }
  
  if(detailConfirm.category === false){
    if(checkedCategory() === null){// 체크된 카테고리가 없는경우
      alertM("카테고리 체크를 확인해주세요");
      return;
    }
  }
  
  if(detailConfirm.categoryList === false){
    if(checkedCategoryList() === null){// 체크된 카테고리가 없는경우
      alertM("카테고리 리스트 체크를 확인해주세요");
      return;
    }
  }

  // deleteOrderList 적재
  const input = document.createElement("input");
  input.type = 'hidden';
  input.name = 'deleteOrderList';
  input.value = Array.from(deleteOrderList);

  updateForm.append(input);

  updateForm.submit();
});

/********************************************************************** */
/********************************************************************** */


/* 모임이름
1. 모임명 유효성 검사
- 필수입력
- 중복X
- 500자
- 한글, 알파벳, 숫자, 공백 만 입력 가능
- 현재모임명과 동일한 경우 pass
*/

const groupName = document.querySelector("#groupName");
const nameMessage = document.querySelector("#groupNameMessage");
const countName = document.querySelector("#countName");


const nameMessageConfirm = {
  "nomal"    : "현재 모임에서 사용중인 모임명 입니다.",
  "shortage" : "3글자 이상의 이름을 사용해 주십시오.",
  "tooLong"  : "최대 500자까지 입력이 가능합니다.",
  "rules"    : "한글, 알파벳, 숫자, 띄어쓰기 만 입력 가능 합니다.",
  "overlap"  : "동일한 이름을 이미 사용중입니다.",
  "statusOk" : "사용 가능한 이름 입니다."
};

// 모임 이름이 입력될때마다 발생할 이벤트
groupName?.addEventListener("input", () => {

  const inputName = groupName.value.trim();
  const inputLength = inputName.length;
  // console.log(inputName);

  // 글자수 표시
  countName.innerText = inputLength + '/500';

  // 기존 이름과 동일할때
  if(inputName === originalGroupName){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.nomal;
    countName.classList.remove("red");
    nameMessage.classList.remove("red");
    nameMessage.classList.add("green");
    detailConfirm.groupName = true;
    return;
  }

  // 3글자 미만일때
  if(inputLength < 3){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.shortage;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }

  // 500글자 넘을 때
  if(inputLength > 500){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.tooLong;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }
  
  // 입력한거 확인
  const pattern = /^[가-힣a-zA-Z0-9\s]+$/;
  if(pattern.test(inputName) === false){
    nameMessage.innerText = "";
    nameMessage.innerText = nameMessageConfirm.rules;
    countName.classList.add("red");
    nameMessage.classList.add("red");
    nameMessage.classList.remove("green");
    detailConfirm.groupName = false;
    return;
  }

  
  // 중복되는 이름일때
  fetch("/groupManage/groupNameCheck?inputName=" + inputName)
  .then(response=>{
    if(response.ok) return response.text();
    throw new Error("모임이름검사 오류 : " + response.sratus)
  })
  .then(result => {
    if(result > 0){
      // 중복되는 경우가 있는 경우
      nameMessage.innerText = "";
      nameMessage.innerText = nameMessageConfirm.overlap;
      nameMessage.classList.add("red");
      nameMessage.classList.remove("green");
      detailConfirm.groupName = false;
      return;
    }
  })
  .catch(error => console.error(error));
  
  // 사용 가능한 이름일 경우
  nameMessage.innerText = "";
  nameMessage.innerText = nameMessageConfirm.statusOk;
  countName.classList.remove("red");
  nameMessage.classList.remove("red");
  nameMessage.classList.add("green");
  detailConfirm.groupName = true;
  return;

});


/********************************************************************** */
/********************************************************************** */


/* 모임소개
2. 모임소개 유효성 검사
- 필수입력
- 4000자
*/

const groupIntroduce = document.querySelector("#groupIntroduce");
const countIntroduce = document.querySelector("#countIntroduce");
const preViewIntroduce = document.querySelector("#preViewIntroduce");
const groupIntroduceMessage = document.querySelector("#groupIntroduceMessage");

groupIntroduce?.addEventListener("input", ()=>{

  const inputIntroduce = groupIntroduce.value.trim();
  const inputLength = inputIntroduce.length;

  preViewIntroduce.innerText = inputIntroduce;

  if(inputLength < 1){
    detailConfirm.groupIntroduce = false;
    groupIntroduceMessage.classList.add("red");
    groupIntroduceMessage.innerText="필수 입력사항 입니다.";
    return;
  }

  if(inputLength > 4000){
    detailConfirm.groupIntroduce = false;
    groupIntroduceMessage.classList.add("red");
    groupIntroduceMessage.innerText="4000자 이내로 입력해 주세요.";
    countIntroduce.innerText = inputLength + '/4000';
    return;
  }

  detailConfirm.groupIntroduce = true;
  groupIntroduceMessage.classList.remove("red");
  groupIntroduceMessage.innerText="모임의 소개글을 입력해 주세요.";
  countIntroduce.innerText ='';

});



/********************************************************************** */
/********************************************************************** */


/* 이미지 입력
3. 이미지 유효성검사
- 10MB제한
- 이미지 이름 50자 제한

java가서만 따로따로 받으면되지 검사 자체는 뭉뚱그려서 검사해도 되지않나...
*/

// 이미지 인풋태그
const inputImageArr = document.getElementsByName("inputImg");
const imgName2 = document.querySelector("#imgName2");

// 이미지 미리보기 창
const imgPreview = document.querySelectorAll(".inputImgPreview");

// 백업용 이미지
const lastImg = [null, null];

// 삭제이미지 순서를 저장할 리스트
const deleteOrderList = new Set();
// 이미지 추가되면 지워주고, x 누르면 채워줘야함

/* input에 이미지가 변한경우 */
for(let i=0; i < inputImageArr?.length ; i++){

  inputImageArr[i]?.addEventListener("change", e=>{
    const file = e.target.files[0];
    
    if(file === undefined){
      if(lastImg[i] === null) return;
      backupLoad(i);
      return;
    }

    if(file.size > 1*1024*1024*1){
      alertM("파일크기가 10MB를 초과합니다");
      backupLoad(i);
      return;
    }

    // 해더이미지 비율 확인
    if( i > 0 ){
      const reader = new FileReader();
      reader.readAsDataURL(file);

      const img = new Image();

      reader.addEventListener("load", e => {
        img.src = e.target.result;
        const width = img.width;
        const height = img.height;
        const ratio = width / height;

        if(ratio <= 4 || ratio >= 9){
          alertM("이미지 크기가 올바르지 않습니다.\n현재비율 : " + ratio);
          imgName2.innerText = "* 권장비율 4:1 ~ 9:1";
          backupLoad(i);
          return;
        }
        imgName2.innerText = '';

        imgPreviewFunction(file, i); // 미리보기 함수 호출
      })
    } // 이미지 비율확인 종료

  }) // inputImage event end
} // for end

const backupLoad = (i) => {
  const transfer = new DataTransfer();
  if(lastImg[i] == null) return;
  transfer.items.add(lastImg[i]);
  inputImageArr[i].files = transfer.files;
};

const imgPreviewFunction = (file, order) => {

  lastImg[order] = file;
  
  // 입력받은 파일을 미리보기창에 url형태로 전달
  const reader = new FileReader();
  reader.readAsDataURL(file);

  reader.addEventListener("load", e => {

    imgPreview[order].src=e.target.result;
    // 메인상단이미지는 미리보기 화면도 바꿈
    if(order === 1){
      imgPreview[2].src = e.target.result;
    }
  });

  deleteOrderList.delete(order);

}; // imgPreviewFuntion() end

// X 버튼 클릭시 기본이미지로 변경
const imgDelBtns = document.querySelectorAll(".imgDelBtn");
imgDelBtns?.forEach((btn, index) => {
  btn.addEventListener("click", () => {

    imgPreview[index].src = defaultImg[index];
    inputImageArr[index].value = '';
    lastImg[index] = null;

    // 하단 미리보기화면도 잊지않고 챙겨줌
    if(index === 1){
      imgPreview[2].src = defaultImg[1];
    }

    if(originalImg[index] !== null){
      deleteOrderList.add(index);
    }
  });
});

// 변경취소 이미지 입력시 원래대로 바꾸기
const deleteImgBtns = document.querySelectorAll(".deleteImg");
deleteImgBtns?.forEach((btn, index) => {
  btn.addEventListener("click", () => {

    if(originalImg[index] !== null){
      imgPreview[index].src = originalImg[index];
    } else {
      imgPreview[index].src = defaultImg[index];
    }
    inputImageArr[index].value = '';
    lastImg[index] = null;

    if(index === 1){
      imgPreview[2].src = defaultImg[1];
    }

    deleteOrderList.delete(index);
  });
});




/********************************************************************** */
/********************************************************************** */


const params = new URLSearchParams(location.search);
let cp    = 1;
let order = 0;
if(params.get("cp") !== null){cp = params.get("cp")};
if(params.get("order") !== null){order = params.get("order")};

/* 페이지네이션 내부의 a태그들 클릭시 해당 페이지로 이동 */
const paginations = document.querySelectorAll(".pagination a");
paginations?.forEach( (e) => {
  e.addEventListener("click", btn => {
    btn.preventDefault();
    // console.log(btn.target.innerText);
    // console.log("cp : " + cp);
    // console.log("order : " + order);

    const i = btn.target.innerText;

    switch(i){
      case '<'  : cp = pagination.prevPage; break;
      case '<<' : cp = 1; break;
      case '>>' : cp = pagination.maxPage; break;
      case '>'  : cp = pagination.nextPage; break;
      default   : cp = i; break;
    }

    // console.log( cp + "페이지 이동 눌림");

    let url = location.pathname + `?cp=${cp}&order=${order}`;
    location.href = url;

  });
});

//------------------------------------------------------------------
//------------------------------------------------------------------


/* 오름차순 1아이디 2닉네임 3가입일
내림차순 -1아이디 -2닉네임 -3가입일*/

// 화면정렬 변경하기
const orderBtnArr = document.querySelectorAll(".orderBtn");
orderBtnArr.forEach( (orderBtn, index) => {
  orderBtn.addEventListener("click", ()=>{
    // console.log(index);
    const check = index + 1;

    if(order == check || order == check * -1){
      order = (order * (-1));
    } else {
      order = check;
    }

    // console.log("order : " + order);

    let url = location.pathname + `?cp=${cp}&order=${order}`;
    location.href = url;

  })
})



/********************************************************************** */
/********************************************************************** */



/* 강퇴 버튼 */

const removeMemberArr = document.querySelectorAll(".removeMember");

const removeMemberArrBtn = () => {
  removeMemberArr?.forEach( (btn) => {
    btn.addEventListener("click", ()=>{

      const btnMemberNo = btn.dataset.memberNo;

      confirmM("해당 회원을 모임에서 강퇴 하시겠습니까?")
      .then(result => {
        if(!result) return;

        memberRemove(btnMemberNo);
      });
    });
  });
};

// 강퇴기능 수행 함수
const memberRemove = (memberNo) => {

  fetch("/groupMember", {
    method : "DELETE",
    headers : {"Content-Type" : "application/json"},
    body : memberNo
  })
  .then( response => {
    if(response.ok) return response.text();
    throw new Error("멤버삭제 비동기통신 실패")
  })
  .then( result => {
    /*
     0 : 실패
     1 : 성공
     2 : GROUP_NO 오류
     3 : 모임장 불일치
    */
    switch(result){
      case '0' : alertM("회원을 강퇴에 실패 하였습니다."); break;
      case '1' : alertM("회원을 강퇴 시켰습니다."); break;
      case '2' : alertM("모임번호를 불러오는데 실패하였습니다."); break;
      case '3' : alertM("모임장만 강퇴할 수 있습니다."); location.href="/"; break;
      default : alertM("알 수 없는 오류가 발생하였습니다.");
    }

    // 화면초기화 함수 호출
    tableAjaxRequest(1);
  })
  .catch( err => console.error(err) );

};


/********************************************************************** */
/********************************************************************** */


/* 권한위임 버튼 */

const delegateArr = document.querySelectorAll(".delegate");

const delegateArrBtn = () => {
  delegateArr?.forEach( (btn) => {
    btn.addEventListener("click", ()=>{

      confirmM("모임장 권한을 이임하시겠습니까?")
      .then(result => {
        if(!result) return;

        /* 컨펌 진행함수 start */
        
        // 클릭버튼에서 얻어온 회원번호
        const btnMemberNo = btn.dataset.memberNo;
  
        let memberNickname;
  
        // 위임하려는 회원정보 얻어오기
        fetch("/groupMember?memberNo=" + btnMemberNo)
        .then( response => {
          if(response.ok) return response.text();
          throw new Error("멤버조회 비동기통신 실패")
        })
        .then( result => {
  
          memberNickname = result;
  
          confirmM( "[ " + memberNickname + " ] 회원에게 모임장의 권한을 양도하는것이 맞습니까?")
          .then(result => {
            if(!result) return;
            // 컨펌 진행함수 start 
            // body태그에 form태그 만들어서 제출
            const form = document.createElement("form");
            form.action = "/groupMemberManage/changeLeader";
            form.method = "POST";
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "nextLeader";
            input.value = btnMemberNo;
            form.append(input);
            document.querySelector("body").append(form);
            form.submit();
            
          });// 컨펌 진행함수 end
  
        })
        .catch( err => console.error(err) );

      }); // 컨펌 진행함수 end


    });
  });
};




//----------------------------------------------------------------
/* 차단해제 버튼 */

const backupMemberArr = document.querySelectorAll(".backupMemberBtn");

const backupMemberArrBtn = () => {
  backupMemberArr?.forEach( (btn) => {
    btn.addEventListener("click", ()=>{

      const btnMemberNo = btn.dataset.memberNo;

      confirmM("해당 회원을 차단해제 하시겠습니까?")
      .then(result => {
        if(!result) return;

        /* 컨펌 진행함수 start */
        
        memberBan(btnMemberNo);

        /* 컨펌 진행함수 end */
      });
    });
  });
}

// 강퇴 해제 기능함수
const memberBan = (memberNo) => {

  fetch("/groupMember", {
    method : "PUT",
    headers : {"Content-Type" : "application/json"},
    body : memberNo
  })
  .then( response => {
    if(response.ok) return response.text();
    throw new Error("멤버 강퇴해제 비동기통신 실패")
  })
  .then( result => {
    /*
     0 : 실패
     1 : 성공
     2 : GROUP_NO 오류
     3 : 모임장 불일치
    */
    switch(result){
      case '0' : alertM("작업 실패 하였습니다."); break;
      case '1' : alertM("회원을 차단해제 시켰습니다."); break;
      case '2' : alertM("모임번호를 불러오는데 실패하였습니다."); break;
      case '3' : alertM("모임장만 강퇴할 수 있습니다."); location.href="/"; break;
      default : alertM("알 수 없는 오류가 발생하였습니다.");
    }

    // 화면 초기화 함수 호출
    tableAjaxRequest(3);
  })
  .catch( err => console.error(err) );

};



/********************************************************************** */
/********************************************************************** */


/* 모임 가입 수락/거절 */

const inviteAgreeBtns = document.querySelectorAll(".inviteAgree");
const inviteRefuseBtns = document.querySelectorAll(".inviteRefuse");

// 요청 수락, 거절 시 메세지 보낼수 있으면 좋을것 같음
const btnsAddEvent = () => {

  // 동의버튼 이벤트 추가
  inviteAgreeBtns?.forEach((btn)=>{
    
    btn.addEventListener("click", ()=>{
      const btnMemberNo = btn.dataset.memberNo;
      const btnMemberNickname = btn.dataset.memberNickname;

      confirmM("[" + btnMemberNickname + "] 님의 가입을 승인 하시겠습니까?")
      .then(result => {
        if(!result) return;

        // 컨펌 진행함수 start 
        const inviteObj = {
          "memberNo" : btnMemberNo,
          "groupNo" : groupNo,
          "inviteDelFl" : "Y"
        };
  
        // 비동기 요청 호출
        inviteSubmit(inviteObj);

      }); // 컨펌 진행함수 end

      
    });
  });
  
  // 거절버튼 이벤트 추가
  inviteRefuseBtns?.forEach((btn)=>{
    
    btn.addEventListener("click", ()=>{
      const btnMemberNo = btn.dataset.memberNo;
      const btnMemberNickname = btn.dataset.memberNickname;

      confirmM("[" + btnMemberNickname + "] 님의 가입을 거절 하시겠습니까?")
      .then(result => {
        if(!result) return;
        // 컨펌 진행함수 start 
        const inviteObj = {
          "memberNo" : btnMemberNo,
          "groupNo" : groupNo,
          "inviteDelFl" : "N"
        };
  
        // 비동기 요청 호출
        inviteSubmit(inviteObj);
        
      }); // 컨펌 진행함수 end

    
    });
  });
};

// 수락&거절 요청
const inviteSubmit = (inviteObj) => {

  fetch("/groupMember/invite", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(inviteObj)
  })
  .then(response => {
    if(response.ok) return response.text();
    throw new Error("가입요청오류")
  })
  .then(result => {
    /*
     0 : 실패
     1 : 성공
     2 : 모임인원초과
     3 : 모임장 불일치
     4 : 강퇴회원
     5 : 중복가입
    */
     switch(result){
      case '0' : alertM("작업 실패 하였습니다."); break;
      case '1' : alertM("회원이 가입되었습니다."); break;
      case '2' : alertM("현재 모임의 정원이 초과되었습니다."); break;
      case '3' : alertM("모임장의 권한입니다."); location.href="/"; break;
      case '4' : alertM("강퇴한 회원 입니다."); break;
      case '5' : alertM("이미 가입되어있는 회원 입니다."); break;
      default : alertM("알 수 없는 오류가 발생하였습니다.");
    }
    // 화면 초기화 함수 호출
    tableAjaxRequest(2);
  })
  .catch(err => console.error(err));

};



/********************************************************************** */
/********************************************************************** */


// 화면(표) 초기화 요청
const tableBody = document.querySelector(".tableBody");

const tableAjaxRequest = (int) => {

  // int
  // 1 : 전체회원 조회
  // 2 : 가입신청인원 조회
  // 3 : 강퇴회원 조회

  // cp, order, groupNo : 전역변수 설정 가져오기

  const listObj = {
    "requestType" : int,
    "groupNo"     : groupNo,
    "cp"          : cp,
    "order"       : order
  };

  fetch("/groupMemberManage/getTableList", {
    method : "POST",
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(listObj)
  })
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("리스트요청오류")
  })
  .then(resultList =>{
    
    // 테이블 바꾸고
    // 페이지네이션 안받아와도 됨
    // -> 현재페이지로 죄회될것임
    // 하고 버튼 이벤트추가 재호출
    tableBody.innerHTML = '';

    resultList.forEach((member) => {
      const tr = document.createElement("tr");
      
      switch(int){
        
        case 1 :

          const td11 = document.createElement("td");
            td11.innerText = member.memberId;
          const td12 = document.createElement("td");
            const img121 = document.createElement("img");
          img121.style.width = '40px';
              img121.style.height = '40px';
            if(member.memberImg != null){
              img121.src = member.memberImg;
            } else {
              img121.src = "/images/dafault.png";
            }
            td12.append(img121);
          const td13 = document.createElement("td");
            td13.innerText = member.memberNickname;
          const td14 = document.createElement("td");
            td14.innerText = member.memberGroupEnrollDate;
          const td15 = document.createElement("td");
            if(groupNo !== member.memberNo){
              const btn = document.createElement("button");
              btn.classList.add("removeMember");
              btn.dataset.memberNo = member.memberNo;
              btn.innerText = "강퇴";
              td15.append(btn);
            }
          const td16 = document.createElement("td");
            if(groupNo !== member.memberNo){
              const btn = document.createElement("button");
              btn.classList.add("delegate");
              btn.dataset.memberNo = member.memberNo;
              btn.innerText = "권한 위임";
              td16.append(btn);
            }

          tr.append(td11, td12, td13, td14, td15, td16);
        break;
          
        case 2 :
          const td21 = document.createElement("td");
            td21.innerText = member.memberId;
          const td22 = document.createElement("td");
            const img221 = document.createElement("img");
              img221.style.width = '40px';
              img221.style.height = '40px';
              if(member.memberImg != null){
                img221.src = member.memberImg;
              } else {
                img221.src = "/images/dafault.png";
              }
            td22.append(img221);
          const td23 = document.createElement("td");
            td23.innerText = member.memberNickname;
          const td24 = document.createElement("td");
            td24.innerText = member.inviteReqTime;
          const td25 = document.createElement("td");
            const btn1 = document.createElement("button");
            btn1.classList.add("inviteAgree");
            btn1.dataset.memberNo = member.memberNo;
            btn1.innerText = "승인";
            td25.append(btn1);
          const td26 = document.createElement("td");
            const btn2 = document.createElement("button");
            btn2.classList.add("inviteRefuse");
            btn2.dataset.memberNo = member.memberNo;
            btn2.innerText = "거절";
            td26.append(btn2);

          tr.append(td21, td22, td23, td24, td25, td26);
        break;

        case 3 :
          const td31 = document.createElement("td");
            td31.innerText = member.memberId;
          const td32 = document.createElement("td");
            const img321 = document.createElement("img");
              img321.style.width = '40px';
              img321.style.height = '40px';
              if(member.memberImg != null){
                img321.src = member.memberImg;
              } else {
                img321.src = "/images/dafault.png";
              }
              td32.append(img321);
          const td33 = document.createElement("td");
            td33.innerText = member.memberNickname;
          const td34 = document.createElement("td");
            td34.innerText = member.memberGroupEnrollDate;
          const td35 = document.createElement("td");
            const btn3 = document.createElement("button");
            btn3.classList.add("backupMemberBtn");
            btn3.dataset.memberNo = member.memberNo;
            btn3.innerText = "차단해제";
            td35.append(btn3);

          tr.append(td31, td32, td33, td34, td35);
        break;
      }
      
      tableBody.append(tr);
      
    });
    
    backupMemberArrBtn();
    btnsAddEvent();
    delegateArrBtn();
    removeMemberArrBtn();

  })
  .catch();


};



/********************************************************************** */
/********************************************************************** */




// 게시판 전체보기버튼 클릭시
 const gotoBoard1 = document.querySelector(".gotoBoard1"); // 공지게시판
 const gotoBoard2 = document.querySelector(".gotoBoard2"); // 일반게시판
 gotoBoard1?.addEventListener("click", () => {location.href = "/board/" + groupNo + "/1" });
 gotoBoard2?.addEventListener("click", () => {location.href = "/board/" + groupNo + "/2" });


/********************************************************************** */
/********************************************************************** */

/* 알림보내기 */
const selectAll = document.querySelector(".noti-selectAll input");
selectAll?.addEventListener("change", ()=> {
  const inputTags = document.querySelectorAll(".noti-tbody input");
  
  inputTags.forEach(e=>{
    e.checked = selectAll.checked;
  })
  
});

// 일정선택이벤트
const selectSchedule = document.querySelector("#selectSchedule");
selectSchedule?.addEventListener("change", e=>{
  selectAll.checked = false;
  const scheduleNo = e.target.value;
  const inputTags = document.querySelectorAll(".noti-tbody input");
  
  if(scheduleNo == 0){
    selectAll.checked = false;
    inputTags.forEach(e=>{
      e.checked = false;
    })
    return;
  }

  fetch("/groupManage/searchScheduleMember?scheduleNo=" + scheduleNo)
  .then(response => {
    if(response.ok) return response.json();
    throw new Error("일정조회오류" + response.status);
  })
  .then(numList => {
    console.log(numList);
    inputTags.forEach(e=>{
      if(numList.includes( Number(e.value) )) {
        e.checked = true;
      } else {
        e.checked = false;
      }
    })

  })
  .catch(err => console.error(err));
});


// 제출이벤트
const sendBtn = document.querySelector("#sendBtn");
sendBtn?.addEventListener("click", ()=>{
  const notificationContent = document.querySelector('[name="notificationContent"]');

  if(notificationContent.value.trim().length === 0){
    alertM("알림 내용을 입력해 주세요");
    return;
  }

  const inputTags = document.querySelectorAll(".noti-tbody input");
  
  const objList = [];
  let count = 0;

  inputTags.forEach(e=>{

    if(e.checked) {
      const inputObj = {
  
        "notificationContent" : notificationContent.value,
        "notificationUrl" : "/groupMain/" + groupNo,
        "sendMemberNo" : chatLoginMemberNo,
        "receiveMemberNo" :   e.value,
        "groupNo" : groupNo
      }
      objList.push(inputObj);
      count ++;
    }
  })
  
  if(count === 0){
    alertM("알림을 보낼 회원을 1명이상 선택해 주세요");
    return;
  }

  confirmM(count + "명의 회원에게 알림을 보내겠습니까?")
  .then(result => {
    if(!result) return;

    // 컨펌 진행함수 start 
    fetch("notification", {
      method : "POST",
      headers : {"Content-Type" : "application/json"},
      body : JSON.stringify( objList )
    })
    .then(response => {
      if(response.ok) return response.text();
      throw new Error("일정조회오류" + response.status);
    })
    .then(result => {
      if(result > 0 ){
        alertM("총 " + result + "개의 알림을 보내었습니다");
        notificationContent.value = '';
        selectAll.checked = false;
        inputTags.forEach(e=>{
          e.checked = false;
        })
      } else {
        alertM("실패 하였습니다.");
      }
  
    })
    .catch(err => console.error(err));
    
  });// 컨펌 진행함수 end

}) // 알림제출 종료





/********************************************************************** */
/********************************************************************** */


document.addEventListener("DOMContentLoaded", ()=>{

  // 버튼 이벤트추가 함수

  // 모임가입 수락, 거절버튼
  if(inviteAgreeBtns !== undefined){
    btnsAddEvent();
  }
  // 회원강퇴버튼
  if(removeMemberArr !== undefined){
    removeMemberArrBtn();
  }
  // 권한 위임버튼
  if(delegateArr !== undefined){
    delegateArrBtn();
  }
  // 강퇴 취소버튼
  if(backupMemberArr !== undefined){
    backupMemberArrBtn();
  }

  // select태그들 초기설정
  const params = new URLSearchParams( location.search );
  const periodParam = params.get("period");
  const keyParam = params.get("key");
  const queryInput = document.querySelector("#searchQuery");
  
  if(periodParam){
    document.getElementById("searchPeriod").value = periodParam;
  }
  if(keyParam){
    document.querySelector("#searchKey").value = keyParam;
    queryInput.value = params.get("query");
  }

  // 회원정보 검색에서 검색내용 없으면 제출 막기
  const searchForm = document.querySelector(".searchForm form");
  searchForm?.addEventListener("submit", e =>{
    if(queryInput){
      if(queryInput.value.trim().length === 0){
        e.preventDefault();
        alertM("검색어를 입력해 주세요");
        return;
      }
    }
  })

  // 메인칸 높이 지정하기
  const mainSec = document.querySelector('.mainSec');
  const children = Array.from(mainSec.children);
  let heightSum = 0;
  children?.forEach(e => {
    heightSum += e.offsetHeight;
    // 자식 요소의 상단, 하단 마진 가져오기
    const childStyle = getComputedStyle(e);
    heightSum += parseFloat(childStyle.marginTop);
    heightSum += parseFloat(childStyle.marginBottom);
    console.log( e.offsetHeight);
  });
  mainSec.style.height = `${heightSum}px`;

});