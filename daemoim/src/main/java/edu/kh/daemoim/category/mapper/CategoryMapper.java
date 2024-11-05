package edu.kh.daemoim.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.daemoim.category.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {

	/** 입력된 검색어 조회하기
	 * @param category
	 * @param query
	 * @return
	 */
	List<CategoryDTO> getGroupsByCategory(
						@Param("category") String category, 
						@Param("query") String query);


}
