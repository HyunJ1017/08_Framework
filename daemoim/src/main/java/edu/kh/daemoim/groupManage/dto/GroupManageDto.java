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
@ToString
@Builder
public class GroupManageDto {
	private int    groupNo;
	private String groupName;
	private String groupDate;
	private String groupDelFl;
	private String groupIntroduce;
	private int    categoryNo;
	private int    categoryListNo;
	private int    memberNo;
	private String groupMainImg;		// 모임 대표이미지 요청경로 + 이름
	private String groupHeaderImg;	// 모임 상단이미지 요청경로 + 이름

	private String categoryName;
	private String categoryListName;
	
	
}
