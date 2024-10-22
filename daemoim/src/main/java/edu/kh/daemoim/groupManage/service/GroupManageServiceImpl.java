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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
@PropertySource("classpath:/config.properties")
public class GroupManageServiceImpl implements GroupManageService {
	
	private final GroupManageMapper mapper;
	
	@Value("${daemoim.groupMain.web-path}")
	private String groupMainWebPath;
	
	@Value("${daemoim.groupMain.folder-path}")
	private String groupMainFolderPath;
	
	@Value("${daemoim.groupHeader.web-path}")
	private String groupHeaderWebPath;
	
	@Value("${daemoim.groupHeader.folder-path}")
	private String groupHeaderFolderPath;
	

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
			inputGroup.setGroupMainImg(groupMainWebPath + groupMainRename);
		}
		
		// mapper에 모임 등록하고 결과 반환받기
		int result = mapper.createGroup(inputGroup);
		
		if(result == 0) return 0;
		
		// 모임장 그룹 가입시키기
		result = mapper.insertGroupLeader(inputGroup);
		
		
		if(groupImg.isEmpty() == false) {
			// 파일저장
			File folder = new File(groupMainFolderPath);
			if(folder.exists() == false) folder.mkdirs();
			
			try {
				groupImg.transferTo( new File( groupMainFolderPath + groupMainRename ) );
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
	
	// 모임 상세정보 수정
	@Override
	public int updateGroup(GroupManageDto updateGroup, List<MultipartFile> images, List<Integer> deleteOrderList) {
		
		// 기존 모임정보 얻어오기
		GroupManageDto preGroup = mapper.selectGroup( updateGroup.getGroupNo() );
		
		int result = 0;
		
		// 이미지 수정
		// 이미지 rename, path 세팅
		
		if(deleteOrderList.isEmpty() == false) { // 삭제한 이미지가 있다면
			for( int i : deleteOrderList) {
				switch(i) {
				case 0 : updateGroup.setGroupMainImg(null); break;
				case 1 : updateGroup.setGroupHeaderImg(null); break;
				}
			}
		} // if end
		
		for(int i=0 ; i<2 ; i++) {
			
			MultipartFile img = images.get(i);
			
			if( img.isEmpty() == false ) { // 업로드된 파일이 있다면
				
				log.info("index : {}", i);
				log.info("originalname : {}", img.getOriginalFilename());
				
				// 파일 리네임
				String imgRename = FileUtil.rename( img.getOriginalFilename() );
				
				 String webPath = "";
				 String folderPath = "";
				// 분기 설정
				switch(i) {
					case 0 :
						webPath = groupMainWebPath;
						folderPath = groupMainFolderPath;
						// 파일 요청경로 저장
						updateGroup.setGroupMainImg(webPath + imgRename);
						break;
					
					case 1 :
						webPath = groupHeaderWebPath;
						folderPath = groupHeaderFolderPath;
						// 파일 요청경로 저장
						updateGroup.setGroupHeaderImg(webPath + imgRename);
						break;
				}
				
				// 파일저장
				File folder = new File(folderPath);
				if(folder.exists() == false) folder.mkdirs();
				
				try {
					img.transferTo( new File( folderPath + imgRename ) );
				} catch (Exception e) {
					result = 0;
					throw new RuntimeException();
				}
			
			} else {
				
				// 업로드된 파일이 없는데 삭제된게 아니라면
				if( deleteOrderList.contains(i) == false ) {
					
					switch(i) {
						case 0 :
							updateGroup.setGroupMainImg( preGroup.getGroupMainImg() );
							break;
							
						case 1 :
							updateGroup.setGroupHeaderImg( preGroup.getGroupHeaderImg() );
							break;
					}
				} // if end
				
			} // if end
			
		} // for end
		
		
		// 모임정보 수정 (모임명, 소개글, 카테고리, 카테고리 리스트, 이미지요청주소 2개)
		// 카테고리 리스트는 수정 안했을때 값이 0임
		if(updateGroup.getCategoryListNo() == 0) {
			// 수정한적이 없다면
			// 이전 DB에있는 카테고리리스트넘버 가져와서덮어쓰기
			updateGroup.setCategoryListNo( preGroup.getCategoryListNo() );
		}
		
		// DB 업데이트 실행
		result = mapper.updateGroup(updateGroup);
		
		return result;
	}

}
