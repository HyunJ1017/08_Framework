package edu.kh.project.common.scheduling.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulingMapper {

	// DB에 저장되어 있는 파일명 모두 조회
	List<String> getDbFileNameList();
	

}
