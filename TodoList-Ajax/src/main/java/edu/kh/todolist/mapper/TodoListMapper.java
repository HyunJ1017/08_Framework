package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.Todo;

@Mapper // 상속 받은 클래스 생성 후 Bean 등록
public interface TodoListMapper {

	/** 할 일 목록 조회
	 * @return todoList
	 */
	List<Todo> selectTodoList();

	/** 완료된 할 일 개수 조회
	 * @return completeCount
	 */
	int selectCompleteCount();

	/** 할 일 추가
	 * @param todo
	 * @return result
	 */
	int todoAdd(Todo todo);

	/** 할 일 상세조회
	 * @param todoNo
	 * @return todo
	 */
	Todo todoDetail(int todoNo);

	/** 완료 여부 변경
	 * @param todoNo
	 * @return result
	 */
	int todoComplete(int todoNo);

	/** 할 일 수정
	 * @param todo
	 * @return result
	 */
	int todoUpdate(Todo todo);

	/** 할 일 삭제
	 * @param todoNo
	 * @return result
	 */
	int todoDelete(int todoNo);

	String searchTitle(int todoNo);

	
	/** 전체 할 일 개수 조회
	 * @return
	 */
	int getTotalCount();
	
	
	

}