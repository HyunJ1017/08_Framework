package edu.kh.daemoim.groupManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.groupManage.dto.ManagePagination;
import edu.kh.daemoim.groupManage.mapper.GroupMemberManageMapper;
import edu.kh.daemoim.groupManage.mapper.GroupMemberMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

	private final GroupMemberMapper mapper;
	private final GroupMemberManageMapper manageMapper;
	
	// 모임 회원 조회
	@Override
	public Map<String, Object> getMemberList(Map<String, Object> paramMap) {
		
		// 전체멤버 수 조회(DEL_FL = 'N')
		int memberAllCount = mapper.getMemberCount((int)paramMap.get("groupNo"));
		
		// 페이지네이션 설정
		int cp = 1;
		if( paramMap.get("cp") != null) cp = Integer.parseInt(paramMap.get("cp").toString());
		ManagePagination pagination =  new ManagePagination(cp, memberAllCount, 20, 10);
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 전체멤버 상세조회
		List<GroupMemberManageDto> memberList = mapper.getMembers(paramMap, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberAllCount", memberAllCount);
		map.put("memberList", memberList);
		map.put("pagination", pagination);
		
		
		return map;
	}
	

	
	// 가입신청관리페이지
	@Override
	public Map<String, Object> getInviteList(Map<String, Object> paramMap) {
		
		// 가입신청 수 조회(DEL_FL = 'N')
		int memberAllCount = mapper.getInviteCount((int)paramMap.get("groupNo"));
		
		// 페이지네이션 설정
		int cp = 1;
		if( paramMap.get("cp") != null) cp = Integer.parseInt(paramMap.get("cp").toString());
		ManagePagination pagination =  new ManagePagination(cp, memberAllCount, 20, 10);
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 전체멤버 상세조회
		List<GroupMemberManageDto> memberList = mapper.getInviteMembers(paramMap, rowBounds);
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberCount", memberAllCount);
		
		// 전체멤버수가 필요해서 다시 조회
		memberAllCount = mapper.getMemberCount((int)paramMap.get("groupNo"));
		
		map.put("memberAllCount", memberAllCount);
		map.put("memberList", memberList);
		map.put("pagination", pagination);
		
			
		return map;
	}
	
	// 그룹 리더 바꾸기
	@Override
	public int changeLeader(GroupManageDto newGroup) {
		int preGroupLeader = manageMapper.getLeaderNo(newGroup.getGroupNo()); 
		int result = mapper.changeLeader(newGroup);
		
		if(result < 1) return result;
		
		// 이전 모임장 권한 내리기
		result = mapper.changeLeaderAuthority(preGroupLeader, 1);
		if(result < 1) return result;
		// 다음 모임장 권한 올리기
		result = mapper.changeLeaderAuthority(newGroup.getMemberNo(), 2);
		return result;
	}
	
	// 차단회원 관리페이지
	@Override
	public Map<String, Object> gotobanManage(Map<String, Object> paramMap) {
		
		/* 그룹정보 group -- 다른서비스에서 가져옴
		 * 차단회원 명수 memberAllCount
		 * 차단회원 리스트 memberList
		 */
		
		// 강퇴멤버 수 조회(BAN_FL = 'Y')
		int memberAllCount = mapper.getBanCount((int)paramMap.get("groupNo"));
		
		// 페이지네이션 설정
		int cp = 1;
		if( paramMap.get("cp") != null) cp = Integer.parseInt(paramMap.get("cp").toString());
		ManagePagination pagination =  new ManagePagination(cp, memberAllCount, 20, 10);
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 차단멤버 상세조회
		List<GroupMemberManageDto> memberList = mapper.getBans(paramMap, rowBounds);
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberCount", memberAllCount);
		
		// 전체멤버수가 필요해서 다시 조회
		memberAllCount = mapper.getMemberCount((int)paramMap.get("groupNo"));
		
		map.put("memberAllCount", memberAllCount);
		map.put("memberList", memberList);
		map.put("pagination", pagination);
		
		return map;
	}
	
}
