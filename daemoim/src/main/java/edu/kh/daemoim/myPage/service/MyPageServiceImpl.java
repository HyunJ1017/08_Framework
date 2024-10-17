package edu.kh.daemoim.myPage.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.myPage.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	
}
