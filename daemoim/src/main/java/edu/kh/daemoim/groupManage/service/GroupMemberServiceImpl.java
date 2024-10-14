package edu.kh.daemoim.groupManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberPagination;
import edu.kh.daemoim.groupManage.mapper.GroupMemberMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

	private final GroupMemberMapper mapper;
	
	// 모임 회원 조회
	@Override
	public Map<String, Object> getMemberList(int groupNo, int cp) {
		
		// 전체멤버 수 조회(DEL_FL = 'N')
		int memberAllCount = mapper.getMemberCount(groupNo);
		
		// 페이지네이션 설정
		GroupMemberPagination pagination =  new GroupMemberPagination(cp, memberAllCount);
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 전체멤버 상세조회
		List<GroupMemberManageDto> memberList = mapper.getMembers(groupNo, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberAllCount", memberAllCount);
		map.put("memberList", memberList);
		map.put("pagination", pagination);
		
		
		return map;
	}
	
}
