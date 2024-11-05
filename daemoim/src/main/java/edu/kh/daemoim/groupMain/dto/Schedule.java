package edu.kh.daemoim.groupMain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

	private int scheduleNo;
	private String location;
	private String scheduleDate;
	private String locationAddress;
	private String cost;
	
	private int groupNo;
	
	private int memberNo;
	
	private int memberCount;
	
	private int dDay;
	
	private int count;
	
	private List<String> memberImages;
	
}
