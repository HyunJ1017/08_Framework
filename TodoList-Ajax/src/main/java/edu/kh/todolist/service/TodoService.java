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
	
	/**전체갯수확인
	 * 
	 * @return
	 */
	int totalCount();

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
	int completeChange(int listNo);

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
	
	
	/** 제목 주서오기
	 * 
	 * @param todoNo
	 * @return
	 */
	String searchTitle(int todoNo);
	
	/****************************************************/
	
	/** 과제목록 불러오기
	 * 
	 * @param listNo : 조회할 과제들의 할일 번호
	 * @return
	 */
	List<Sub> selectAllSub(int listNo);
	
	/** 과제 1개 불러오기
	 * 
	 * @param subjectNo : 조회할 과제번
	 * @return
	 */
	Sub selectSub(int subjectNo);
	
	/** 과제 추가하기
	 * 
	 * @param sub
	 * @return
	 */
	int insertSub(Sub sub);
	
	/** 과제 완료여부 변경
	 * 
	 * @param sub : subjectNo 변경할 과제번호, complete 수정될 완료상태
	 * @return
	 */
	int completeChangeSub(Sub sub);
	
	/** 과제 수정
	 * 
	 * @param todo
	 * @return
	 */
	int updateSub(Sub sub);

	/** 과제 삭제
	 * 
	 * @param listNo
	 * @return
	 */
	int deleteSub(int subjectNo);



}
