package edu.kh.daemoim.groupMain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.groupMain.dto.PhotoBox;

@Mapper
public interface PhotoBoxMapper {

	// 사진첩 불러오기
	List<PhotoBox> getPhotoBoxData();

}
