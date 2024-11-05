package edu.kh.daemoim.siteManage.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.siteManage.dto.StopMember;
import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.siteManage.mapper.SiteManageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SiteManageServiceImpl implements SiteManageService {

	private final SiteManageMapper mapper;


	// 사이트매니저 필요정보 얻어오기
	@Override
	public Map<String, Object> getSiteManage() {

		// 총 모임 수
		int groupAllcount = mapper.getGroupAllcount();

		// 총 회원수
		int memberAllcount = mapper.getMemberAllcount();

		// 정지된 계정 수
		int outMemberCount = mapper.getOutMemberCount();

		// 오늘 새로 가입한 회원 수
		int todayEnrollCount = mapper.getTodayEnrollCount();

		// 모임조회 - 전체활동이 많았던 모임 5개
		List<GroupManageDto> groupList = mapper.getActiveGroupList();

		// 회원조회 - 최근 가입한 회원 5개
		List<GroupMemberManageDto> memberList = mapper.getNewMember();

		

		Map<String, Integer> countList = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		countList.put("groupAllcount", groupAllcount);
		countList.put("memberAllcount", memberAllcount);
		countList.put("outMemberCount", outMemberCount);
		countList.put("todayEnrollCount", todayEnrollCount);
		map.put("countList", countList);
		map.put("groupList", groupList);
		map.put("memberList", memberList);

		
		return map;
	}

	// 계정 정지(이메일 확인)

	@Override
	public StopMember findMemberByEmail(String email) {

		return mapper.findMemberByEmail(email);
	}
	// 계정 정지(이메일 확인)
	
	@Override
	public StopMember findMemberByEmail2(String email) {
		
		return mapper.findMemberByEmail2(email);
	}

	// 계정 정지
	@Override

	public int StopMember(StopMember member) {
	    int result = mapper.updateOut(member); // 정지 상태로 업데이트
        if (result == 0) return 0;

        result = mapper.insertStop(member); // 정지 테이블에 추가
        if (result == 0) return 0;

        result = mapper.deleteReport(member); // 신고 목록에서 삭제
        return result;
	}


	// 회원 탈퇴
	@Override
	public int resignMember(StopMember member) {

		int result = 0;

		result = mapper.deleteMember(member);
		
		
		
		return result;
	}

	// 신고 목록 조회
	@Override
	public List<StopMember> getReportList() {
	
		return mapper.getReportList();
	}

	// 모달창
	@Override
	public StopMember getReportDetail(int reportListNo) {

		int result = mapper.updateReportViewStatus(reportListNo);
		System.out.println("result : " + result);
		if(result> 0){
			return mapper.getReportDetail(reportListNo);
		} else {
			return null;
		}
	}

	// 조회여부 변경
	@Override
	public int updateReportViewStatus(int reportListNo) {
	
		return mapper.updateReportViewStatus(reportListNo);
	}

	// 신고목록 삭제
	@Override
	public void deleteReportOut(int reportListNo) {
		mapper.deleteReportOut(reportListNo);
		
	}



	
}
