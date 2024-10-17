package edu.kh.daemoim.findIdPw.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.findIdPw.mapper.FindIdPwMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindIdPwServiceImpl implements FindIdPwService{

	private final FindIdPwMapper mapper;
	
}
