package edu.kh.project.fileUpload.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;
import edu.kh.project.fileUpload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("fileUpload")
@RequiredArgsConstructor
@Controller
public class FileUploadController {
	
	private final FileUploadService service;
	
	/** 파일 업로드 연습 메인 페이지 전환
	 * @return
	 */
	@GetMapping("main")
	public String goMain(Model model) {
		List<FileDto> fileList = service.selectFileList();
		model.addAttribute("fileList", fileList);
		
		return "fileUpload/main";
	}
	
	/* MultipartFile
	 * [Spring에서 제출된파일을 처리하는 방법]
	 * 
	 * 1. encType = "multipart/form-data"로 클라이언트가 요청
	 * 
	 * 2. Spring에 내장되어 있는 MultipartResolver가 제출된 파라미터들을 분류함
	 *    
	 *    문자열, 숫자 -> String (숫자 -> int,double 가능)
	 *    File -> MultipartFile 인터페이스 구현한 객체
	 * 
	 * 3. 컨트롤러 메서드 매개변수로 전달
	 * 		(@RequestParam, @ModelAttribute)
	 */
	
	/** 단일 파일 업로드(서버로 파일 제출)
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PostMapping("test1")
	public String test1(
			@RequestParam("uploadFile") MultipartFile uploadFile) throws IllegalStateException, IOException {
		
		String filePath = service.test1(uploadFile);
		
		log.debug("업로드 된 파일 경로 : {}", filePath);
		
		return "redirect:main";
	}

}
