package edu.kh.project.chatting.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.chatting.dto.ChattingRoom;
import edu.kh.project.chatting.dto.Message;
import edu.kh.project.member.dto.Member;

@Mapper
public interface ChattingMapper {

	// 채팅 상대 검색
	List<Member> selectTarget(
			@Param("query") String query,
			@Param("memberNo") int memberNo);

	// 두 회원이 참여한 채팅방이 존재하는지 확인
	int checkChattingRoom(@Param("targetNo") int targetNo, @Param("memberNo") int memberNo);
	// 채팅방 생성
	int createChattingRoom(Map<String, Integer> map);

	
	// 로그인한 회원의 체팅방 목록 조회
	List<ChattingRoom> selectRoomList(int memberNo);

	
	// 채팅방 메시지 조회하기
	List<Message> selectMessageList(int chattingNo);
	// 채팅방 메시지 읽음처리
	int updateReadFlag(@Param("chattingNo") int chattingNo, @Param("memberNo") int memberNo);

	// 메시지 삽입하기
	int insertMessage(Message msg);

}
