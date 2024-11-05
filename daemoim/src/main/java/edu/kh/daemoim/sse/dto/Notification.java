package edu.kh.daemoim.sse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notification {
	
	private int notificationNo;
	private String notificationContent;
	private String notificationCheck;
	private String notificationDate;
	private String notificationUrl;
	private int sendMemberNo;
	private int receiveMemberNo; 

	private int groupNo;
	private String notificationType; 
	private int pkNo;

	private String sendMemberProfileImg;
}
