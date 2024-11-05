package edu.kh.daemoim.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.groupMain.dto.Schedule;


@Mapper
public interface ScheduleMapper {

	
	List<Schedule> getAllSchedules(int groupNo);

}
