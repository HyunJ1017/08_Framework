package edu.kh.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.mapper.ChatMapper;

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

}
