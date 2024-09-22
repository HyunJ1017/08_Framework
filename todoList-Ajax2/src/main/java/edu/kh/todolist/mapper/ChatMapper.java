package edu.kh.todolist.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.ChatMember;

@Mapper
public interface ChatMapper {

	String getPassword(String memberNickname);

	ChatMember selectMember(ChatMember member);

}
