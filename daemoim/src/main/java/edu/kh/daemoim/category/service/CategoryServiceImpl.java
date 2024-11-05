package edu.kh.daemoim.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.category.dto.CategoryDTO;
import edu.kh.daemoim.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

	private final CategoryMapper mapper;
	
	// 입력된 검색어 조회하기
	@Override
	public List<CategoryDTO> getGroupsByCategory(String category, String query) {
		return mapper.getGroupsByCategory(category, query);
	}
}
