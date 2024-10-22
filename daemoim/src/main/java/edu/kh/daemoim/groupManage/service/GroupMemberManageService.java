package edu.kh.daemoim.groupManage.service;

import java.util.Map;

public interface GroupMemberManageService {

	/** 모임 강퇴
	 * @param memberNo : 강퇴할 회원번호
	 * @param loginMemberNo : 로그인 한 회원번호
	 * @return 0 || 1
	 */
	int deleteMember(int memberNo, int loginMemberNo, int groupNo);

	/** 회원 닉네임 조회
	 * @param memberNo
	 * @return memberNickname
	 */
	String selectMemberNickname(int memberNo);

	/** 강퇴회원 복구
	 * @param memberNo
	 * @param loginMemberNo
	 * @param groupNo
	 * @return result
	 */
	int backupMember(int memberNo, int loginMemberNo, int groupNo);

	/** 모임가입
	 * @param loginMemberNo
	 * @param map
	 * @return
	 */
	int inviteMember(int loginMemberNo, Map<String, Object> map);

}
