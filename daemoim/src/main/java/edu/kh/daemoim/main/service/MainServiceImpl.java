package edu.kh.daemoim.main.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.main.mapper.MainMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

	private final MainMapper mapper;
	
}