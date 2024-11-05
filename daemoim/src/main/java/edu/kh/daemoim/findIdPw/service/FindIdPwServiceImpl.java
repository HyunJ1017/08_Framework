package edu.kh.daemoim.findIdPw.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.findIdPw.mapper.FindIdPwMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FindIdPwServiceImpl implements FindIdPwService{

	private final FindIdPwMapper mapper;
	
	@Override
	public int findIdEmailcheck(String email) {
		
		return mapper.findIdEmailcheck(email);
	}
	@Override
	public int findPwEmailcheck(String id, String email) {
		
		return mapper.findPwEmailcheck(id,email);
	}
	
	@Override
	public String memberId(String email) {
		
		return mapper.memberId(email);
	}
	
	private final BCryptPasswordEncoder encoder ;
	
	@Override
	public int chagePwAuthKeyPage(String findPwMemberId, String newPw) {

					String encPw = encoder.encode(newPw);
					
					// 3) DB 비밀번호 변경(회원 번호, 암호화된 새 비밀번호)
					return mapper.chagePwAuthKeyPage(findPwMemberId, encPw);
	}
}