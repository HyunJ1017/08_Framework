package edu.kh.daemoim.signin.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.signin.mapper.SigninMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SigninServiceImpl implements SigninService{
	
	private final SigninMapper mapper;
	

}
