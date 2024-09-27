package edu.kh.project.fileUpload.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.fileUpload.dto.FileDto;

public interface FileUploadService {

	/** 단일 파일 업로드(서버로 파일 제출)
	 * @param uploadFile
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String test1(MultipartFile uploadFile) throws IllegalStateException, IOException;

	/** 메인페이지에 띄워줄 파일리스트 불러오기
	 * @return fileList
	 */
	List<FileDto> selectFileList();

	/** 파일 이름을 입력받아 파일 업로드 하기
	 * @param uploadFile : 임시저장되어있는 파일
	 * @param fileName : 입력받은 파일 이름
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String test2(MultipartFile uploadFile, String fileName) throws IllegalStateException, IOException;

	/** 단일 파일 업로드 + 사용자 정의 예외를 이용한 예외처리
	 * @param uploadFile
	 * @return
	 */
	String test3(MultipartFile uploadFile);

}
