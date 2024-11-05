package edu.kh.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kh.api.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:/config.properties")
@Slf4j // Logger log 필드 자동생성(Lombok 제공)
@RequestMapping
@Controller
public class MainController {
	
	// 서비스 키 얻어오기
	@Value("${api.serviceKey.encoding}")
	private String encodingServiceKey;
	@Value("${api.serviceKey.decoding}")
	private String decodingServiceKey;
	
	@GetMapping("/")
	public String mainPage(
			@RequestParam(name="sidoName",
										required = false,
										defaultValue = "서울") String sidoName,
			Model model) {
		
		/* Server에서 공공데이터 요청하기 */
		
		// 시도별 미세먼지 농도 조회 요청주소
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		// 파라미터 : serviceKey, sidoName, returnType, numOfRows, pageNo, ver
		String returnType = "json";
		int numOfRows = 1;
		int pageNo = 1;
		String ver = "1.0";
		
		/* UriComponentsBuilder
		 * - Spring에서 제공하는 URI 관련 객체
		 * - 주소 + 쿼리스트링 조합을 쉽게 할 수 있는 기능을 제공함
		 * - 자동으로 URL 인코딩 처리하여 안전하게 요청 가능
		 * - 자동으로 url + 쿼리스트링이 합쳐진 주소가 생성
		 * - {}는  url 인코딩이 안됨 + ...
		 */
		UriComponentsBuilder uriBuilder
			= UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("serviceKey", "{decodingServiceKey}")
				.queryParam("sidoName", sidoName)
				.queryParam("returnType", returnType)
				.queryParam("numOfRows", numOfRows)
				.queryParam("pageNo", pageNo)
				.queryParam("ver", ver);
		
		// UriComponentsBuilder uri -> String uri
		String uriString = uriBuilder.build().toUriString();
		log.debug("uriString : {}", uriString);
		
		/* - 인코딩 2중으로 되면 안되서 여기서 다시 바꿔줌 */
		uriString = uriString.replace("{decodingServiceKey}", decodingServiceKey);
		log.debug("decodingServiceKey : {}", uriString);
		
		
		
		// 요청해더(sping)
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		
		// Http 요청 해더와 바디를 묶어주는 객체(sping)
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		// 스프링 제공 Http 클라이언트 (sping)
		// 스프링에서 외부로 http요청을 보내고 응답을 받는 역할의 객체
		// => 스트림 통신을 간단하게 만듬
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			ResponseEntity<String> response
				= restTemplate.exchange(
						uriString,	// 요청주소 + 쿼리스트링(여기서 2번째 url 인코딩 수행)
						HttpMethod.GET, // GET 방식 요청
						entity, // Http 헤더, 바디
						String.class); // 응답 데이터의 형태 (위에서 json으로 해서 json형태의 문자열로 받을거임)
			
			String responseBody = response.getBody();
			log.debug("responseBody : {}", responseBody);
			
			
			/* 전달받은 json 자료형을 java에서 활용할 수 있게 변형 */
			// Jackson
			// -> Java에서 JSON을 다룰 수 있게 해주는 라이브러리
			
			// Jackson data-bind
			// -> JSON 데이터를 Java 객체(Map,Dto)로 변환할 수 있는 라이브러리
			
			// spring-boot-starter-web dependency에 Jackson data-bind 포함되어있음
			
			// JSON을 쉽게 다루거나 변환할 수 있는 객체
			ObjectMapper objectMapper = new ObjectMapper();
			
			// json 표기법으로 작성된 문자열을 노드트리 형태로 바꿈
			JsonNode rootNode = objectMapper.readTree(responseBody);
			
			// (JS) const item = data.response.body.items[0];
			JsonNode itemNode = rootNode.path("response").path("body").path("items");
			
			List<ItemDto> items
				= objectMapper.readValue(
						itemNode.toString(), // items JSON 반환(문자열)
						new TypeReference<List<ItemDto>>() {});
			// items JSON 데이터를 읽어올 때
			// List<ItemDto>를 참조(Reference)해서 형변환 
			
			log.debug("items.get(0) : {}", items.get(0));
			
			// model.addAttribute("item", items.get(0));
			
			/* 데이터 가공 */
			String[] gradeEmoji = {"😄", "🙂", "😷", "🤢"};
			String[] gradeText = {"좋음", "보통", "나쁨", "매우나쁨"};
			
			ItemDto item = items.get(0);
			model.addAttribute("pm10Grade", gradeEmoji[item.getPm10Grade() -1]);
			model.addAttribute("pm10GradeText", gradeText[item.getPm10Grade() -1]);
			model.addAttribute("pm10Value",
					String.format("미세먼지 농도 : %s ㎍/㎥", item.getPm10Value()) );
			model.addAttribute("pm25Grade", gradeEmoji[item.getPm25Grade() -1]);
			model.addAttribute("pm25GradeText", gradeText[item.getPm25Grade() -1]);
			model.addAttribute("pm25Value", 
					String.format("초미세먼지 농도 : %s ㎍/㎥", item.getPm25Value()) );
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 해석된 HTML 코드가 클라이언트에게 전달(응답)
		return "main";
	}
	
	/* ResponseEntity : @ResponseBody + 응답상태코드
	 * 
	 * - HTTP 응답을 표현할 수 있는 객체
	 * - 비동기 응답 서비스(RESTful API)에서 유용하게 사용
	 * 
	 * ? : 아무 자료형이나 들어갈 수 있음
	 */
	@PostMapping("getServiceKey")
	public ResponseEntity<?> getServiceKey(){
		try {
			return new ResponseEntity<String>(encodingServiceKey, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>("에러", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
}
