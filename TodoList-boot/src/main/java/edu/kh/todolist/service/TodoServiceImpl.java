package edu.kh.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	
	
	

}
