package edu.kh.daemoim.groupMain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.mapper.GroupMainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMainServiceImpl implements GroupMainService {

	private final GroupMainMapper mapper;

	// 페이지 전환
	@Override
	public List<Notice> getBoardList() {
		
		return mapper.getBoardList();
	}

	// 공지사항 불러오기
	@Override
	public List<Notice> selectBoardList() {
	
		return mapper.selectBoardList();
	}
	


}
