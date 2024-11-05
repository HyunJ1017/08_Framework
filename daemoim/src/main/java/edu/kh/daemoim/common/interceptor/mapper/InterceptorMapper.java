package edu.kh.daemoim.common.interceptor.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterceptorMapper {

	// 회원목록 얻어오기
	List<Integer> getMemberNoList(String groupNo);

	// 주인장번호불러오기
	int getMembetNo(String groupNo);

}
