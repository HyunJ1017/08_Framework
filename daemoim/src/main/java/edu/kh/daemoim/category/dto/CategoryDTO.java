package edu.kh.daemoim.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoryDTO {
	
	private String groupNo;
	private String groupMainImg;
    private String groupName;
    private String groupIntroduce;
    private String categoryName;
    private int currentPopulation; // 현재 해당 모임의 인원
    private int maxPopulation;
}
