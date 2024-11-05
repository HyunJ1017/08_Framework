package edu.kh.daemoim.siteManage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class StopMember {


	
	
	
		// DB에 report 테이블
		private int 		memberNo;
		private String 	reportDate;
		private String 	reportDetail;
		private int 		reportNo;
		private String 	reportView;
		private int 		reportListNo;
		
		
		private String memberEmail;
		private int stopTerm;
		private String stopReason;
		private String stopDate;
		
		
}
