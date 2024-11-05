package edu.kh.daemoim.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
	
	private int loginMemberNo;
	private int groupNo;
	private String groupName;
	private int unreadCount;
	
}
