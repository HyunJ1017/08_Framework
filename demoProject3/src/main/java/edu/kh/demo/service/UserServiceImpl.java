package edu.kh.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	
	
	
	
	
	
	
	
	
	

}
