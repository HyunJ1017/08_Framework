package edu.kh.daemoim.groupMain.service;

import java.util.List;

import edu.kh.daemoim.groupMain.dto.PhotoBox;

public interface PhotoBoxService {

	// 사진첩 목록 불러오기
	List<PhotoBox> getPhotoBoxData();

}
