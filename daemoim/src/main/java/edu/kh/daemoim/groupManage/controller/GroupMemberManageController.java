package edu.kh.daemoim.groupManage.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.daemoim.groupManage.service.GroupMemberManageService;
import lombok.RequiredArgsConstructor;

/* groupMember 비동기 요청을 관리할 컨트롤러
GET 회원조회
1. 회원 닉네임 조회

회원강퇴 -> 비동기 DELETE
완료

권한위임 -> FOWARD동기, 화면전환해야됨
GROUP 에 있는 MEMBER_NO만 변경하면 됨

회원가입관리(승인y/거절N) -> 비동기 POST
수락여부 y로 변경
MEMBER_GROUP 테이블에 추가
화면 변경

강퇴회원관리 -> PUT
BAN Y, DEL_FL N => BAN N, DLE_FL N
 * */
@RestController
@RequiredArgsConstructor
public class GroupMemberManageController {
	
	private final GroupMemberManageService service;
	
	@GetMapping("groupMember")
	public String selectMemberNickname(
			@RequestParam("memberNo") int memberNo) {
		return service.selectMemberNickname(memberNo);
	}
	
	/** 회원 강퇴
	 * @param memberNo
	 * @param referer
	 * @return
	 */
	@DeleteMapping("groupMember")
	public int removeMember(
			@RequestBody int memberNo,
			@RequestHeader("referer") String referer) {
		
		/* 모임번호 추출하기 */
		//http://localhost/groupMemberManage/2/memberManage
		int groupNo = 0;
		groupNo = Integer.parseInt( referer.split("/")[4] );
		
		/* Session에서 로그인 멤버 얻어오셔야 합니다 */
		int loginMemberNo = 1;
		
		// 모임 강퇴 서비스 호출
		// 삭제할 회원번호, 로그인한 모임장 회원번호
		int result = service.deleteMember(memberNo, loginMemberNo, groupNo);
		
		return result;
	}
}
