package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.interceptor.BoardNameInterceptor;
import edu.kh.project.common.interceptor.BoardTypeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	// BoardTypeInterceptor 클래스를 Bean으로 등록
	@Bean
	public BoardTypeInterceptor boardTypeInterceptor() {
		return new BoardTypeInterceptor();
	}
	
	@Bean
	public BoardNameInterceptor boardNameInterceptor() {
		return new BoardNameInterceptor();
	}
	
	// 요청 응답을 가로채서 동작할 interceptor 추가
	// 해당 메서드에 작성된 인터셉터 객체가 동작
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor( boardTypeInterceptor() )
						.addPathPatterns("/**") // "/"이하 모든 요청을 가로챔
						.excludePathPatterns("/css/**", "/js/**", "/images/**", "/favicon.ioc"); // 가로채지 않을 요청주소
		registry.addInterceptor( boardNameInterceptor() )
		.addPathPatterns("/board/**", "/editBoard/**");
	}

}
