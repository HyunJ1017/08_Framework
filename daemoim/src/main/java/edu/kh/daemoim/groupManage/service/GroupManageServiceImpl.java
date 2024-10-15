package edu.kh.daemoim.groupManage.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.common.util.FileUtil;
import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.ManageCategory;
import edu.kh.daemoim.groupManage.mapper.GroupManageMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
@PropertySource("classpath:/config.properties")
public class GroupManageServiceImpl implements GroupManageService {
	
	private final GroupManageMapper mapper;
	
	@Value("${daemoim.groupMain.web-path}")
	private String webPath;
	
	@Value("${daemoim.groupMain.folder-path}")
	private String folderPath;

	// 모임이름 중복검사
	@Override
	public int groupNameCheck(String inputName) {
		return mapper.groupNameCheck(inputName);
	}

	// 카테고리 리스트 검색
	@Override
	public List<ManageCategory> getCategoryList(int categoryNo) {
		return mapper.getCategoryList(categoryNo);
	}
	
	// 카테고리 검색
	@Override
	public List<ManageCategory> getCategoryArr() {
		return mapper.getCategoryArr();
	}
	
	// 모임 생성
	@Override
	public int createGroup(GroupManageDto inputGroup, MultipartFile groupImg) {
		
		// 모임 이름, 소개, 카테고리
		// 모임대표이미지, 모임장 등록
		// 모임넘버, 생성일은 자동생성
		
		String groupMainRename = null;
		
		if(groupImg.isEmpty()) {
			inputGroup.setGroupMainImg("");
		} else {
			// 파일 리네임
			groupMainRename = FileUtil.rename( groupImg.getOriginalFilename() );
			// 파일 요청경로 저장
			inputGroup.setGroupMainImg(webPath + groupMainRename);
		}
		
		// mapper에 모임 등록하고 결과 반환받기
		int result = mapper.createGroup(inputGroup);
		
		if(result == 0) return 0;
		
		// 모임장 그룹 가입시키기
		result = mapper.insertGroupLeader(inputGroup);
		
		
		if(groupImg.isEmpty() == false) {
			// 파일저장
			File folder = new File(folderPath);
			if(folder.exists() == false) folder.mkdirs();
			
			try {
				groupImg.transferTo( new File( folderPath + groupMainRename ) );
			} catch (Exception e) {
				result = 0;
				throw new RuntimeException();
			}
		}
		return result;
	}
	
	// 모임정보 불러오기
	@Override
	public GroupManageDto selectGroup(int groupNo) {
		
		return mapper.selectGroup(groupNo);
	}

}
