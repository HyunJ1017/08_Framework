package edu.kh.todolist.service;

import java.util.List;

import edu.kh.todolist.dto.Todo;

public interface TodoService {

	/** 전체리스트조회
	 * @return
	 */
	List<Todo> todoListFullView();

	/**완료된 카운드 숫자 확인
	 * 
	 * @return
	 */
	int countCompleteCount();

	/**새 목록 추가
	 * 
	 * @param todo
	 * @return
	 */
	int insertTodo(Todo todo);
	
	/** detail뷰 넘어가기전에 todo받기
	 * 
	 * @param listNo
	 * @return
	 */
	Todo selectTodo(int listNo);


}
