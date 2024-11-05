package edu.kh.daemoim.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.board.mapper.ScheduleMapper;
import edu.kh.daemoim.groupMain.dto.Schedule;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
	
	private final ScheduleMapper mapper;
	
	@Override
	public List<Schedule> getAllSchedules(int groupNo) {
		return mapper.getAllSchedules(groupNo);
	}
}
