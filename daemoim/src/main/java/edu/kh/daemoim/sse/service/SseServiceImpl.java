package edu.kh.daemoim.sse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.sse.dto.Notification;
import edu.kh.daemoim.sse.mapper.SseMapper;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService{

	private final SseMapper mapper;
	
	// 알림 입력
	@Override
	public Map<String, Object> insertNotification(Notification notification) {
		
		Map<String, Object> map = new HashMap<>();
		
		int result = mapper.insertNotification(notification);
		
		if(result > 0) {
			map.put("receiveMemberNo", notification.getReceiveMemberNo());
		}
		
		return map;
	}

	// 알림리스트 조회
	@Override
	public List<Notification> selectNotificationList(int memberNo) {
		return mapper.selectNotificationList(memberNo);
	}

	// 읽지않은 알림 체크
	@Override
	public int notReadCheck(int memberNo) {
		return mapper.notReadCheck(memberNo);
	}

	// 알림 삭제
	@Override
	public void deleteNotification(int notificationNo) {
		mapper.deleteNotification(notificationNo);
	}
	
	// [전체] 알림 삭제
	@Override
	public void deleteAllNotification(int memberNo) {
		mapper.deleteAllNotification(memberNo);
	}

	// 알림 읽음처리
	@Override
	public void updateNotification(int notificationNo) {
		mapper.updateNotification(notificationNo);
	}

	// [전체] 알림 읽음처리
	@Override
	public void updateAllNotification(int memberNo) {
		mapper.updateAllNotification(memberNo);
	}
}
