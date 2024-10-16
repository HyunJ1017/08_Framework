package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 생성 및 적용 방법]
 * 
 * SignUpFilter 가서 확인
 * 
 */

public class LoginFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// HttpServletRequest/Response로 다운캐스팅
		HttpServletRequest req   = (HttpServletRequest)  request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		// 로그인이 되어있지 않은 경우
		HttpSession session = req.getSession();
		if(session.getAttribute("loginMember") == null) {
			resp.sendRedirect("/");
		} else {
			chain.doFilter(request, response);
		}
		System.out.println("--- {[[ 222 ]]} ---");
	}

}
