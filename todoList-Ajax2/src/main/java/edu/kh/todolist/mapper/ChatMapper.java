package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.dto.Chatting;

@Mapper
public interface ChatMapper {

	String getPassword(String memberNickname);

	ChatMember selectMember(ChatMember member);

	int insertChat(Chatting chatting);

	List<Chatting> selectChat();

	String selectName(int memberNo);

}
