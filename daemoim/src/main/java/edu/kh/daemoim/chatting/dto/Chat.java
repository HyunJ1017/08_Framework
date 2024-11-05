package edu.kh.daemoim.chatting.dto;

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
public class Chat {

	 private int chatNo;
	 private String chatContent;
	 private String chatWriteDate;
	 private int chatCount;
	 private int memberNo;
	 private int groupNo;
	 private String memberNickname;
	 private String memberProfileUrl;
}
