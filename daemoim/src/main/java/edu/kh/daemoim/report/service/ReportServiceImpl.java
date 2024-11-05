package edu.kh.daemoim.report.service;

import org.springframework.stereotype.Service;

import edu.kh.daemoim.report.mapper.ReportMapper;
import edu.kh.daemoim.siteManage.dto.StopMember;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

	private final ReportMapper mapper;

	@Override
	public int insertReport(StopMember member) {
		// TODO Auto-generated method stub
		return mapper.insertReport(member);
	}
}
