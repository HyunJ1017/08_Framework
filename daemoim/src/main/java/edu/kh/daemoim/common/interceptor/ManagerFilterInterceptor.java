package edu.kh.daemoim.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import edu.kh.daemoim.common.interceptor.service.InterceptorService;
import edu.kh.daemoim.myPage.dto.MyPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

// 모임관리페이지 접근제한
@Slf4j
public class ManagerFilterInterceptor implements HandlerInterceptor {

	@Autowired
	private InterceptorService service;
	
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler)
			throws Exception {
        
        MyPage loginMember = (MyPage)request.getSession().getAttribute("loginMember");
        String groupNo = request.getRequestURI().split("/")[2];
        
		if(groupNo != null && groupNo.matches("\\d+")) { // 숫자인 경우에만 시행
			int memberNo = service.getMemberNo(groupNo);
			if(memberNo != loginMember.getMemberNo()) {
				HttpSession session = request.getSession();
				session.setAttribute("message", "모임장만 입장할 수 있습니다.");
				log.info("[보안] >> 모임장_권한_페이지 : 리턴");
				response.sendRedirect("/groupMain/" + groupNo);
				return false;
			}
		}
        
        return true;
	}
}
