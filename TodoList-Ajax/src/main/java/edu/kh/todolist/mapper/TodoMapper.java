package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.Sub;
import edu.kh.todolist.dto.Todo;

@Mapper
public interface TodoMapper {
	
	/**
	 * 리스트 전체조회
	 * @return
	 */
	List<Todo> todoListFullView();
	List<Todo> todoListFullViewComASC();
	List<Todo> todoListFullViewComDESC();
	
	/** 전체갯수확인
	 * 
	 * @return
	 */
	int totalCount();

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
	
	/**완료여부변경
	 * 
	 * @param todo
	 * @return
	 */
	int completeChange(int listNo);

	/**
	 * 할일 수정
	 * @param todo
	 * @return
	 */
	int updateTodo(Todo todo);

	/**삭제
	 * 
	 * @param listNo
	 * @return
	 */
	int deleteUser(int listNo);
	
	/** 제목 줏어오기
	 * 
	 * @param todoNo
	 * @return
	 */
	String searchTitle(int todoNo);
	
	/**************************************************/
	
	/** 과제목록 불러오기
	 * 
	 * @param listNo
	 * @return
	 */
	List<Sub> selectAllSub(int listNo);
	
	/** 과제 1개 불러오기
	 * 
	 * @param subjectNo : 조회할 과제번호
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
	 * @param sub
	 * @return
	 */
	int updateSub(Sub sub);
	
	/** 과제 삭제
	 * 
	 * @param subjectNo
	 * @return
	 */
	int deleteSub(int subjectNo);
	
	

	


}
