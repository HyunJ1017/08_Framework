package edu.kh.daemoim.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration 
@PropertySource("classpath:/config.properties")
public class FileConfig implements WebMvcConfigurer {

	// config.properties에서 ${key} 가 일치하는 값을 얻어와 변수에 대입
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold; // 임계값
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize; // 요청당 파일 최대 크기
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize; // 개별 파일당 최대 크기
	
	@Value("${spring.servlet.multipart.location}")
	private String location; // 임계값 초과 시 임시 저장 폴더 경로
	
	
	// ---------------------------------------
	
	// 모임 대표 이미지 요청 경로 + 서버 연결 폴더
	@Value("${daemoim.groupMain.resource-handler}")
	private String groupMainImgHandler;
	
	@Value("${daemoim.groupMain.resource-location}")
	private String groupMainImgLocation;
	// ---------------------------------------
	
	// 모임 해더 이미지 요청 경로 + 서버 연결 폴더
	@Value("${daemoim.groupHeader.resource-handler}")
	private String groupHeaderImgHandler;
	
	@Value("${daemoim.groupHeader.resource-location}")
	private String groupHeaderImgLocation;
	
	

	/* MultipartResolver 설정 */
	@Bean
	public MultipartConfigElement configElement() {
		
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		factory.setLocation(location);
		
		
		return factory.createMultipartConfig();
	}
	
	
	// MultipartResolver 객체를 bean으로 추가
	@Bean
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver multipartResolver
			= new StandardServletMultipartResolver();
		
		return multipartResolver;
	}
	
	
	// 인터넷(웹)으로 특정 형태의 요청(js,css,image)이 있을 경우
	// 서버 컴퓨터의 특정 폴더와 연결하는 설정을 작성하는 메서드
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		// 모임대표이미지
		registry
		.addResourceHandler(groupMainImgHandler)
		.addResourceLocations(groupMainImgLocation);
		
		// 모임해더이미지
		registry
		.addResourceHandler(groupHeaderImgHandler)
		.addResourceLocations(groupHeaderImgLocation);
	}
	
	
	
	
	
	
	
}


