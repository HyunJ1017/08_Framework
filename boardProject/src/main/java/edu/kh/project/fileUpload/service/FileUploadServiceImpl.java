package edu.kh.project.fileUpload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
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
				
				/* DB에 파일데이터 저장*/
		
				// DB로 insert 하기 전 중복되지 않는 이름으로 변경
				String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
				// FileDto 객체를 만들어 필요한 정보를 셋
				FileDto file = FileDto.builder()
												.fileOriginalName(uploadFile.getOriginalFilename())
												.fileRename( rename )
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
		uploadFile.transferTo( new File( testFolderPath + rename ) );
		
		// 저장된 파일 경로 반환
		return testWebPath + rename;
	}
	
	
	// 파일 목록조회
	@Override
	public List<FileDto> selectFileList() {
		
		return mapper.selectFileList();
	}
	
	
	// 파일 이름을 입력받아 파일 업로드 하기
	@Override
	public String test2(MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException {
		
		// 1) 업로드된 파일 확인, 셋다 같은 의미입니다
		if(uploadFile.isEmpty()) return null;
		if(uploadFile.getSize() <= 0) return null;
		if(uploadFile.getOriginalFilename().equals("")) return null;;
		
		// 2) 제출된 fileName이 없다면 기존 파일명 유지( 3항연산자 입니다.)
		
		int index = uploadFile.getOriginalFilename().lastIndexOf(".");
		String ext = uploadFile.getOriginalFilename().substring(index);
		
		String originalName = fileName.equals("") ? uploadFile.getOriginalFilename() : fileName + ext; // 입력된 파일명.확장자
		
		// 3) 파일명 변경하기
		String rename = FileUtil.rename(originalName);
		
		// 4) DB에 파일 정보부터 INSERT
		FileDto file = FileDto.builder().fileOriginalName(originalName)
																		.fileRename(rename)
																		.filePath(testWebPath)
																		.build();
		
		int result = mapper.fileInsert(file);
		
		// 5) 지정된 폴더로 임시저장된 파일을 옮기기
		uploadFile.transferTo( new File(testFolderPath + rename) );
		
		return testWebPath + rename;
	}
	
	
	//단일 파일 업로드 + 사용자 정의 예외를 이용한 예외처리
	@Override
	public String test3(MultipartFile uploadFile) {
		
		// 1. 업로드된 파일이 있는지 검사
		if(uploadFile.isEmpty()) return null;
		
		// 2) 파일명 변경
		String rename = FileUtil.rename(uploadFile.getOriginalFilename());
		
		// 3) DB에 파일정보 insert
		FileDto file = FileDto.builder().fileOriginalName(uploadFile.getOriginalFilename())
				                            .fileRename(rename)
				                            .filePath(testWebPath)
				                            .build();
		int result = mapper.fileInsert(file);
		
		// 4) 지정된 경로로 임시저장된 파일 옮기기
		try {
			uploadFile.transferTo(new File(testFolderPath + rename));
			
			// 예외 확인용 정상작동해도 아무 Exception이나 던져보는 코드
			if(result > 0) throw new RuntimeException();
			
		} catch (Exception e) {
			e.printStackTrace();
		  // 여기서 예외를 잡아버리면 클래스너머로 예외가 안넘어가져서 @transactional이 커밋 해버림
			
			// transferTo()는 Checked Exception을 던지기 때문에
			// 1. throws 또는 try-catch를 무조건 작성
			// 2) throws 작성시 호출하는 메서드에서 추가 예외처리를 작성해야 되는 번거로움이 있음
			// 3) try-catch 작성 시
			//    메서드 내부에서 예외가 처리되어
			//    메서드 종료 시 예외가 던져지지 않아
			//    @Transactional이 rollback을 수행할 수 없게된다
			
			// [해결방법]
			// * try-catch를 작성해서 Checkd Exception을 처리
			//   - 호출하는 메서드에 throws 구문 작성하는 번거로움이 없음
			
			// * "Unchecked Exception" 형태의 "사용자 정의 예외" 강제 발생
			//   - @Transactional 어노테이션에 rollback 속성을 적어야하는 번거로움이 없음
			
			// 예외 강제 발생
			throw new FileUploadFailException(); //=> 사용자정의 Exception이어서 만들어야함, => edu.kh.project.common.exception 에 작성함
			// IOException : Unchecked Exception
			// RuntimeException : Checked Exception
			// Unchecked Exception는 throws를 작성하지 않아도 컴파일러가 자동으로 호출메서드로 던져줌
			
			//@Transactional
			// - Unchecked Exception 발생 시 Rollback 수행
			// Unchecked Exception : 예외처리 안해도 되는 친구
			//@Transactional(rollbackFor = Exception.class)
			// - Exception 이하 예외 발생시 Rollback 수행
			// -> Unchecked, checked 가리지 않고 예외 발생 시 롤백
		}
		
		return testWebPath + rename;
	}
	
	

}
