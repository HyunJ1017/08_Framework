package edu.kh.daemoim.chatting.service;

import java.util.List;
import java.util.Map;

import edu.kh.daemoim.chatting.dto.Chat;

public interface ChattingService {

	// [ws] 전달받은 채팅 입력
	int insertChatting(Chat chat);

	// [ws] 채팅받을 모임의 회원들 얻어오기
	List<Chat> getMemberNos(int groupNo);

	// 채팅내용 불러오기
	List<Chat> getContent(int groupNo, int memberNo);

	// 채팅룸 정보 가져가기
	Map<String, Object> getChatInfo(int memberNo);

}
