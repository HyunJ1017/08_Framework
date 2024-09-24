package edu.kh.todolist.service;

import java.util.List;
import java.util.Map;

import edu.kh.todolist.dto.ChatBody;
import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.dto.Chatting;

public interface ChatService {

	boolean checkPassword(ChatMember member);

	ChatMember selectMember(ChatMember member);

	
	int insertChat(Chatting chatting);

	List<ChatBody> selectChat();

	String selectName(int memberNo);

	List<ChatMember> selectAllMember();

	int insertMember(ChatMember member);


}
