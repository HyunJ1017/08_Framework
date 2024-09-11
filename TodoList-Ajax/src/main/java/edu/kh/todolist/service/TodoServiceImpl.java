package edu.kh.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todolist.dto.Sub;
import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.mapper.TodoMapper;


@Transactional	// 내부 메서드 수행 후 트랜잭션 처리 수행
								// - 예외 발생시 rollback, 아님 commit
@Service
public class TodoServiceImpl implements TodoService {
	
	@Autowired // 등록된 bean중 같은 타입을 얻어와 대입(DI)
	private TodoMapper mapper;

	
	/**
	 * 전체리스트조회
	 */
	@Override
	public List<Todo> todoListFullView() {
		return mapper.todoListFullView();
	}
	
	@Override
	public List<Todo> todoListFullViewOder(int ordernum) {
		
		if (ordernum == 2) {//asc
			return mapper.todoListFullViewComASC();
		}
		return mapper.todoListFullViewComDESC();
	}

	/** 전체갯수확인
	 * 
	 */
	@Override
	public int totalCount() {
		
		return mapper.totalCount();
	}
	
	/**
	 * 완료된목록숫자확인
	 */
	@Override
	public int countCompleteCount() {
		return mapper.countCompleteCount();
	}

	/**새 목록 추가
	 * 
	 */
	@Override
	public int insertTodo(Todo todo) {
		
		return mapper.insertTodo(todo);
	}

	/**선택목록불러오기
	 * 
	 */
	@Override
	public Todo selectTodo(int listNo) {
		
		return mapper.selectTodo(listNo);
	}


	/**완료여부변경
	 * 
	 */
	@Override
	public int completeChange(int listNo) {
		
		return  mapper.completeChange(listNo);
	}


	/**할일 수정
	 * 
	 */
	@Override
	public int updateTodo(Todo todo) {
		
		return mapper.updateTodo(todo);
	}

	/** 삭제
	 * 
	 */
	@Override
	public int deleteTodo(int listNo) {
		return  mapper.deleteUser(listNo);
	}

	/** 제목 주서오기
	 * 
	 */
	@Override
	public String searchTitle(int todoNo) {
		
		return mapper.searchTitle(todoNo);
	}
	
	/*****************************************************/
	
	/** 과제목록 불러오기
	 * 
	 */
	@Override
	public List<Sub> selectAllSub(int listNo) {
		
		return mapper.selectAllSub(listNo);
	}
	
	/** 과제 1개 불러오기
	 * 
	 * @param subjectNo : 조회할 과제번호
	 * @return
	 */
	@Override
	public Sub selectSub(int subjectNo) {
		
		return mapper.selectSub(subjectNo);
	}

	/** 과제 추가하기
	 * 
	 */
	@Override
	public int insertSub(Sub sub) {
		
		return mapper.insertSub(sub);
	}

	/** 과제 완료여부 변경
	 * 
	 * @param sub : subjectNo 변경할 과제번호, complete 수정될 완료상태
	 * @return
	 */
	@Override
	public int completeChangeSub(Sub sub) {
		
		return mapper.completeChangeSub(sub);
	}

	/** 과제 수정
	 * 
	 */
	@Override
	public int updateSub(Sub sub) {
		
		return mapper.updateSub(sub);
	}

	/** 과제삭제
	 * 
	 */
	@Override
	public int deleteSub(int subjectNo) {
		
		return mapper.deleteSub(subjectNo);
	}

	/** 디테일 호출
	 * 
	 */
	@Override
	public String getDetail(int listNo) {
		
		return mapper.getDetail(listNo);
	}



	
	
	
	

}
