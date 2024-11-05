package edu.kh.daemoim.common.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.groupManage.service.GroupManageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 모임 관리페이지 댓글 전달
public class GroupManageInterceptor implements HandlerInterceptor{
	
	@Autowired
	private GroupManageService service;
	
	@Override
	public void postHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView
			) throws Exception {
		
		
		String url = request.getRequestURI();
			// /groupMemberManage/{groupNo}/**
		String groupNo = url.split("/")[2];
			// {boardNo}
		
		if(groupNo != null && groupNo.matches("\\d+")) { // 숫자인 경우에만 시행
			// 언제 새 댓글이 생길지 모르니까 항상 덮어쓰기
			List<Comment> commentList = service.getRecentComments(groupNo);
			
			request.setAttribute("commentList", commentList);
		}
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
