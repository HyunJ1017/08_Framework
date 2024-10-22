package edu.kh.daemoim.groupMain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.groupMain.mapper.GroupMainMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMainServiceImpl implements GroupMainService {

	private final GroupMainMapper mapper;
}
