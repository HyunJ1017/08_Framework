package edu.kh.daemoim.groupMain.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.dto.PhotoBox;
import edu.kh.daemoim.groupMain.dto.Schedule;

@Mapper
public interface GroupMainMapper {

	/**
	 *  공지 목록 조회
	 * @return
	 */
	List<Notice> getBoardList();

	/**
	 *  공지사항 목록 불러오기
	 * @return
	 */

	List<Notice> selectBoardList(int groupNo);

	/**
	 *  사진 불러오기
	 * @param groupNo
	 * @return
	 */

	List<PhotoBox> getPhotos(int groupNo);

	/**
	 *  일정 불러오기
	 * @param groupNo
	 * @return
	 */
	List<Schedule> getSchedule(int groupNo);

	/**
	 * 소개 불러오기 
	 * @param groupNo
	 * @return
	 */
	String getIntroduce(int groupNo);

	/**모임 회원 체크
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	int checkLoginMember(@Param("groupNo") int groupNo, @Param("memberNo") int memberNo);

	/**모임 가입신청 확인 버튼 클릭 시
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	
	int joinGroup(Map<String, Object> paramMap);



	
}
