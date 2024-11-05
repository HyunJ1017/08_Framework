package edu.kh.daemoim.groupManage.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.groupMain.dto.Schedule;
import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.groupManage.dto.ManageCategory;

public interface GroupManageService {

	/** 모임이름 중복검사
	 * @param inputName
	 * @return
	 */
	int groupNameCheck(String inputName);

	/** 카테고리리스트 목록 반환
	 * @param categoryNo : 반환할 카테고리 종류
	 * @return
	 */
	List<ManageCategory> getCategoryList(int categoryNo);

	/** 카테고리 목록 반환
	 * @return
	 */
	List<ManageCategory> getCategoryArr();

	/** 모임 생성
	 * @param inputGroup : 생성할 그룹 정보
	 * @param groupImg : 모임 대표이미지 정보
	 * @return : 성공시 1, 실패시 0
	 */
	int createGroup(GroupManageDto inputGroup, MultipartFile groupImg);

	/** 모임정보 불러오기
	 * @param groupNo : 입력받은 모임번호
	 * @return
	 */
	GroupManageDto selectGroup(int groupNo);

	/** 모임 상세정보 수정
	 * @param updateGroup
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int updateGroup(GroupManageDto updateGroup, List<MultipartFile> images, List<Integer> deleteOrderList);

	/** [인터페이스] 최근작성댓글 얻어오기
	 * @param groupNo
	 * @return
	 */
	List<Comment> getRecentComments(String groupNo);

	
	/**공시글 불러오기
	 * @param groupNo
	 * @return
	 */
	List<Board> getOrderBoard(int groupNo);

	/** 최근글 불러오기
	 * @param groupNo
	 * @return
	 */
	List<Board> getRecentBoard(int groupNo);

	/** 인기글 불러오기
	 * @param groupNo
	 * @param period
	 * @return
	 */
	List<Board> getPopularBoard(int groupNo, int period);

	/** [인터페이스] 모임상단이미지
	 * @param groupNo
	 * @return
	 */
	String getGroupHeaderImg(String groupNo);

	/** 멤버리스트 호출
	 * @param groupNo
	 * @return
	 */
	List<GroupMemberManageDto> getMemberList(int groupNo);

	// 일정호출
	List<Schedule> getScheduleList(int groupNo);

	// 일정회원불러오기
	List<Integer> searchScheduleMember(int scheduleNo);



}
