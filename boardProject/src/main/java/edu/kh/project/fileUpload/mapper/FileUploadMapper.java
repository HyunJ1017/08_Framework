package edu.kh.project.fileUpload.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.fileUpload.dto.FileDto;

@Mapper
public interface FileUploadMapper {


	/** 파일 1개 정보 DB에 삽입
	 * @param file
	 * @return result
	 */
	int fileInsert(FileDto file);

	List<FileDto> selectFileList();

}
