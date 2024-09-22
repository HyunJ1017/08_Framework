package edu.kh.todolist.service;

import edu.kh.todolist.dto.ChatMember;

public interface ChatService {

	boolean checkPassword(ChatMember member);

	ChatMember selectMember(ChatMember member);

}
