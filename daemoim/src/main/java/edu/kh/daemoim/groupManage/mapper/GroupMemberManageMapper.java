package edu.kh.daemoim.groupManage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMemberManageMapper {

	// 회원 강퇴
	int deleteMember( @Param("memberNo") int memberNo,
										@Param("groupNo") int groupNo);

	// 모임장 번호 확인
	int getLeaderNo(int groupNo);

	// 회원 닉네임 확인
	String selectMemberNickname(int memberNo);

}
