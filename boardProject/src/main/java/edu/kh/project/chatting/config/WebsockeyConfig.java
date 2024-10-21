package edu.kh.project.chatting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.chatting.handler.ChattingWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration // 서버 실행시 메서드 모두 수행
@EnableWebSocket // 웹소캣 활성화 설정
@RequiredArgsConstructor
public class WebsockeyConfig implements WebSocketConfigurer{ // 웹소캣설정

	//SessionHandshake "Interceptor" 빈 의존성 주입 받기
	private final HandshakeInterceptor handshakeInterceptor;
	
	//ChattingWebsocket "Handler" 빈 의존성 주입받기
	private final ChattingWebsocketHandler chattingWebsocketHandler; 
	
	// 웹소캣 핸들러를 등록하는 메서드
	@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry
			// 핸들러 등록
			.addHandler(chattingWebsocketHandler, "/chattingSock")
			// 인터셉터 등록
			.addInterceptors(handshakeInterceptor)
			// 웹소켓 요청을 허용할 주소페턴
			.setAllowedOriginPatterns(
					"http://localhost/",
					"http://127.0.0.1/",
					"http://192.168.10.124/")
			// SockJS 지원 + 브라우저 호환성 증가
			.withSockJS();
		}
	
}
