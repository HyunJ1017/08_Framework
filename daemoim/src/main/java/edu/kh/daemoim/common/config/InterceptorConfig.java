package edu.kh.daemoim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.daemoim.common.interceptor.GroupHeaderImgInterceptor;
import edu.kh.daemoim.common.interceptor.GroupManageInterceptor;
import edu.kh.daemoim.common.interceptor.ManagerFilterInterceptor;
import edu.kh.daemoim.common.interceptor.BoardFilterInterceptor;
import edu.kh.daemoim.common.interceptor.BoardScheduleFilterInterceptor;
import edu.kh.daemoim.common.interceptor.BoardTypeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	
	@Bean // 메서드에서 반환된 객체를 Bean으로 등록
	public BoardTypeInterceptor boardTypeInterceptor() {
		return new BoardTypeInterceptor();
	}
	
	@Bean
	public GroupManageInterceptor groupManageInterceptor() {
		return new GroupManageInterceptor();
	}
	
	@Bean
	public GroupHeaderImgInterceptor groupHeaderImgInterceptor() {
		return new GroupHeaderImgInterceptor();
	}
	
	@Bean
	public BoardFilterInterceptor boardFilterInterceptor() {
		return new BoardFilterInterceptor();
	}
	
	@Bean
	public BoardScheduleFilterInterceptor boardScheduleFilterInterceptor() {
		return new BoardScheduleFilterInterceptor();
	}
	
	@Bean
	public ManagerFilterInterceptor managerFilterInterceptor() {
		return new ManagerFilterInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor( groupManageInterceptor() )
				.addPathPatterns("/groupManage/**", "/groupMemberManage/**");
		
		registry.addInterceptor( groupHeaderImgInterceptor() )
				.addPathPatterns("/groupMain/**", "/board/**");
		
		registry.addInterceptor(boardTypeInterceptor())
			     .addPathPatterns("/**")
			     .excludePathPatterns("/css/**", "/js/**");
		
		registry.addInterceptor( boardFilterInterceptor() )
				.addPathPatterns("/board/**")
				.excludePathPatterns("/board/boardSchedule/**", "/board/boardCalendar/**");
		
		registry.addInterceptor( boardScheduleFilterInterceptor() )
				.addPathPatterns("/board/boardSchedule/**");
		
		registry.addInterceptor( managerFilterInterceptor() )
				.addPathPatterns("/groupManage/**", "/groupMemberManage/**")
				.excludePathPatterns("/groupManage/createGroup");
		
	}

}
