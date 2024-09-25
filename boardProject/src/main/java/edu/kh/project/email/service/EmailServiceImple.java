package edu.kh.project.email.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.common.util.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
//- 필드 중 final(상수) 키워드가 작성된 필드값을 초기화 하기위한 생성자를 자동완성하는 Lombok 제공 어노테이션
//+ final 필드 초기화를 위한 생성자 매개변수에 자동으로 같은 자료형의 Bean을 의존성 주입함 (== DI @Autowired)
// == 요약 : final 필드에 의존성주입하기

/**
 * 이메일 관련 비지니스 로직 처리 클레스
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImple implements EmailService {
	
	/* 의존성 주입 방법 1	@Autowired	public 자료형 필드명
	 */
	/* 의존성 주입 방법 2 : setter 이용
	private 자료형 필드명1;
	@Autowired
	public void set필드명(자료형 변수명) {
		this.필드명 = 변수명;
	}
	 */
	
	/* 의존성 주입 방법 3 : 생성자를 이용하는 방법 (필드 많을 때) @RequiredArgsConstructor
	private 자료형 필드명1;
	private 자료형 필드명2;
	
	@Autowired
	public 클래스명(자료형 변수명1, 자료형 변수명2) {
		this.필드명1 = 변수명1;
		this.필드명2 = 변수명2;
	}
	 */
	
	// 상수형 필드 선언
	
	private final JavaMailSender mailSender; 
	// EmailConfig 내용이 적용된 이메일 발송이 가능한 객체(Bean)
	
	private final RedisUtil redisUtil;
	// Redis(InMemory DB) CRUD 할 수 있는 기능을 제공하는 객체(Bean)
	
	private final SpringTemplateEngine templateEngine;
	// 자바에서 타임리프를 사용할 수 있게하는 객체(Bean)
	// html코드를 Java로 읽어올 수 있음
	
	
	// 이메일 발송
	@Override
	public int sendEmail(String htmlName, String email) {
		// 이메일을 브라우저에서 보게되니까 html코드로 작성해서 보내야하는데
		// java에서 작성하면 어려우니까 타임리프를 이용하게 해주는 개체를 이용해
		// 외부 html 문서를 읽어와 보낼 예정
		
		try {
			
			// 발송할 이메일 제목
			String emailTitle = null;
			
			// 생성된 인증 번호
			String authKey    = createAuthKey();
			
			
			// 이메일 발송 시 사용할 htmlName에 따라
			// 이메일 제목, 내용을 다르게 설정
			switch(htmlName) {
				case "signUp" : 
					emailTitle = "[boadProject] 회원 가입 인증번호 입니다.";
					break;
				case "findPw" :
					emailTitle = "[boadProject] 비밀번호 찾기 인증번호 입니다.";
					break;
			}
			
			/* ----- 메일 발송 ----- */
			
			// MimeMessage : 메일 발송 객체
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			// MimeMessageHelper : Spring제공 메일 발송 도우미
			// 매개변수 1 : MimeMessage
			// 매개변수 2 : 이메일에 파일첨부 여부(true : 첨부함, false : 첨부안함)
			// 매개변수 3 : 발송되는 이메일의 문자 인코딩 지정
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			helper.setTo(email); // 받는 사람 이메일 세팅
			helper.setSubject(emailTitle); // 이메일 제목 세팅
			
			helper.setText( loadHtml(authKey, htmlName) , true); // 이메일 내용 세팅
			// 매개변수 1 : 이메일내용 html 코드
			// 매개변수 2 : HTML 코드 해석 여부 지정(true : 해석함)
			
			// 지정된 HTML 파일에 authKey가 첨부된 후
			// HTML 코드 전체가 하나의 String으로 변환되서 반환 받은 후
			// 변환된 String을 메일 내용으로 세팅
			
			// CID(Content-ID)를 이용해 메일에 이미지 첨부
			helper.addInline("logo", new ClassPathResource("static/images/user.png"));
			
			// 메일 발송하기
			mailSender.send(mimeMessage);
			
			// redis에 이메일, 인증번호 저장(5분 후 만료)
			redisUtil.setValue(email, authKey, 5 * 60 );
			
		} catch (Exception e) {
			// 이메일 오류시 오류페이지 나면 복잡하니까 실패했다고 0 반환할 거임
			e.printStackTrace();
			return 0;
		}
		// 성공시 1 반환
		return 1;
	}
	
	/** 인증번호 생성 (영어 대문자 + 소문자 + 숫자 6자리)
   * @return authKey
   */
  public String createAuthKey() {
  	String key = "";
    for(int i=0 ; i< 6 ; i++) {
        
      int sel1 = (int)(Math.random() * 3); // 0:숫자 / 1,2:영어
      
      if(sel1 == 0) {
        
        int num = (int)(Math.random() * 10); // 0~9
        key += num;
          
      }else {
        
        char ch = (char)(Math.random() * 26 + 65); // A~Z
        
        int sel2 = (int)(Math.random() * 2); // 0:소문자 / 1:대문자
        
        if(sel2 == 0) {
            ch = (char)(ch + ('a' - 'A')); // 대문자로 변경
        }
        
        key += ch;
      }
    }
    return key;
  }
  
  //HTML 파일을 읽어와 String으로 변환 (타임리프 적용)
	public String loadHtml(String authKey, String htmlName) {
		
		// org.tyhmeleaf.Context 선택!!
		Context context = new Context();
		
		//타임리프가 적용된 HTML에서 사용할 값 추가
		context.setVariable("authKey", authKey);
		
		// templates/email 폴더에서 htmlName과 같은 
		// .html 파일 내용을 읽어와 String으로 변환
		return templateEngine.process("email/" + htmlName, context);
		
	}

	// 인증번호 확인
	@Override
	public boolean checkAuthKey(Map<String, String> map) {
		
		String email   = map.get("email");
		String authKey = map.get("authKey");
		
		// 1) Redis에 key가 입력된 email과 같은 데이터가 있는지 확인
		if( redisUtil.hasKey( email ) == false ) {
			// 없을경우
			return false;
		}
		
		// 2) Redis에 같은 key가있다면 value를 얻어와 입력받은 authKey와 비교
		return redisUtil.getValue(email).equals(authKey);
	}

}
