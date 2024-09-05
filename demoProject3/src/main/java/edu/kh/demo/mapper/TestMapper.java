package edu.kh.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

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

}
