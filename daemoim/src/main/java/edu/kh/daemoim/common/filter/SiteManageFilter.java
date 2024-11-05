package edu.kh.daemoim.common.filter;

import java.io.IOException;

import edu.kh.daemoim.myPage.dto.MyPage;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SiteManageFilter implements Filter {

	@Override
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain)
			throws IOException, ServletException {

		// 다운 캐스팅
		HttpServletRequest  req =  (HttpServletRequest)  request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		if( ((MyPage)session.getAttribute("loginMember") ).getAuthority() != 3 ) {
			// 사이트관리자 권한이 없다면 리턴
			session.setAttribute("message", "관리자만 입장할 수 있습니다.");
			resp.sendRedirect("/");
			log.info("[보안] >> 사이트_관리_페이지 : 리턴");
		} else {
			chain.doFilter(request, response);
		}
		
		
	}
}
