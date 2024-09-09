package edu.kh.todolist.service;

import java.util.List;

import edu.kh.todolist.dto.Sub;
import edu.kh.todolist.dto.Todo;

public interface TodoService {

	/** 전체리스트조회
	 * @return
	 */
	List<Todo> todoListFullView();
	List<Todo> todoListFullViewOder(int ordernum);

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

	/**완료여부 변경
	 * 
	 * @param todo
	 * @return
	 */
	int completeChange(Todo todo);

	/**할일 수정
	 * 
	 * @param todo
	 * @return
	 */
	int updateTodo(Todo todo);

	/**삭제
	 * 
	 * @param listNo
	 * @return
	 */
	int deleteTodo(int listNo);
	
	/** 과제목록 불러오기
	 * 
	 * @param listNo
	 * @return
	 */
	List<Sub> selectSub(int listNo);
	
	/** 과제 추가하기
	 * 
	 * @param sub
	 * @return
	 */
	int insertSub(Sub sub);
	
	/** 제목 주서오기
	 * 
	 * @param todoNo
	 * @return
	 */
	String searchTitle(int todoNo);
	


}
