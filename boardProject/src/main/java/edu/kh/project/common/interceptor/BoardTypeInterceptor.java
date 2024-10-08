package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.board.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* [Iterceptor]
 * 
 * - 요청/응답을 가로채는 객체(Spring 지원)
 * 
 * Client <-> DispatcherServlet <-> controller/redirect
 * Client <-> Filter <-> DispatcherServlet <-> controller
 * Client <-> DispatcherServlet <-> interceptor <-> controller
 * 
 * - HandlerInterceptor 인터페이스 상속 필요
 * 
 * - 제공 메서드 중 필요한 메서드 오버라이딩
 * 
 * 1) preHandle()
 *  - 전처리
 *  - DispatcherServlet -> controller 사이의 요청/응답을 가로채서 수행
 *  - ApplicationScope에 값을 저장해야 html까지 전달됨
 * 
 * 2) postHandle()
 *  - 후처리
 *  - controller -> DispatcherServlet 사이의 요청/응답을 가로채서 수행
 *  - RequestScope에 값을 저장해도 html에 전달됨
 * 
 * 3) afterCompletion()
 *  - 완성 후
 *  - forward된 뷰 완성 후
 *  - View Resolver -> DispatcherServlet 사이에서 수행
 */

@Slf4j
public class BoardTypeInterceptor implements HandlerInterceptor {
	
	@Autowired
	private BoardService service;
	
	// 전처리
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		log.info("------ BoardTypeInterceptor 전처리 메서드 실행 ------");
		
		// 어떤 요청이 와도 header에 출력되는 게시판 메뉴를 DB에서 얻어와 application scope에 세팅
		// aplication scope : 서버에 존재하면서 보든 사용자가 사용할 수 있음
		
		// 1) application scope 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// 2) application 객체에 "boardTypeList"가 없을 경우
		if(application.getAttribute("boardTypeList") == null) {
			
			//DB에서 모든 게시판 종류를 조회
			List<Map<String,String>> boardTypeList = service.selectBoardTypeList();

			// 조회 결과를 application 객체에 세팅
			log.debug(boardTypeList.toString());
			application.setAttribute("boardTypeList", boardTypeList);
			
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	

}
