package edu.kh.project.common.scheduling.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.common.scheduling.mapper.SchedulingMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {
	
	private final SchedulingMapper mapper;

	// DB에 저장되어 있는 파일명 모두 조회
	@Override
	public List<String> getDbFileNameList() {
		return mapper.getDbFileNameList();
	}

}
