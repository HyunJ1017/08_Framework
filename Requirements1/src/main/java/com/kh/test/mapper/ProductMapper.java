package com.kh.test.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

	List<Map<String, Object>> selectCategoryList();

}
