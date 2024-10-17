package edu.kh.daemoim.groupManage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;

@Mapper
public interface GroupMemberMapper {

	// 전체멤버 수 조회
	int getMemberCount(int groupNo);

	// 모임멤버 상세조회
	List<GroupMemberManageDto> getMembers(Map<String, Object> paramMap, RowBounds rowBounds);

	// 가입신청멤버 조회
	int getInviteCount(int groupNo);
	List<GroupMemberManageDto> getInviteMembers(Map<String, Object> paramMap, RowBounds rowBounds);

	// 모임리더바꾸기
	int changeLeader(GroupManageDto newGroup);

	// 차단멤버 전체수 조회
	int getBanCount(int groupNo);

	// 차단 멤버리스트 조회
	List<GroupMemberManageDto> getBans(Map<String, Object> paramMap, RowBounds rowBounds);


}
