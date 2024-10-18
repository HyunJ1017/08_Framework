package edu.kh.project.common.scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.project.common.scheduling.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class ImageDeleteScheduling {
	
	private final SchedulingService service;
	
	@Value("${my.profile.folder-path}")
	private String profilePath; // 프로필이미지 저장경로

	@Value("${my.board.folder-path}")
	private String boardPath; // 게시판이미지 저장경로
	
	
	// 0초 시작, 매 20초가 지날 때 마다 수행
	// 보통 몇주 ~ 몇달마다 함
	// @Scheduled(cron = "0/20 * * * * *")
	@Scheduled(cron = "0 30 * * * *")
	public void imageDelete() {
		
		
		
		// 1. DB에 저장되어 있는 파일명 모두 조회
		//  - MEMBER.PROFILE_IMG에서 파일 명만 조회
    //  - BOARD.FILE_RENAME만 조회
    //  - 두 결과를 UNION해서 하나의 SELECT 결과로 반환 받기
		
		List<String> dbFileNameList = service.getDbFileNameList();
		
		
		
		// 2. 서버에 저장된 이미지 목록 모두 조회하기
		
		// 서버 저장폴더를 참조
		File profileFolder = new File(profilePath);
		File boardFolder = new File(boardPath);
		
		// 폴더에 저장된 파일 목록을 File[] 형태로 반환받아 List<File>로 변환
		List<File> profileList = Arrays.asList( profileFolder.listFiles() );
		List<File> boardList   = Arrays.asList( boardFolder.listFiles() );
		
		// 두 리스트를 하나로 합침
		List<File> serverList = new ArrayList<>();
		serverList.addAll(profileList);
		serverList.addAll(boardList);
		
		
		
		// 3. dbFileNameList와 serverList의 파일명 비교
		// serverList에 존재하는 파일이 dbFileNameList에 없으면 서버에 저장된 이미지 삭제
		log.info("-------- 파일삭제를 시작 합니다 --------");
		int count = 0;
		for( File serverFile : serverList) {
			if( !dbFileNameList.contains( serverFile.getName() ) ) {
				log.info("파일삭제 : {}", serverFile.getName() );
				serverFile.delete(); // 파일삭제
				count ++;
			}
		}
		log.info("-------- {}개의 파일이 삭제되었습니다. --------", count);
		
		
		
		
	}

}
