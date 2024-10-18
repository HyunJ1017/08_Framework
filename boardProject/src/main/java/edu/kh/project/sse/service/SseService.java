package edu.kh.project.sse.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.sse.dto.Notification;

public interface SseService {

	/** 알림 삽입 후 알림받을 회원번호 + 알림갯수를 반환
	 * @param notification
	 * @return map
	 */
	Map<String, Object> insertNotification(Notification notification);

	/** 로그인 한 회원의 알림을 조회하겠다
	 * @param memberNo
	 * @return
	 */
	List<Notification> selectNotificationList(int memberNo);

	/** 로그인 한 회원의 읽지않은 알림갯수 조회하기
	 * @param memberNo
	 * @return
	 */
	int notReadCheck(int memberNo);

	/** 알림 삭제
	 * @param notificationNo
	 */
	void deleteNotification(int notificationNo);

	/** 알림 읽음처리하기
	 * @param notificationNo
	 */
	void updateNotification(int notificationNo);

}
