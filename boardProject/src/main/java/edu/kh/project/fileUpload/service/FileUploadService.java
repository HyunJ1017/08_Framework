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

}
