package edu.kh.daemoim.chatting.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import edu.kh.daemoim.chatting.service.ChattingService;
import edu.kh.daemoim.myPage.dto.MyPage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChattingWebsocketHandler extends TextWebSocketHandler{
	
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

		log.debug("messageChatting : {}", message.getPayload());
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		Chat chat = objectMapper.readValue(message.getPayload(), Chat.class);
		
		int result = service.insertChatting(chat);
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm");
//		chat.setChatWriteDate( sdf.format( new Date() ) );
		
		List<Chat> memberNoList = service.getMemberNos(chat.getGroupNo());
		List<Integer> noList = new ArrayList<>();
		memberNoList.forEach( e -> noList.add( e.getMemberNo() ) );
		
		// 모임에 속한 회원들에게 메세지전송하기
		for(WebSocketSession wss : sessions) {
		
			HttpSession clientSession = (HttpSession)wss.getAttributes().get("session");
			
			int clientNo = ((MyPage)clientSession.getAttribute("loginMember")).getMemberNo();
			
			if(noList.contains(clientNo)) {
				log.debug("sendChat to : {}", clientNo);
				TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chat));
				wss.sendMessage(textMessage);
			}
			
		} // for end
		
	} // handleTextMessage() end

}
