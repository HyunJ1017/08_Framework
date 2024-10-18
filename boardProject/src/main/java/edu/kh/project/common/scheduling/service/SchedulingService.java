package edu.kh.project.common.scheduling.service;

import java.util.List;

public interface SchedulingService {

	/** DB에 저장되어 있는 파일명 모두 조회
	 * @return list
	 */
	List<String> getDbFileNameList();

}
