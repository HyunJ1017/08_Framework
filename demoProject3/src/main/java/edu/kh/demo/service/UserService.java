package edu.kh.demo.service;

import java.util.List;

import edu.kh.demo.dto.UserDto;

public interface UserService {

	/**사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * @param userNo
	 * @return userName
	 */
	String selectUserName(int userNo);

	/** 사용자 전체 조회
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
	 * @return result 1 || 0
	 */
	int deleteUser(int userNo);

	/** 유저 추가하기
	 * @param user
	 * @return
	 */
	int insertUser(UserDto user);

}
