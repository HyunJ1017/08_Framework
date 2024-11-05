package edu.kh.daemoim.chatting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.daemoim.chatting.dto.Chat;
import edu.kh.daemoim.chatting.dto.ChatRoom;

@Mapper
public interface ChattingMapper {
	
	// [ws] 마지막 접속일 < 읽음처리 < 현재시간
	int updateChatCount(@Param("memberNo") int memberNo, @Param("groupNo") int groupNo);

	// [ws] 마지막 접속일 업데이트
	int searchChatRoomUser(@Param("memberNo") int memberNo, @Param("groupNo") int groupNo);
	int updateChatContectDate(@Param("memberNo") int memberNo, @Param("groupNo") int groupNo);
	int insertChatContectDate(@Param("memberNo") int memberNo, @Param("groupNo") int groupNo);
	
	// [ws] 전달받은 채팅 입력
	int insertChatting(Chat chat);

	// [ws] 채팅받을 모임의 회원들 얻어오기
	List<Chat> getMemberNos(int groupNo);

	// 채팅방 정보 얻어오기
	List<ChatRoom> getChatInfo(int memberNo);

	// 마지막으로 접속한 그룹번호 불러오기
	int getLastReaded(int memberNo);

	// 채팅방 들어간적이 있는지 확인
	int searchChatRoom(int memberNo);

	// 채팅내역 불러오기
	List<Chat> getContent(int groupNo);


}
