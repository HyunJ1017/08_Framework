package edu.kh.daemoim.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.daemoim.board.service.ScheduleService;
import edu.kh.daemoim.groupMain.dto.Schedule;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("schedules")
@RequiredArgsConstructor
public class ScheduleController {
	
	private final ScheduleService service;
	
	@GetMapping("list/{groupNo:[0-9]+}")
	@ResponseBody
	public List<Map<String, Object>> getSchedules(
		@PathVariable("groupNo") int groupNo) {
		List<Schedule> schedules = service.getAllSchedules(groupNo);
    List<Map<String, Object>> events = new ArrayList<>();

    for (Schedule schedule : schedules) {
        Map<String, Object> event = new HashMap<>();
        event.put("title", schedule.getLocation());  // 일정 제목
        event.put("start", schedule.getScheduleDate().toString());  // 시작 날짜
        event.put("end", schedule.getScheduleDate().toString());    // 종료 날짜
        event.put("description", schedule.getLocationAddress());    // 장소 설명
        events.add(event);
    }

    return events;
	}
}
