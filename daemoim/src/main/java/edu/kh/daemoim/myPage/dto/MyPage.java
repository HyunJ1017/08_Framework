package edu.kh.daemoim.myPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MyPage {

	private int memberNo;
	private String memberEmail;
	private String memberIm;
	private String memberKind;
	private String memberId;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberImg;
	private String enrollDate;
	private String memberDelFl;
	private int authority;
	
	private int groupNo;
	private int boardTypeCode;
	private int boardNo;

}
