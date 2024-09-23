package edu.kh.todolist.service;

import java.util.List;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.dto.Chatting;

public interface ChatService {

	boolean checkPassword(ChatMember member);

	ChatMember selectMember(ChatMember member);

	
	int insertChat(Chatting chatting);

	List<Chatting> selectChat();

	String selectName(int memberNo);


}
