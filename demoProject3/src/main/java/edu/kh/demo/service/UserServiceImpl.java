package edu.kh.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.demo.dto.UserDto;
import edu.kh.demo.mapper.TestMapper;

//@Service
// - Service 역할(비즈니스 로직 처리)임을 명시하고
// - Bean 등록 (== Spring 관리 객체, IOC 제어반전)

@Service
public class UserServiceImpl implements UserService {
	
	/* @Autowired
	 * - 등록된 Bean 중에서
	 * 자료형이 같은 Bean을 얻어와 필드에 대입
	 * == DI (의존성 주입)
	 */
	@Autowired
	private TestMapper mapper;

	/**사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * 
	 */
	@Override
	public String selectUserName(int userNo) {
		// 커넥션 생성구문 생략
		// 커넥션 자원반환구문 생략
		return mapper.selectUserName(userNo);
	}

	/** 사용자 전체 조회
	 * @return userList
	 */
	@Override
	public List<UserDto> selectAll() {
		// mapper : 의존성주입(DI)으로 실행된
		// TestMapper 인터페이스를 상속하도록 클래스로 생성하여 만들어진 Bean
		return mapper.selectAll();
	}

	/** userNo가 일치하는 User 조회
	 */
	@Override
	public UserDto selectUser(int userNo) {
		return mapper.selectUser(userNo);
	}

	/** 사용자 정보 수정 (DML 구문 시행)
	 *  -> 트랜잭션 제어처리 시행해야함
	 * @param user : 수정할 정보가 담긴 dto 객체
	 * @return result 0 || 1
	 */
//	@Transactional
//	해당 메서드 수행중 예외(RuntimeException) 발생 시 ROLLBACK 수행
//	예외가 발생하지 않으면 메서드 종료 후 COMMIT 수행
	@Transactional
	@Override
	public int updateUser(UserDto user) {
		return mapper.updateUser(user);
	}

	/** 사용자 삭제
	 */
	@Transactional
	@Override
	public int deleteUser(int userNo) {
		return mapper.deleteUser(userNo);
	}

	/** 사용자 추가
	 * @Transactional
	 */
	@Transactional
	@Override
	public int insertUser(UserDto user) {
		return mapper.insertUser(user);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
