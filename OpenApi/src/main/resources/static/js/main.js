// 비동기로 공공데이터 - 시도별 미세먼지 정보 조회
const getAirQualityFunction = async (cityName) =>{
  
  // 서비스키를 서버에 비동기 POST 방식으로 요청해서 가져오기(숨기기)
  // -> 비동기는 코드수행을 기다리지 않고 다음코드를 수행함
  // -> 서비스키를 얻어오지 못한 상태에서 공공데이터를 요청하는 문제 발생
  
  // * 해결방법 : 비동기 -> 동기식 요청으로 변경 (async/await)
  /*
    async : 비동기 요청 코드가 포함된 함수에 작성하는 키워드
      - 이 키워드가 붙은 함수는 항상 Promise 객체를 반환함
        (Promise : 비동기 데이터가 돌아올 것을 양속하는 객체)
      - async가 포함된 함수에서 명시적으로 Promise 객체 반환구문을 작성하지 않을 경우
        자동으로 Promise.resolve() 형태로 반환된다.
        (약속했던 결과가 돌아옴 == Promise.resolve())
        (fetch API에서 첫 번째 then에 사용되는 매개변수 response)

    await : 비동기 요청을 동기식 요청으로 변환
      - async가 작성된 함수 내에서만 사용 가능
      - await 키워드가 붙은 함수가 종료될 때 까지 코드해석을 일시중지
        (동기식)
  */

  // /getServiceKey 요청에 대한 응답이 돌아올 때 까지 대기(await)
  // 응답이 돌아오면 respose1 변수에 응답 결과(첫번째 then의 response)가 저장됨
  const response1 = await fetch('/getServiceKey', { method : "POST" });

  // 응답데이터를 text 형태로 파싱 될 때까지 대기(await)
  // -> seviceKey에 서버에서 조회된 값이 저장됨
  const serviceKey = await response1.text();
  // console.log(serviceKey);

  let url = 'http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty';
  url += `?serviceKey=${serviceKey}`;
  url += `&sidoName=${cityName}`;
  url += `&returnType=json`;
  url += `&numOfRows=1`;
  url += `&pageNo=1`;
  url += `&ver=1.0`;

  /*
  fetch(url)
  .then(response => {
    if(!response.ok) throw new Error("공공데이터 조회 실패");
    return response.json();
    })
  .then(data => {
    // console.log(data);

    const item = data.response.body.items[0];
    // console.log(item);
    console.log(`미세먼지 농도 : ${item['pm10Value']} ㎍/㎥`);
    console.log(`미세먼지 등급 : ${item['pm10Grade']}`);
    
    console.log(`초미세먼지 농도 : ${item['pm25Value']} ㎍/㎥`);
    console.log(`초미세먼지 등급 : ${item['pm25Grade']}`);
  })
  .catch(err => console.error(err));
  */

  // fetch를 동기식으로 변경해서 짧게 작성하기
  try {
    const response2 = await fetch(url);
    if(!response2.ok) throw new Error("공공 데이터 조회실패 : " + response2.status); // 파싱은 안기다려서 swait 붙임
    const data = await response2.json();

    const item = data.response.body.items[0];
    
    console.log(`미세먼지 농도 : ${item['pm10Value']} ㎍/㎥`);
    console.log(`미세먼지 등급 : ${item['pm10Grade']}`);
    
    console.log(`초미세먼지 농도 : ${item['pm25Value']} ㎍/㎥`);
    console.log(`초미세먼지 등급 : ${item['pm25Grade']}`);

    // 이모지/등급 배열
    const gredeEmoji = ['😄', '🙂', '😷', '🤢'];
    const gredeText = ['좋음', '보통', '나쁨', '매우나쁨'];
    
    const resultTdArr = document.querySelectorAll(".resultTd");
    resultTdArr[0].innerText = `${item['pm10Value']} ㎍/㎥`;
    resultTdArr[1].innerText = gredeText[item.pm10Grade - 1];
    resultTdArr[2].innerText = gredeEmoji[item.pm10Grade - 1];
    resultTdArr[3].innerText = `${item['pm25Value']} ㎍/㎥`;
    resultTdArr[4].innerText = gredeText[item.pm25Grade - 1];
    resultTdArr[5].innerText = gredeEmoji[item.pm25Grade - 1];

  } catch(err){
    console.error(err);
  }

}; // function end

document.addEventListener("DOMContentLoaded", ()=>{
  const dustSearch = document.querySelector("#dustSearch");
  dustSearch?.addEventListener("click", ()=>{
    const sidoName = document.getElementById("sidoName").value;
    getAirQualityFunction(sidoName);
  });
})



//-------------------------------------------------------------------------

document.querySelector("#selectBtn2").addEventListener("click", ()=>{

  const cityName = document.querySelector("#cityName2").value;
  location.href = location.pathname + "?sidoName=" + cityName;
});