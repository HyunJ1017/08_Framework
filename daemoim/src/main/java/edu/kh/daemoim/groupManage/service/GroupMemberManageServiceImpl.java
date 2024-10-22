package edu.kh.daemoim.groupManage.service;

import java.util.Map;

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
		// 탈퇴0 , 밴 O
		return mapper.deleteMember(memberNo, groupNo);
	}
	
	
	
	// 강퇴맴버 복구
	@Override
	public int backupMember(int memberNo, int loginMemberNo, int groupNo) {
			
		// 로그인 한 회원이 해당 모임장인지 확인
		int leaderNo = mapper.getLeaderNo(groupNo);
		if(leaderNo != loginMemberNo ) return 3;	// 모임장 불일치 return;
		
		// 매퍼호출
		return mapper.backupMember(memberNo, groupNo);
	}
	
	
	
	// 모임가입
	@Override
	public int inviteMember(int loginMemberNo, Map<String, Object> map) {
		
		// 로그인 한 회원이 해당 모임장인지 확인
		int leaderNo = mapper.getLeaderNo( (int)map.get("groupNo") );
		if(leaderNo != loginMemberNo ) return 3;	// 모임장 불일치 return;
		
		int result = 0;
		
		// inviteDelFl == 'Y' 면 승인 'N'면 거절
		if( map.get("inviteDelFl").toString().equals("Y") ) {
			
			// MEMBER_GROUP 테이블에 회원 추가
			result = mapper.insertMember( map );
		}
		
		// INVITE 테이블에서 신청내역 지우기
		result = mapper.deleteInvite(map);
			
		return result;
	}

}
