package edu.kh.daemoim.groupManage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GroupMemberManageDto {
	
	private int    memberNo;
	private String memberId;
	private String memberPw;
	private String memberEmail;
	private String memberNickname;
	private String memberTel;
	private char   memberDelFl;
	private String enrollDate;
	private int    authority;
	private String memberImg;
	private String memberIm;
	private char   memberOutFl;

	private String memberGroupEnrollDate;	// 모임 가입일
	private String memberGroupBan;				// 강퇴
	private String memberGroupDelFl;			// 모임탈퇴여부
	
	// "MEMBER_GROUP" 테이블 필드
	private int    groupNo;
	private char   inviteDelFl;
	private String inviteReqTime;
}
