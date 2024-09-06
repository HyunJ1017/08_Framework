package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.Todo;

@Mapper
public interface TodoMapper {
	
	/**
	 * 리스트 전체조회
	 * @return
	 */
	List<Todo> todoListFullView();

	/**
	 * 완료목표갯수확인
	 * @return
	 */
	int countCompleteCount();

	/** 새 할일 추가
	 * 
	 * @param todo
	 * @return
	 */
	int insertTodo(Todo todo);

	/** 목록 불러오기
	 * 
	 * @param listNo
	 * @return
	 */
	Todo selectTodo(int listNo);
	
	

}
