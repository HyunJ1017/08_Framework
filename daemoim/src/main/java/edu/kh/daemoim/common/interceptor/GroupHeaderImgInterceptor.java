package edu.kh.daemoim.common.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.daemoim.groupManage.service.GroupManageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 모임 해더이미지 전달
@Slf4j
public class GroupHeaderImgInterceptor implements HandlerInterceptor {
	
	@Autowired
	private GroupManageService service;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String url = request.getRequestURI();
			// /board/{groupNo}/**
		String[] urlList = url.split("/");
		int length = urlList.length;
		String groupNo1 = null;
		if(length>2) groupNo1 = url.split("/")[2];
		String groupNo2 = null;
		if(length>3) groupNo2 = url.split("/")[3];

		
		if(groupNo1 != null && groupNo1.matches("\\d+")) { // 숫자인 경우에만 시행
			// 모임 상단이미지 덮어쓰기
			String groupHeaderImg = service.getGroupHeaderImg(groupNo1);
			request.setAttribute("groupHeaderImg", groupHeaderImg);
		} else if(groupNo2 != null && groupNo2.matches("\\d+")) {
			String groupHeaderImg = service.getGroupHeaderImg(groupNo2);
			request.setAttribute("groupHeaderImg", groupHeaderImg);
		}
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
