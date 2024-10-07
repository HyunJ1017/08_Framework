package edu.kh.daemoim.common.util;

import java.text.SimpleDateFormat;

public class FileUtil {
	
	// 파일명 뒤에 붙는 숫자 (1~99999 반복)
	public static int seqNum = 1;

	
	/** 전달받은 원본 파일명을 시간 형태의 파일명으로 변경
	 * 
	 *  aple.jpg -> 20240927093712_00003.jpg : 1초에 99999개 이상 업로드되면 오류 생길거임
	 * @param originalFileName
	 * @return
	 */
	public static String rename(String originalFileName) {
		
		// 1) 확장자 추출하기( .jpg, .png 등)
		int index = originalFileName.lastIndexOf("."); // 파일명 제일뒤 "."의 인덱스 반환
		String ext = originalFileName.substring( index ); // 원본파일명에서 .부터 끝까지 잘라낸 문자열 (== 확장자)
		
		// 2) 현재시간을 yyyyMMddHHmmss 형태의 문자열로 반환받기
		// 시간을 지정된문자열로 간단히 바꿔주는 객체
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// Date() : 현재 시간을 저장한 객체 생성, new java.util.Date() : 임폴트 안할려고 다씀
		String time = sdf.format(new java.util.Date());
		
		// 3) seqNum을 이용한 숫자 생성
		// "%05d" : 정수가 들어갈 5칸짜리 오른쪽 정렬 패턴, 빈칸에는 0을 체워넣음
		String number = String.format("%05d", seqNum);
		
		seqNum++;
		if(seqNum == 100000) seqNum=1; //(1~99999 반복)
		
		return time + "_" + number + ext; // => 20240927093712_00003.jpg
	}
}
