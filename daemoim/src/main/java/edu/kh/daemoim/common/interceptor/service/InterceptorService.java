package edu.kh.daemoim.common.interceptor.service;

import java.util.List;

public interface InterceptorService {

	// 멤버리스트 호출
	List<Integer> getMemberNoList(String groupNo);

	// 주인장번호불러오기
	int getMemberNo(String groupNo);

}
