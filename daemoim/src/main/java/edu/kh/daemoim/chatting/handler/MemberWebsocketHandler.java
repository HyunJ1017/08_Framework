package edu.kh.daemoim.chatting.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kh.daemoim.chatting.dto.Chat;
import edu.kh.daemoim.chatting.dto.ChatRoom;
import edu.kh.daemoim.chatting.service.ChattingService;
import edu.kh.daemoim.myPage.dto.MyPage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberWebsocketHandler extends TextWebSocketHandler{
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	@Autowired
	private ChattingService service;
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		log.debug("messageChatRoom : {}", message.getPayload());
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ChatRoom chatRoom = objectMapper.readValue(message.getPayload(), ChatRoom.class);
		
		List<Chat> memberNoList = service.getMemberNos(chatRoom.getGroupNo());
		List<Integer> noList = new ArrayList<>();
		memberNoList.forEach( e -> noList.add( e.getMemberNo() ) );
		
		List<Chat> memberNickList = new ArrayList<>();
		
		
		// 로그인중인 회원 뽑아내기
		for(WebSocketSession wss : sessions) {
		
			HttpSession clientSession = (HttpSession)wss.getAttributes().get("session");
			
			int clientNo = ((MyPage)clientSession.getAttribute("loginMember")).getMemberNo();
			log.debug("[수행] MemberWebsocketHandler 조회번호 : {}", clientNo);
			if(noList.contains(clientNo)) {
				String clientNickname = ((MyPage)clientSession.getAttribute("loginMember")).getMemberNickname();
				memberNickList.add( new Chat().builder().memberNickname(clientNickname).build() );
				log.debug("[수행] MemberWebsocketHandler 리스트추가 : {}", clientNickname);
			}
		} // for end
		
		TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(memberNickList));
		
		// 다시 돌려주기
		for(WebSocketSession wss : sessions) {
			
			HttpSession clientSession = (HttpSession)wss.getAttributes().get("session");
			
			int targetNo = ((MyPage)clientSession.getAttribute("loginMember")).getMemberNo();
			
			if(noList.contains(targetNo)) {
				wss.sendMessage(textMessage);
				log.debug("[수행] MemberWebsocketHandler sendData to : {}", targetNo);
			}
			
		} // for end
		
	} // handleTextMessage() end

}
