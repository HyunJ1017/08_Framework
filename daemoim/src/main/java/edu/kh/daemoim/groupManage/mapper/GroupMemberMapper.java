package edu.kh.daemoim.groupManage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;

@Mapper
public interface GroupMemberMapper {

	// 전체멤버 수 조회
	int getMemberCount(int groupNo);

	// 모임멤버 상세조회
	List<GroupMemberManageDto> getMembers(int groupNo, RowBounds rowBounds);


}
