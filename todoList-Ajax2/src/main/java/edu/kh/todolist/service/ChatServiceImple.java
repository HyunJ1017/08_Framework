package edu.kh.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	 * 
	 */
	@Override
	public int insertChat(Chatting chatting) {
		
		return mapper.insertChat(chatting);
	}

	/** 채팅부르기
	 * 
	 */
	@Override
	public List<Chatting> selectChat() {
		
		return mapper.selectChat();
	}

	/**
	 * 닉네임찾기
	 */
	@Override
	public String selectName(int memberNo) {
		
		return mapper.selectName(memberNo);
	}
	

}
