package edu.kh.daemoim.groupManage.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.daemoim.groupManage.service.GroupMemberManageService;
import lombok.RequiredArgsConstructor;

/* groupMember 비동기 요청을 관리할 컨트롤러
GET 회원조회
1. 회원 닉네임 조회


권한위임 -> FOWARD동기, 화면전환해야됨
GROUP 에 있는 MEMBER_NO만 변경하면 됨
- 이름만 get ajax로 확인후에
- 실제작업은 포워드로 다시받기

회원가입관리(승인y/거절N) -> 비동기 POST
INVITE 테이블에서 삭제
MEMBER_GROUP 테이블에 추가
화면 변경

강퇴회원관리,강퇴 -> delete
BAN N, DLE_FL N => BAN Y, DEL_FL Y
강퇴회원관리, 복구 -> PUT
BAN Y, DEL_FL Y => BAN N, DLE_FL N
 * */
@RequestMapping("groupMember")
@RestController
@RequiredArgsConstructor
public class GroupMemberManageController {
	
	private final GroupMemberManageService service;
	
	/** 멤버 닉네임 불러오기
	 * - 모임장 권한 이임전 확인용도
	 * @param memberNo
	 * @return
	 */
	@GetMapping("")
	public String selectMemberNickname(
			@RequestParam("memberNo") int memberNo) {
		return service.selectMemberNickname(memberNo);
	}
	
	/** 회원 강퇴
	 * @param memberNo
	 * @param referer
	 * @return
	 */
	@DeleteMapping("")
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
	
	
	
	/** 강퇴회원 복구
	 * @param memberNo
	 * @param referer
	 * @return
	 */
	@PutMapping("")
	public int backupMember(
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
		int result = service.backupMember(memberNo, loginMemberNo, groupNo);
		
		return result;
	}
	
	
	/** 모임가입 승인, 거절
	 * @param map 모임번호, 신청한 멤버번호, 승인Y,거절N 가 담긴 map
	 * @return result
	 */
	@PostMapping("")
	public int inviteMember(
			@RequestBody Map<String, Object> map) {
		
		/* Session에서 로그인 멤버 얻어오셔야 합니다 */
		int loginMemberNo = 1;
		
		// 서비스 호출
		return service.inviteMember(loginMemberNo, map);
	}
	
}
