package edu.kh.daemoim.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.daemoim.board.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardTypeInterceptor implements HandlerInterceptor{

	@Autowired
	private BoardService service;
	
//전처리
	@Override
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler) throws Exception {
		
		// log.info("----- BoardTypeInterceptor 전처리 메서드 실행 -----");
		
		// 어떤 요청이 와도
		// header에 출력되는 게시판 메뉴를 DB에서 조회해
		// Application Scope에 세팅
		
		// 1) application scope 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// 2) application 객체에 "boardTypeList"가 없을 경우
		if(application.getAttribute("boardTypeList") == null) {
			
			log.info("----- boardTypeList 조회 -----");
			
			// DB에서 모든 게시판 종류를 조회하는 서비스 호출
			List<Map<String, String>> boardTypeList
				= service.selectBoardTypeList();
			
			log.debug(boardTypeList.toString());
			
			// 조회 결과를 application 객체에 세팅
			application.setAttribute("boardTypeList", boardTypeList);
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	// 후처리
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	// view 완성 후
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception {
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
