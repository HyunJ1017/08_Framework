package edu.kh.daemoim.sse.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.sse.dto.Notification;
import edu.kh.daemoim.sse.service.SseService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SseController {

	@Autowired
	private SseService service;
	
	private final Map<String, SseEmitter> emitters
		= new ConcurrentHashMap<>();
	
	/** 회원 연결 요청처리 */
	@GetMapping("sse/connect")
	public SseEmitter sseConnect(
			@SessionAttribute("loginMember") MyPage loginMember) {
		
		String clientId = loginMember.getMemberNo()+ "";
		
		SseEmitter emitter = new SseEmitter( 10*60*1000L);
		
		emitters.put(clientId, emitter);

	    emitter.onCompletion(() -> emitters.remove(clientId));

	    emitter.onTimeout(() -> emitters.remove(clientId));

	    return emitter;
		
	}
	
	/** 알림 메시지 전송 */
	@PostMapping("sse/send")
	public void sendNotification(
			@RequestBody Notification notification,
			@SessionAttribute("loginMember") MyPage loginMember
			) {
		
		notification.setSendMemberNo(loginMember.getMemberNo());
		
		Map<String, Object> map
			= service.insertNotification(notification);
		
		if (map == null || map.get("receiveMemberNo") == null) {
		    throw new IllegalStateException("알림 삽입 실패: receiveMemberNo가 null입니다.");
		}
		String clientId = map.get("receiveMemberNo").toString();
		
		SseEmitter emitter = emitters.get(clientId);
		
		if(emitter != null) {
			try {
				emitter.send( map );
			} catch(Exception e) {
				emitters.remove(clientId);
			}
		}
	}
	
	// -------------------------------------------------

	  /**
	   * 로그인한 회원의 알림 목록 조회
	   * @param loginMember
	   * @return
	   */
	  @GetMapping("notification")
	  public List<Notification> selectNotificationList(
	    @SessionAttribute("loginMember") MyPage loginMember
	    ){
	    int memberNo = loginMember.getMemberNo();
	    return service.selectNotificationList(memberNo);
	  }

	  /**
	   * 현재 로그인한 회원이 받은 알림 중 
	   * 읽지 않은 알림 개수 조회
	   * ("NOTIFICATION".NOTIFICATION_CHECK = 'N')
	   * @return
	   */
	  @GetMapping("notification/notReadCheck")
	  public int notReadCheck(
	    @SessionAttribute("loginMember") MyPage loginMember
	    ){
	    
	    return service.notReadCheck(loginMember.getMemberNo());
	  }

	  /** 알림 삭제 */
	  @DeleteMapping("notification/{notificationNo}")
	  public void deleteNotification(@PathVariable("notificationNo") int notificationNo) {
	      service.deleteNotification(notificationNo);
	  }
	  
	  /** [전체]알림 삭제 */
	  @DeleteMapping("notification/all")
	  public void deleteAllNotification(
			  @SessionAttribute("loginMember") MyPage loginMember){
		  service.deleteAllNotification(loginMember.getMemberNo());
	  }

	  /** 
	   * 알림 읽음 여부 변경(N->Y)
	   * @param notificationNo
	   */
	  @PutMapping("notification")
	  public void updateNotification(
			  @RequestParam(name="notificationNo") int notificationNo) {
	      service.updateNotification(notificationNo);
	  }
	  
	  /** 
	   * [전체]알림 읽음 여부 변경(N->Y)
	   * @param notificationNo
	   */
	  @PutMapping("notification/all")
	  public void updateAllNotification(
			  @SessionAttribute("loginMember") MyPage loginMember) {
	      service.updateAllNotification(loginMember.getMemberNo());
	  }
}
