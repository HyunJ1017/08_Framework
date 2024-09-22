package edu.kh.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMember {
	private int    memberNo;
	private String memberNickname;
	private String memberPassword;
	private String regDate;
}
