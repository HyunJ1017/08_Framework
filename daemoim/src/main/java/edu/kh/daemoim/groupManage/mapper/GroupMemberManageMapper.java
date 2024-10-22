package edu.kh.daemoim.groupManage.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMemberManageMapper {

	// 모임장 번호 확인
	int getLeaderNo(int groupNo);

	// 회원 닉네임 확인
	String selectMemberNickname(int memberNo);
	
	// 회원 강퇴
	int deleteMember(
			@Param("memberNo") int memberNo,
			@Param("groupNo") int groupNo);

	// 강퇴회원 복구
	int backupMember(
			@Param("memberNo") int memberNo,
			@Param("groupNo") int groupNo);

	// 회원가입 승인
	int insertMember(Map<String, Object> map);
	// 가입신청내역 지우기
	int deleteInvite(Map<String, Object> map);


}
