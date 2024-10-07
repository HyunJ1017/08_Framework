package edu.kh.daemoim.groupManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManageCategory {
	
	// 카테고리 정보를 다루기 위한 객체
	
	private int CategoryNo;						// 카테고리 번호
	private String CategoryName;			// 카테고리 이름
	private String CategoryImg;				// 카테고리 이미지
	private int CategoryListNo;				// 카테고리 리스트 번호
	private String CategoryListName;	// 카테고리 리스트 이름
}
