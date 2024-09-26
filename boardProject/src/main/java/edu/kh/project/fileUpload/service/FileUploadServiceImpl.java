package edu.kh.project.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;
import edu.kh.project.fileUpload.mapper.FileUploadMapper;
import lombok.RequiredArgsConstructor;

//@Transactional
// - Unchecked Exception 발생 시 Rollback 수행
// Unchecked Exception : 예외처리 안해도 되는 친구

//@Transactional(rollbackFor = Exception.class)
// - Exception 이하 예외 발생시 Rollback 수행
// -> Unchecked, checked 가리지 않고 예외 발생 시 롤백
@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class FileUploadServiceImpl implements FileUploadService {
	
	private final FileUploadMapper mapper;
	
	// folder-path에 저장된 파일을 요청하고 싶을때 사용할 URL(주소), config.properties 설정
	@Value("${my.test.web-path}")
	private String testWebPath;
	
	// 파일 업로드 테스트 시 업로드된 파일이 저장될 폴더경로, config.properties 설정
	@Value("${my.test.folder-path}")
	private String testFolderPath;
	
	// 단일 파일 업로드(서버로 파일 제출)
	@Override
	public String test1(MultipartFile uploadFile) throws IllegalStateException, IOException {
		
			// MultipartFile이 제공하는 메서드
		
			// - getSize() : 파일 크기
			// - isEmpty() : 업로드한 파일이 없을 경우 true
			// - getOriginalFileName() : 원본 파일 명
			// - transferTo(경로) : 
			//    메모리 또는 임시 저장 경로에 업로드된 파일을
			//    원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		// 1) 업로드된 파일이 있는지 검사
		if( uploadFile.isEmpty() ) return null;
		
		// 2) 지정된 경로에 저장
		File forder = new File(testFolderPath);
		
		if(forder.exists() == false) { // C:/uploadFiles/ 폴더가 없을 경우
			forder.mkdirs(); // 폴더 까지의 경로 생성
		}
		
		
		// FileDto 객체를 만들어 필요한 정보를 셋
		FileDto file = FileDto.builder()
										.fileOriginalName(uploadFile.getOriginalFilename())
										.fileRename(uploadFile.getOriginalFilename())
										.filePath(testWebPath)
										.build();
		
		/* DB에 업로드 되는 파일 정보를 INSERT
		 * -> DB INSERT -> "파일 저장" 순서로 동작
		 *    만약에 "파일 저장" 중 예외 발생 (용량이 크다거나 등)
		 * -> @transactional 어노테이션이 Rollback 수행
		 * -> INSERT 취소
		 *  */
		int result = mapper.fileInsert(file);
		
				// 지정된 경로에 파일 저장
				// 업로드되어 메모리 또는 임시저장 폴더에 저장된 파일을 지정된 경로로 전달(저장)하는 구문
		uploadFile.transferTo( new File( testFolderPath + uploadFile.getOriginalFilename() ) );
		
		// 저장된 파일 경로 반환
		return testWebPath + uploadFile.getOriginalFilename();
	}
	
	// 파일 목록조회
	@Override
	public List<FileDto> selectFileList() {
		
		return mapper.selectFileList();
	}

}
