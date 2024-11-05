package edu.kh.daemoim.board.service;

import java.util.List;
import java.util.Map;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.groupMain.dto.Schedule;
import edu.kh.daemoim.siteManage.dto.StopMember;

public interface BoardService {

	/** 게시글 목록 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int groupNo, int boardTypeCode, int cp);

	/** 검색 목록 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @param cp
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> selectSearchList(int groupNo, int boardTypeCode, int cp, Map<String, Object> paramMap);

	/** 게시글 상세 조회
	 * @param map
	 * @return
	 */
	Board selectDetail(Map<String, Integer> map);

	/** 조회수 1 증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** DB에서 모든 게시판 종류를 조회
	 * @return
	 */
	List<Map<String, String>> selectBoardTypeList();

	/** 일정 조회
	 * @param groupNo
	 * @return
	 */
	Map<String, Object> selectScheduleList(int groupNo);

	/** 일정 참석
	 * @param scheduleNo
	 * @return
	 */
	int attendSchedule(int scheduleNo, int groupNo, int memberNo);

	/** 일정 참석 취소
	 * @param scheduleNo
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	int cancelSchedule(int scheduleNo, int groupNo, int memberNo);

	/** 일정 생성
	 * @param scheduleMap
	 * @return
	 */
	int createSchedule(Map<String, Object> scheduleMap);

	/** 좋아요 up 및 down
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	Map<String, Object> boardLike(int boardNo, int memberNo);

	/** 댓글 목록 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @param boardNo
	 * @return commentList
	 */
	List<Comment> selectCommentList(int boardNo);
	

	/** 현재 게시물이 속한 페이지 번호 조회
	 * @param paramMap
	 * @return
	 */
	int getCurrentPage(Map<String, Object> paramMap);



}
