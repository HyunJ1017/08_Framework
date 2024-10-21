package edu.kh.project.chatting.service;

import java.util.List;

import edu.kh.project.chatting.dto.ChattingRoom;
import edu.kh.project.chatting.dto.Message;
import edu.kh.project.member.dto.Member;

public interface ChattingService {

	/** 채팅 상대 검색
	 * @param query : 상대 닉네임 또는 이메일
	 * @param loginMember : 로그인한 회원 정보
	 * @return memberList
	 */
	List<Member> selectTarget(String query, int memberNo);

	/** 채팅방 입장
	 * @param targetNo
	 * @param memberNo
	 * @return
	 */
	int chattingEnter(int targetNo, int memberNo);

	/** 로그인 한 회원이 참여한 체팅방 목록 조회하기
	 * @param loginMember : 로그인한 회원
	 * @return chattingRoom List
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	
	/** 채팅방 메시지 모두 조회하기
	 * @param chattingNo
	 * @param memberNo
	 * @return messageList
	 */
	List<Message> selectMessageList(int chattingNo, int memberNo);

	/** 채팅 읽음표시
	 * @param chattingNo
	 * @param loginMember
	 * @return
	 */
	int updateReadFlag(int chattingNo, int memberNo);

	/** 메시지 삽입하기
	 * @param msg
	 * @return result
	 */
	int insertMessage(Message msg);

}
