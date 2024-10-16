package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;
import edu.kh.project.common.filter.SignUpFilter;

/* 사용할 필터 클래스를 Bean으로 등록 + 적용될 요청 주소 설정 */

@Configuration // 서버 실행시 내부 메서드 모두 수행
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<SignUpFilter> signUpFilter(){
		// FilterRegistrationBean : 필터를 Bean으로 등록하는 객체
		
		FilterRegistrationBean<SignUpFilter> filter = new FilterRegistrationBean<>();
		
		// 동작할 코드가 doFilter() 메서드에 작성된 필터객체(SignUpFilter) 생성
		SignUpFilter signUpfilter = new SignUpFilter();
		
		filter.setFilter(signUpfilter); // 필터 등록
		
		// 배열을 리스트로 변환하여 필터가 동작할 요청경로 패턴 지정
		String[] filteringUrl = {"/member/signUp"/*, "", "" ...*/};
		filter.setUrlPatterns( Arrays.asList(filteringUrl) );
		
		// 필터 이름 지정
		filter.setName("signUpFilter");
		
		// 필터 순서 지정
		filter.setOrder(1);
	
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		
		// LoginFilter 필터객체를 가져와 filter에 세팅
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		LoginFilter loginFilter = new LoginFilter();
		filter.setFilter(loginFilter);
		
		// 필터가 동작할 url패턴 지정
		String[] filteringUrl = {"/myPage/*", "/editBoard/*"};
		filter.setUrlPatterns( Arrays.asList(filteringUrl) );
		
		filter.setName("loginFilter");
		filter.setOrder(2);
		
		return filter;
	}
	
	
}
