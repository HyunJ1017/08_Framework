package com.kh.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {	

	@Autowired
	private ProductMapper mapper;
	
	@Override
	public List<Map<String, Object>> selectCategoryList() {
		return mapper.selectCategoryList();
	}
}
