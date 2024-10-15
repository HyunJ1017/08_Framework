package edu.kh.daemoim.groupManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryList {
	
	private int categoryListNo;
	private String categoryListName;
	private int categoryNo;
}
