package edu.kh.daemoim.signup.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.signup.mapper.SignupMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService{

	private final SignupMapper mapper;
	
}
