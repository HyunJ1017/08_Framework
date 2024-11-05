package edu.kh.daemoim.groupMain.service;

import java.util.List;

import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.dto.PhotoBox;
import edu.kh.daemoim.groupMain.dto.Schedule;

public interface GroupMainService {

	/**
	 *  페이지전환
	 * @return
	 */
	List<Notice> getBoardList();

	/**
	 *  공지사항 목록 불러오기
	 * @return
	 */

	List<Notice> selectBoardList(int groupNo);

	/**
	 *  사진불러오기
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
	 *  모임 소개 불러오기
	 * @param groupNo
	 * @return
	 */
	String getIntroduce(int groupNo);

	/** 모임회원 체크
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	boolean checkLoginMember(int groupNo, int memberNo);

	/** 모임 가입신청 확인 버튼 클릭 시
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	boolean joinGroup(int groupNo, int memberNo);

	




	



	



}
