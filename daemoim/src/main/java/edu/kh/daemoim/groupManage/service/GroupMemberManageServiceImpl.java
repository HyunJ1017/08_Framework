package edu.kh.daemoim.groupManage.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.groupManage.mapper.GroupManageMapper;
import edu.kh.daemoim.groupManage.mapper.GroupMemberManageMapper;
import edu.kh.daemoim.groupManage.mapper.GroupMemberMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMemberManageServiceImpl implements GroupMemberManageService{
	
	private final GroupMemberManageMapper mapper;
	private final GroupManageMapper groupManageMapper;
	
	
	
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
		GroupManageDto group = groupManageMapper.selectGroup( (int)map.get("groupNo") );
		if(group.getMemberNo() != loginMemberNo ) return 3;	// 모임장 불일치 return;
		
		// MEMBER_GROUP테이블에 이미 있는인원인지 확인
		GroupMemberManageDto searchResult = mapper.searchMember( map );
		if(searchResult != null) {
			// 가입내역이 존재하는 인원이라면
			
			if(searchResult.getMemberGroupBan().toString().equals("Y")) return 4; // 강퇴회원 return;
			
			// 재가입 시키고 결과반환
			if(searchResult.getMemberGroupDelFl().toString().equals("Y")) return mapper.updateMemberGroup(map);
			
			// 벤, 탈퇴 둘다 N이면 가입되어있는 회원임
			return 5;
		}
		
		int result = 0;
		
		// inviteDelFl == 'Y' 면 승인 'N'면 거절
		if( map.get("inviteDelFl").toString().equals("Y") ) {

			// 만약 현재 모임인원이 최대인원이라면 그대로 리턴
			int population = mapper.checkPopulation( (int)map.get("groupNo") );
			if(population > group.getGroupMaxPopulation()) return 2;
			
			// MEMBER_GROUP 테이블에 회원 추가
			result = mapper.insertMember( map );
		}
		
		// INVITE 테이블에서 신청내역 지우기
		result = mapper.deleteInvite(map);
			
		return result;
	}

}
