package edu.kh.project.sse.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.expression.Arrays;

import edu.kh.project.member.dto.Member;
import edu.kh.project.sse.dto.Notification;
import edu.kh.project.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


/*
[알림기능 구현]

SSE (Server - Sent - Event)
 */
//@RequiredArgsConstructor : emitters 를 상수선언 해놔서 오류날 수 있음
@RestController // @Controller + @responseBody
@Slf4j
public class SseController {
	
	// SseEmitter : 서버로부터 메시지를 전달받을 클라이언트 정보를 저장한 객체 == 연결된 클라이언트
	// ( 이미터 입니다. 에미터 아닙니다. )
	
	// (* Concurrent : 동시발생)
	// ConcurrentHashMap : 멀티스레드 환경에서 동기화를 보장하는 Map
	// 한번에 많은 요청이 있어도 차례대로 처리 (조금 느려도 안전성이 보장됨)
	
	@Autowired
	private SseService service;
	
	// 상수(final)선언 되어서 서버 실행시 static 필드에 항상 만들어져 있을거임!!!
	// -> 이거때문에 @RequiredArgsConstructor 못쓰고 @Autowired 써야함
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // 연결된 클라이언트의 대기명단
	
	
	
	/** 클라이언트가 서버에 연결 요청
   *  -> 클라이언트가 서버로부터 데이터를 받기 위한 대기상태에 놓임
	 * @param loginMember
	 * @return
	 */
	@GetMapping("sse/connect")
	public SseEmitter sseConnect(
			@SessionAttribute("loginMember") Member loginMember) {
		
		// Map에 저장된 "key"값으로 회원번호 얻어오기
		String clientId = loginMember.getMemberNo() + "";
		
		// SseEmitter 객체 생성
		// SseEmitter( Long timeout ) -> 연결 대기시간 10분 설정 (ms단위)
		SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
		
		// 클라이언트 정보를 Map에 추가
		emitters.put(clientId, emitter);
			// {"user01" : user01 정보}, {"user02" : user02 정보} ...
		
		// 클라이언트 연결 종료시 Map에서 제거
		// emitter.onCompletion( () -> {} );
		emitter.onCompletion( () -> emitters.remove(clientId) );
		 
		// 클라이언트 타임아웃 시 Map에서 제거
		emitter.onTimeout( () -> emitters.remove(clientId) );
		
		return emitter;
	} // sseConnect end
	
	
	/** 알림 메시지 전달
	 * 
	 * 	return void
	 */
	@PostMapping("sse/send")
	public void sendNotification(
			@RequestBody Notification notification,
			@SessionAttribute("loginMember") Member loginMember) {
		
		
		// 알림 보낸사람(현재 로그인한 회원)번호 추가
		notification.setSendMemberNo(loginMember.getMemberNo());
		
		// 전달받은 알림데이터를 DB에 저장하고
		// 알림 받을 회원의 번호 
		// + 해당 회원이 읽지 않은 알림 개수 반환 받는 서비스 호출
		Map<String, Object> map = service.insertNotification(notification);
		
		
		
		
		// 알림을 받을 클라이언트 id
		String clientId = map.get("receiveMemberNo").toString();
			// log.debug("clientId : {}", clientId);
		
		// 연결된 클라이언트 대기 명단(emitters)에서
		// clientId가 일치하는 클라이언트 찾기
		SseEmitter emitter = emitters.get(clientId);
		
		// clientId가 일치하는 클라이언트가 있을경우
		if(emitter != null) {
			try {
				emitter.send( map );
			} catch (Exception e) {
				emitters.remove(clientId);
			} // try-catch end
			
		} // if end
		
	} // sendNotification() end
	
	/** 로그인 한 회원의 알림목록 조회
	 * @param loginMember
	 * @return
	 */
	@GetMapping("notification")
	public List<Notification> notification(@SessionAttribute("loginMember") Member loginMember) {
		return service.selectNotificationList(loginMember.getMemberNo());
	}
	
	
	/** 현재 읽지 않은 알림 갯수 조회
	 * @return
	 */
	@GetMapping("notification/notReadCheck")
	public int  notReadCheck(@SessionAttribute("loginMember") Member loginMember) {
		return service.notReadCheck(loginMember.getMemberNo());
	}
	
	
	/** 알림 지우기
	 */
	@DeleteMapping("notification")
	public void deleteNotification(@RequestBody int notificationNo) {
		service.deleteNotification(notificationNo);
	}
	
	
	/** 알림 읽음 표시하기
	 * @param notificationNo
	 */
	@PutMapping("notification")
	public void updateNotification(@RequestBody int notificationNo) {
		service.updateNotification(notificationNo);
	}
}
