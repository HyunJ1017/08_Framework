package edu.kh.project.main.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.main.mapper.MainMapper;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
	
	private final MainMapper mapper;
	
	private final BCryptPasswordEncoder encoder;
	
	// 전체회원조회
	@Override
	public List<Member> selectMemberList() {
		
		return mapper.selectMemberList();
	}
	
	// 빠른 로그인
	@Override
	public Member directLogin(int memberNo) {
		
		return mapper.directLogin(memberNo);
	}
	
	// 비밀번호 초기화
	@Override
	public int resetPw(int memberNo) {
		
		String encPw = encoder.encode("pass01!");
		
		return mapper.resetPw(memberNo, encPw);
	}
	
	// 탈퇴여부 변경
	@Override
	public int changeStatus(int memberNo) {
		
		return mapper.changeStatus(memberNo);
	}

}
