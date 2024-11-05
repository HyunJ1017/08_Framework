package edu.kh.daemoim.category.service;

import java.util.List;

import edu.kh.daemoim.category.dto.CategoryDTO;

public interface CategoryService {

	/** 입력된 검색어 조회하기
	 * @param category
	 * @param query
	 * @return
	 */
	List<CategoryDTO> getGroupsByCategory(String category, String query); 


}
