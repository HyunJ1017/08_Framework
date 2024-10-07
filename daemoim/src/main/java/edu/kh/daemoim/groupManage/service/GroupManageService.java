package edu.kh.daemoim.groupManage.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.ManageCategory;

public interface GroupManageService {

	/** 모임이름 중복검사
	 * @param inputName
	 * @return
	 */
	int groupNameCheck(String inputName);

	/** 카테고리리스트 목록 반환
	 * @param categoryNo : 반환할 카테고리 종류
	 * @return
	 */
	List<ManageCategory> getCategoryList(int categoryNo);

	/** 카테고리 목록 반환
	 * @return
	 */
	List<ManageCategory> getCategoryArr();

	/** 그룹 생성
	 * @param inputGroup : 생성할 그룹 정보
	 * @param groupImg : 그룹 대표이미지 정보
	 * @return : 성공시 1, 실패시 0
	 */
	int createGroup(GroupManageDto inputGroup, MultipartFile groupImg);

}
