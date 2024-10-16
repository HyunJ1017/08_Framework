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
 * 1. jakarta.servlet.Filter 인터페이스를 상속받은 클래스 생성
 * 2. 상속 후 doFilter() 메서드 오버라이딩
 * 3. FilterConfig 클래스에 생성한 필터 클래스를 등록
 * 
 */

public class SignUpFilter implements Filter {

	@Override
	public void doFilter(
			ServletRequest request,		// HttpServletRequest 의 부모타입
			ServletResponse response,	// HttpServletResponse의 부모타입
			FilterChain chain)				// 연결된 필터로 넘어가는 용도
			throws IOException, ServletException {
		
		// request, response => 요청 응답 객체 가 전달되어 저장됨
		
		/* 로그인 회원의 일부 페이지 진입 차단 */
		
		// 로그인 된 회원의 정보가 Session에 있는지 확인
		// -> session 객체를 얻어와 확인
		// -> session 객체를 얻어오려면 HttpServletRequest
		
		// Http 통신이 가능한 형태로 request, response 다운 캐스팅
		HttpServletRequest  req =  (HttpServletRequest)  request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		// 세션에 로그인한 회원 정보가 있을경우
		HttpSession session = req.getSession();
		if ( session.getAttribute("loginMember") != null ) {
			
			resp.sendRedirect("/");
		} else {
			// 다음 필터로 이동
			// 만약 다음 필터가 없다면 DispatbherServlet 으로 이동
			chain.doFilter(request, response);
		}
		
		System.out.println("--- {[[ 111 ]]} ---");
		
	}
}
