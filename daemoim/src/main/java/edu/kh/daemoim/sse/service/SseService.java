package edu.kh.daemoim.sse.service;

import java.util.List;
import java.util.Map;

import edu.kh.daemoim.sse.dto.Notification;

public interface SseService {

	// 알림입력
	Map<String, Object> insertNotification(Notification notification);

	// 알림리스트 조회
	List<Notification> selectNotificationList(int memberNo);

	// 읽지않은 알림갯수 조회
	int notReadCheck(int memberNo);

	// 알림삭제
	void deleteNotification(int notificationNo);
	
	// 알림 전체삭제
	void deleteAllNotification(int memberNo);

	// 알림읽음여부 변경
	void updateNotification(int notificationNo);

	// 알림 전체읽음
	void updateAllNotification(int memberNo);

}
