package edu.kh.daemoim.tacou.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.tacou.mapper.TacouMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TacouServiceImpl implements TacouService{

	private final TacouMapper mapper;
	
}
