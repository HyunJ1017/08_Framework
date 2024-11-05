package edu.kh.daemoim.groupMain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupMain.dto.PhotoBox;
import edu.kh.daemoim.groupMain.mapper.PhotoBoxMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoBoxServiceImpl implements PhotoBoxService {

	private final PhotoBoxMapper mapper;

	// 사진첩 불러오기
	@Override
	public List<PhotoBox> getPhotoBoxData() {
	
		return mapper.getPhotoBoxData();
	}
}
