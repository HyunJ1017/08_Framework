package edu.kh.daemoim.groupManage.service;

import java.util.List;
import java.util.Map;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;


public interface GroupMemberService {

	/** 모임멤버조회
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> getMemberList(Map<String, Object> paramMap);
	

	/** 가입신청목록 조회
	 * @param paramMap
	 * @return map
	 */
	Map<String, Object> getInviteList(Map<String, Object> paramMap);

	/** 그룹리더바꾸기
	 * @param newGroup
	 * @return
	 */
	int changeLeader(GroupManageDto newGroup);

	/** 차단회원 관리페이지
	 * @param groupNo
	 * @return
	 */
	Map<String, Object> gotobanManage(Map<String, Object> paramMap);



}
