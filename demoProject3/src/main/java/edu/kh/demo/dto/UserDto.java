package edu.kh.demo.dto;

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
public class UserDto {
	
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String enrollDate;
	
}
