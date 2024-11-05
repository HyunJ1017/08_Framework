package edu.kh.daemoim.common.interceptor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.common.interceptor.mapper.InterceptorMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InterceptorServiceImple implements InterceptorService {
	
	private final InterceptorMapper mapper;
	
	@Override
	public List<Integer> getMemberNoList(String groupNo) {
		return mapper.getMemberNoList(groupNo);
	}
	
	@Override
	public int getMemberNo(String groupNo) {
		return mapper.getMembetNo(groupNo);
	}

}
