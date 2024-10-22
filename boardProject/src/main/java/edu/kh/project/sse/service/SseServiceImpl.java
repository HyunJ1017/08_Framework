package edu.kh.project.sse.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.sse.dto.Notification;
import edu.kh.project.sse.mapper.SseMapper;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {
	
	private final SseMapper mapper;
	
	// 알림 삽입 후 알림받을 회원번호 + 알림갯수를 반환
	@Override
	public Map<String, Object> insertNotification(Notification notification) {
		
		// 매개변수 notification 에 저장된 값 5개
		// notificationType/Url/Content, pkNo, sendMemberNo
		Map<String, Object> map = null;
		
		// 알림 삽입
		int result = mapper.insertNotification(notification);
		
		if(result > 0) { // 알림 삽입 성공시
			// 알림을 받아야 하는 회원의 번호 + 안읽은 알람 개수 조회
			map = mapper.selectReceiveMember(notification.getNotificationNo());

			
      /* 채팅 알림인 경우 */
      if(notification.getNotificationType().equals("chatting")){
        String url = notification.getNotificationUrl();
        String[] arr = url.split("chat-no=");
        String chatNo = arr[arr.length-1];
  
        map.put("chattingRoomNo", chatNo);
        map.put("notificationNo", notification.getNotificationNo());
      }
		}
		
		return map;
	}
	
	// 로그인 한 회원의 알림을 조회하겠다
	@Override
	public List<Notification> selectNotificationList(int memberNo) {
		
		return mapper.selectNotificationList(memberNo);
	}
	
	// 로그인 한 회원의 읽지않은 알림 갯수 조회하기
	@Override
	public int notReadCheck(int memberNo) {
		return mapper.notReadCheck(memberNo);
	}
	
	// 알림 삭제
	@Override
	public void deleteNotification(int notificationNo) {
		mapper.deleteNotification(notificationNo);
	}
	
	// 읽음처리하기
	@Override
	public void updateNotification(int notificationNo) {
		mapper.updateNotification(notificationNo);
	}

}
