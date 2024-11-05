package edu.kh.daemoim.signup.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;


import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.common.util.RedisUtil;
import edu.kh.daemoim.signup.mapper.SignupMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	private final SignupMapper mapper;

	private final BCryptPasswordEncoder encoder;
	
	@Override
	public int emailCheck(String email) {
		return mapper.emailCheck(email);
	}
	
	// 아이디 체크
	@Override
	public int memberIdCheck(String memberId) {
		return mapper.memberIdCheck(memberId);
	}

	
	// 전화번호 체크
	@Override
	public int telCheck(String tel) {
		return mapper.telCheck(tel);
	}
	
	// 닉네임 체크
	@Override
	public int nicknameCheck(String nickname) {
		return mapper.nicknameCheck(nickname);
	}

	// 마지막 가입 신청
	@Override
	public int signUp(MyPage inputMember) {
		//  비밀번호 암호화
		String encPw = encoder.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
	
	return mapper.signUp(inputMember);
}



}
