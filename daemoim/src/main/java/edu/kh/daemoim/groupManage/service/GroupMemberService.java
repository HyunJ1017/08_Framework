package edu.kh.daemoim.groupManage.service;

import java.util.Map;


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


}
