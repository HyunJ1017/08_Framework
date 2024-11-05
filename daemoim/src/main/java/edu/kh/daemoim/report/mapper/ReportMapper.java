package edu.kh.daemoim.report.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.siteManage.dto.StopMember;

@Mapper
public interface ReportMapper {

	int insertReport(StopMember member);
		

}
