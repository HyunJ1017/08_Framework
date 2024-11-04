package edu.kh.daemoim.groupMain.service;

import java.util.List;

import edu.kh.daemoim.groupMain.dto.Notice;

public interface GroupMainService {

	// 페이지전환
	List<Notice> getBoardList();

	// 공지사항 목록 불러오기

	List<Notice> selectBoardList();


	



}
