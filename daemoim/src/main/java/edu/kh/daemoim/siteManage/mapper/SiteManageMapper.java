package edu.kh.daemoim.siteManage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.siteManage.dto.StopMember;
import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;

@Mapper
public interface SiteManageMapper {
	
	// 그룸 총 모입수 조회
	int getGroupAllcount();

	// 멤버 총 인원수 조회
	int getMemberAllcount();

	// 정지 계정 수 조회
	int getOutMemberCount();

	// 오늘 가입한 회원 수 조회
	int getTodayEnrollCount();

	// 활동이 많은 그룹 조회
	List<GroupManageDto> getActiveGroupList();

	// 최근 가입한 회원 조회
	List<GroupMemberManageDto> getNewMember();

	// 계정 정지
	StopMember findMemberByEmail(String email);
	StopMember findMemberByEmail2(String email);

	// 정지 여부 변경
	int updateOut(StopMember member);

	// 정지 테이블 추가
	int insertStop(StopMember member);

	// 신고목록에서 제거
	int deleteReport(StopMember member);

	// 회원 탈퇴
	int deleteMember(StopMember member);

	// 신고 목록 조회
	List<StopMember> getStopMemberList();
	
	// 신고 목록 조회

	List<StopMember> getReportList();

	// 모달창
	StopMember getReportDetail(int reportListNo);

	// 조회 여부 변경
	int updateReportViewStatus(int reportListNo);

	// 신고목록 삭제
	void deleteReportOut(int reportListNo);



}
