package edu.kh.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.demo.dto.UserDto;

/* @Mapper
 * - 마이바티스 mapper와 연결된 인터페이스임을 명시
 * - 자동으로 해당 인터페이스를 상속받은 클래스로 만들어
 *   Bean으로 등록함
 */

@Mapper
public interface TestMapper {

	/**사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * 
	 * @param userNo
	 * @return
	 */
	String selectUserName(int userNo);
	// -> 해당 메서드가 호출되면
	// 연결되어져있는 mapper.xml파일에서
	// id속성값이 메서드명과 같은 sql태그가 수행됨

	/** 사용자 전체 조회
	 * resultType이 UserDto로 설정되어 있는데
	 * 반환형은 Lsit 형태라서 다르다고 생각할 수 있지만
	 * 한 행이 조회 될 대 마다 List에 DTO가 add 되는 것이다
	 * @return userList
	 */
	List<UserDto> selectAll();

	/** userNo가 일치하는 User 조회
	 * @param userNo
	 * @return
	 */
	UserDto selectUser(int userNo);

	/** 사용자 정보 수정
	 * @param user : 수정할 정보가 담긴 dto 객체
	 * @return result 0 || 1
	 */
	int updateUser(UserDto user);

	/** 사용자 삭제
	 * @param userNo
	 * @return 0 || 1
	 */
	int deleteUser(int userNo);

	/**사용자 추가
	 * @param user
	 * @return
	 */
	int insertUser(UserDto user);

}
