package edu.kh.daemoim.groupManage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.ManageCategory;

@Mapper
public interface GroupManageMapper {

	// 모임명 중복검사
	int groupNameCheck(String inputName);
	
	// 카테고리 리스트 검색
	List<ManageCategory> getCategoryList(int categoryNo);

	// 카테고리 검색
	List<ManageCategory> getCategoryArr();

	// 모임 생성
	int createGroup(GroupManageDto inputGroup);

	// 모임장 그룹에 넣기
	int insertGroupLeader(GroupManageDto inputGroup);
	
	// 모임 조회
	GroupManageDto selectGroup(int groupNo);

	// 모임 상세내용 수정
	int updateGroup(GroupManageDto updateGroup);



}
