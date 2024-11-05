package edu.kh.daemoim.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.groupMain.dto.Schedule;
import edu.kh.daemoim.siteManage.dto.StopMember;

@Mapper
public interface BoardMapper {

	/** groupNo와 boardTypeCode가 일치하고 게시물 중 삭제되지 않은 게시글 수 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @return
	 */
	int getListCount(@Param("groupNo") 			 int groupNo, 
									 @Param("boardTypeCode") int boardTypeCode);

	/** 지정된 페이지 분량의 게시글 목록 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(@Param("groupNo")				int groupNo,
															@Param("boardTypeCode")	int boardTypeCode, 
															@Param("rowBounds")			RowBounds rowBounds);
	
	/** 게시글 상세 조회
	 * @param map
	 * @return
	 */
	Board selectDetail(Map<String, Integer> map);

	/** 검색조건이 맞는 게시글 수 조회
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);

	/** 검색 목록 조회
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectSearchList(Map<String, Object> paramMap, RowBounds rowBounds);
	
	/** DB에서 모든 게시판 종류를 조회
	 * @return
	 */
	List<Map<String, String>> selectBoardTypeList();

	/** 조회수 1 증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	/** 일정 조회
	 * @param groupNo
	 * @return
	 */
	List<Schedule> selectScheduleList(int groupNo);

	/**
	 * 일정 참석 여부 확인
	 * @param scheduleNo
	 * @param groupNo
	 * @param memberNo
	 * @return result
	 */
	int checkSchedule(	
			@Param("scheduleNo") int scheduleNo, 
			@Param("groupNo") int groupNo,
			@Param("memberNo") int memberNo);
	
	/** 일정 참석
	 * @param scheduleNo
	 * @return
	 */
	int attendSchedule(
			@Param("scheduleNo") int scheduleNo, 
			@Param("groupNo") int groupNo,
			@Param("memberNo") int memberNo);

	/** 일정 참석 취소
	 * @param scheduleNo
	 * @param groupNo
	 * @param memberNo
	 * @return
	 */
	int cancelSchedule(
			@Param("scheduleNo") int scheduleNo, 
			@Param("groupNo") int groupNo,
			@Param("memberNo") int memberNo);

	/** 일정 생성
	 * @param scheduleMap
	 * @return
	 */
	int createSchedule(Map<String, Object> scheduleMap);

	/** 좋아요를 이미 눌렀는지 확인
	 * @param groupNo
	 * @param boardTypeCode
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int checkBoardLike(@Param("boardNo")       int boardNo, 
										 @Param("memberNo") 		 int memberNo);
	
	/** 댓글 목록 조회
	 * @param groupNo
	 * @param boardTypeCode
	 * @param boardNo
	 * @return
	 */
	List<Comment> selectCommentList(@Param("boardNo")	int boardNo);

	/** 현재 게시물이 속해있는 페이지 조회
	 * @param paramMap
	 * @return
	 */
	int getCurrentPage(Map<String, Object> paramMap);


	/** 좋아요 DB에 저장
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int insertBoardLike(@Param("boardNo")int boardNo, 
										  @Param("memberNo")int memberNo);

	/** 좋아요 DB에서 삭제
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int deleteBoardLike(@Param("boardNo")int boardNo, 
											@Param("memberNo")int memberNo);

	/** 페이지에 좋아요 개수 체크
	 * @param boardNo
	 * @return
	 */
	int getLikeCount(int boardNo);
}
