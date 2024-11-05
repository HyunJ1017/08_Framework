package edu.kh.daemoim.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.daemoim.common.filter.LoginFilter;
import edu.kh.daemoim.common.filter.SiteManageFilter;

@Configuration
public class FilterConfig {
	
	// 로그인회원 제한
	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		LoginFilter loginFilter = new LoginFilter();
		
		filter.setFilter(loginFilter);
		
		String[] filteringUrl = {"/siteManage", "/siteManage/*", "/groupManage/*", "/groupMemberManage/*", "/board/*", "/myPage"};
		filter.setUrlPatterns( Arrays.asList(filteringUrl) );
		
		filter.setName("loginFilter");
		
		filter.setOrder(1);
		
		return filter;
	}
	
	// 사이트관리권환 제한
	@Bean
	public FilterRegistrationBean<SiteManageFilter> siteManageFilter(){
		// FilterRegistrationBean : 필터를 Bean으로 등록하는 객체
		
		FilterRegistrationBean<SiteManageFilter> filter = new FilterRegistrationBean<>();
		
		// 동작할 코드가 doFilter() 메서드에 작성된 필터객체(SignUpFilter) 생성
		SiteManageFilter siteManageFilter = new SiteManageFilter();
		
		filter.setFilter(siteManageFilter); // 필터 등록
		
		// 배열을 리스트로 변환하여 필터가 동작할 요청경로 패턴 지정
		String[] filteringUrl = {"/siteManage", "/siteManage/*"};
		filter.setUrlPatterns( Arrays.asList(filteringUrl) );
		
		// 필터 이름 지정
		filter.setName("siteManageFilter");
		
		// 필터 순서 지정
		filter.setOrder(2);
	
		return filter;
	}

}
