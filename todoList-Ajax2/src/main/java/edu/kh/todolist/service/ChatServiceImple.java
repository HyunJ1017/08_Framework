package edu.kh.todolist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todolist.dto.ChatBody;
import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.dto.Chatting;
import edu.kh.todolist.mapper.ChatMapper;

@Transactional
@Service
public class ChatServiceImple implements ChatService {
	
	@Autowired
	ChatMapper mapper;

	@Override
	public boolean checkPassword(ChatMember member) {
		
		String getPassword = mapper.getPassword(member.getMemberNickname());
		if( ( member.getMemberPassword() ).equals(getPassword)) return true;
		return false;
	}

	@Override
	public ChatMember selectMember(ChatMember member) {
		return mapper.selectMember(member);
	}

	/** 채팅입력
	 * @param chatting : 전달받은 멤버의 멤버번호, 채팅내용
	 * @return 0 || 1
	 */
	@Override
	public int insertChat(Chatting chatting) {
		
		return mapper.insertChat(chatting);
	}

	/** 채팅창 불러오기
	 * @return 채팅 전체목록
	 */
	@Override
	public List<ChatBody> selectChat() {
		
		List<Chatting> chatList = mapper.selectChat();
		List<ChatBody> chatbodyList = new ArrayList<>();
		
		for( Chatting chat : chatList) {
			String memberName = mapper.selectName( chat.getMemberNo() );
			String chatBody = chat.getChatBody();
			String creationDate = chat.getCreationDate();
			chatbodyList.add( new ChatBody(memberName, chatBody, creationDate) );
		}
		
		return chatbodyList;
	}

	/**넥네임 찾기
	 * @param memberNo : 전달받은 PK번호
	 */
	@Override
	public String selectName(int memberNo) {
		
		return mapper.selectName(memberNo);
	}

	
	/** 몽땅 부르기
	 *
	 */
	@Override
	public List<ChatMember> selectAllMember() {
		
		return mapper.selectAllMember();
	}

	/** 맴버추가
	 * 
	 */
	@Override
	public int insertMember(ChatMember member) {
		
		return mapper.insertMember(member);
	}
	

}
