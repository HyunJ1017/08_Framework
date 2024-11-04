package edu.kh.daemoim.signup.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.signup.mapper.SignupMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	private final SignupMapper mapper;

	// 이메일 체크
	@Override
	public int emailCheck(String email) {
		return mapper.memberEmailCheck(email);
	}
	
	// 아이디 체크
	@Override
	public int memberIdCheck(String memberId) {
		return mapper.memberIdCheck(memberId);
	}

	// 닉네임 체크
	@Override
	public int nicknameCheck(String nickname) {
		return mapper.nicknameCheck(nickname);
	}
}
