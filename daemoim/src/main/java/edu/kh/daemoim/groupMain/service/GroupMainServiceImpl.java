package edu.kh.daemoim.groupMain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.dto.PhotoBox;
import edu.kh.daemoim.groupMain.dto.Schedule;
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
	public List<Notice> selectBoardList(int groupNo) {

		return mapper.selectBoardList(groupNo);
	}

	// 사진 불러오기
	@Override
	public List<PhotoBox> getPhotos(int groupNo) {

		return mapper.getPhotos(groupNo);

	}

	// 일정 불러오기
	@Override
	public List<Schedule> getSchedule(int groupNo) {

		return mapper.getSchedule(groupNo);
	}

	// 모임 소개 불러오기
	@Override
	public String getIntroduce(int groupNo) {

		return mapper.getIntroduce(groupNo);
	}

	// 모임 회원 체크
	@Override
	public boolean checkLoginMember(int groupNo, int memberNo) {

		int result = mapper.checkLoginMember(groupNo, memberNo);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 모임 가입신청 확인 버튼 클릭 시
	@Override
	public boolean joinGroup(int groupNo, int memberNo) {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberNo", memberNo);
		paramMap.put("groupNo", groupNo);
		paramMap.put("memberGroupBan", "N"); // 기본 값으로 0 설정
		paramMap.put("memberGroupDelFl", "N"); // 기본 값으로 "N" 설정
		
		int result = mapper.joinGroup(paramMap);
		return result > 0; // 삽입 성공 여부 반환
	}

}
