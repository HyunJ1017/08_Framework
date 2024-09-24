package edu.kh.project.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.project.member.dto.Member;
import edu.kh.project.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

// 왜 Service 인터페이스 상속 받을까?
// - 팀 프로젝트, 유지보수에 굉장히 도움이 많이되기 때문에!
// + AOP Proxy 적용을 위해서

@Slf4j
@Service // 비즈니스 로직을 처리하는 역할 명시 + Bean 등록(IOC)
public class MemberServiceImpl implements MemberService {
	
	@Autowired // 등록된 Bean 중에서 같은 타입의 Bean을 대입(DI)
	private MemberMapper mapper;
	
	@Autowired // BCrypt 암호화 객체 의존성 주입 받기, =>> common.config.SequrityConfig.java
	private BCryptPasswordEncoder encoder;
	
	/** 비밀번호 암호화
	 * - 하는 이유 : 평문 상태로 비밀번호 저장하면 안됨!
	 * 
	 * - 아주 옛날 방식 : 데이터 -> 암호화 -> "암호화된 데이터" -> 복호화 -> 원본데이터
	 *  -> 해킹 위험도 많이많이높음
	 *  
	 * - 좀덜 옛날 방식 : 데이터를 암호화만 가능(SHA-000)
	 * 										-> 복호화 방법이 제공되지 않음
	 *  -> 마구잡이로 대입해서 만들어진 암호화 데이터 테이블에 뚫림
	 *  
	 * - 요즘 쓰는 방식 : Bcrypt 암호화
	 * 
	 * - 입력된 문자열(비밀번호)에 salt(소금)을 추가한 후 암호화
	 *   -> 암호화 할때마다 결과가 다름
	 *   -> DB에 입력받은 비밀번호를 암호화 해서 넘겨줘도 비교 불가능
	 *   -> Bcrypt가 함께 제공하는 평문, 암호화 데이터 비교 메서드인 matches()를 이용하면 된다! (같으면 true, 다르면 false)
	 *      -> matches() 메서드는 Java에서 작동하는 메서드
	 *      -> DB에 저장된 암호화된 비밀번호를 조회해서 가져와야 한다!
	 */

	// 로그인 서비스
	@Override
	public Member login(String memberEmail, String memberPw) {
		// 1. memberEmail이 일치하는 회원의 정보를 DB에서 조회
		// 2. 이메일(id)이 일치하는 회원 정보가 없을 경우
		// 3. DB에서 조회된 비밀번호와 입력받은 비밀번호가 같은지 확인
		// 4. 로그인 결과 반환
		
		// 암호화 테스트
		// log.debug("memberPw : {}", memberPw);
		log.debug("암호화된 memberPw : {}", encoder.encode(memberPw));
		
		// 1. memberEmail이 일치하는 회원의 정보를 DB에서 조회
		// 		(비밀번호 포함!)
		Member loginMember = mapper.login(memberEmail);
		
		// 2. 이메일(id)이 일치하는 회원 정보가 없을 경우
		if(loginMember == null) return null;
		

		// 3. DB에서 조회된 비밀번호와 입력받은 비밀번호가 같은지 확인
//		log.debug( "비밀번호 일치? : {}", encoder.matches( memberPw, loginMember.getMemberPw() ) );
		if( !encoder.matches( memberPw, loginMember.getMemberPw() ) ) return null;
		
		// 4. 로그인 결과 반환
		return loginMember;
	}

	// 회원가입
	@Override
	public int signUp(Member inputMember) {
		// 1) 비밀번호 암호화(BCrypt)
		// 2) 주소 미입력 시 null로 변경
		// 3) mapper 호출 후 결과 반환
		
		// 1) 비밀번호 암호화(BCrypt)
		String encPw = encoder.encode( inputMember.getMemberPw() );
		inputMember.setMemberPw(encPw);
		
		// 2) 주소 미입력 시 null로 변경
		// 빈칸 입력시 "" , "" , "" 가 합쳐져서 ",,"로 바뀜
		// text타입의 input은 값이 작성되지 않으면 "" 빈칸
		// checkbox,radio가 체크가 안되면 null
		if( inputMember.getMemberAddress().equals(",,") ) {
			inputMember.setMemberAddress(null);
		}
		
		// 3) mapper 호출 후 결과 반환
		return mapper.signUp(inputMember);
	}

	// 이메일 중복 검사
	@Override
	public int emailCheck(String inputEmail) {
		
		return mapper.emailCheck(inputEmail);
	}

	// 닉네임 중복 검사
	@Override
	public int duplication(String inputNickname) {
		
		return mapper.duplication(inputNickname);
	}

}
