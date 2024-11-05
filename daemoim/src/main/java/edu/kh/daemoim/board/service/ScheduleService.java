package edu.kh.daemoim.board.service;

import java.util.List;

import edu.kh.daemoim.groupMain.dto.Schedule;

public interface ScheduleService {

	List<Schedule> getAllSchedules(int groupNo);

}
