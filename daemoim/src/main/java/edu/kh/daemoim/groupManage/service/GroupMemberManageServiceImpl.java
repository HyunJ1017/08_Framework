package edu.kh.daemoim.groupManage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupManage.mapper.GroupMemberManageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMemberManageServiceImpl implements GroupMemberManageService{
	
	private final GroupMemberManageMapper mapper;
	
	// 닉네임 조회
	@Override
	public String selectMemberNickname(int memberNo) {
		
		return mapper.selectMemberNickname(memberNo);
	}
	
	// 회원 강퇴
	@Override
	public int deleteMember(int memberNo, int loginMemberNo, int groupNo) {
		
		// 로그인 한 회원이 해당 모임장인지 확인
		int leaderNo = mapper.getLeaderNo(groupNo);
		if(leaderNo != loginMemberNo ) return 3;	// 모임장 불일치 return;
		
		// 멤버 강퇴 매퍼 호출
		// 자진 탈퇴 X, 밴 O
		return mapper.deleteMember(memberNo, groupNo);
	}

}
