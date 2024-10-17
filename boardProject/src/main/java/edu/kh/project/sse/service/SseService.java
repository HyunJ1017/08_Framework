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

}
