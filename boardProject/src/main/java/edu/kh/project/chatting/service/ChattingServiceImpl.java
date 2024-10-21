package edu.kh.project.chatting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.chatting.dto.ChattingRoom;
import edu.kh.project.chatting.dto.Message;
import edu.kh.project.chatting.mapper.ChattingMapper;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService{
	
	private final ChattingMapper mapper;


	// 채팅 상대 검색
	@Override
	public List<Member> selectTarget(String query, int memberNo) {
		return mapper.selectTarget(query, memberNo);
	}


	// 채팅방 입장
	@Override
	public int chattingEnter(int targetNo, int memberNo) {
		
		// 두 회원이 참여한 채팅방이 존재하는지 확인
		int chattingNo = mapper.checkChattingRoom(targetNo, memberNo);
		
		// 두 회원이 참여한 채팅방이 없을 경우
		if(chattingNo == 0) {
			Map<String, Integer> map = new HashMap<>();
			map.put("targetNo", targetNo);
			map.put("memberNo", memberNo);
			
			/* 갑자기 Map을 사용하는 이유
			 * 
			 * - insert 시 새로 생성되는 CHATTING_ROOM_NO 값을
			 * 	 MAP에 담아서 얻어오기 위해!
			 *   (useGeneratedKeys=true, <selectKey>)
			 */
			int result = mapper.createChattingRoom(map);
			chattingNo = (int)map.get("chattingNo");
		}
		
		return chattingNo;
	}

	// 로그인한 회원의 채팅목록 조회
	@Override
	public List<ChattingRoom> selectRoomList(int memberNo) {
		return mapper.selectRoomList(memberNo);
	}
	
	// 특정 채팅방의 메시지 조회하기
	@Override
	public List<Message> selectMessageList(int chattingNo, int memberNo) {
		List<Message> messageList = mapper.selectMessageList(chattingNo);
		
		if(messageList.isEmpty() == false) {
			// 조회된 목록이 있을경우
			// 나말고 상대방 메시지 읽음처리하기
			int result = mapper.updateReadFlag(chattingNo, memberNo);
		}
		
		return messageList;
	}
	
	// 채팅 읽음표시
	@Override
	public int updateReadFlag(int chattingNo, int memberNo) {
		return mapper.updateReadFlag(chattingNo, memberNo);
	}
	
	// 메시지 삽입하기
	@Override
	public int insertMessage(Message msg) {
		return mapper.insertMessage(msg);
	}
}
