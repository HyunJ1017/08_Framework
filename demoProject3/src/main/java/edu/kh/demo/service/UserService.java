package edu.kh.demo.service;

public interface UserService {

	/**사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * @param userNo
	 * @return userName
	 */
	String selectUserName(int userNo);

}
