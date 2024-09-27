package edu.kh.project.myPage.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@PropertySource("classpath:/config.properties")
@Transactional	// 서비스 내 매서드 수행 중 UnCheckedException 발생 시 rollback 수행, 아니면 메서드 종료시 commit 수행
@Service
public class MyPageServiceImple implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired // 의존성주입 DI
	private BCryptPasswordEncoder encoder;
	
	@Value("${my.profile.web-path}")
	private String profileWebPath; // 웹 접근 경로
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath; // 이미지 저장경로

	/**
	 *  회원 정보 수정
	 * @param inputMember : 멤버번호 + 수정할 닉네임, 전화번호, 주소
	 * @return
	 */
	@Override
	public int updateInfo(Member inputMember) {
		
		// 만약 주소가 입력되지 않은 경우 null로 변경
		// "" + "" + "" => '"", "", ""' => ',,'
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// UPDATE 구문 수행 시 MEMBER_ADDRESS 컬럼값이 NULL이 됨
		}
		
		// SQL 수행 후 결과반환
		return mapper.updateInfo(inputMember);
	}

	/**(비동기) 닉네임 중복검사
	 * 
	 * @param input
	 * @return 0 | 1, 0이면 중복아님, 1중복맞음
	 */
	@Override
	public int checkNickname(String input) {
		
		return mapper.checkNickname(input);
	}

	/**
	 * 비밀번호 변경
	 * @param currentPw : 현재 비밀번호
	 * @param newPw : 변경하려는 새 비밀번호
	 * @param loginMember : 세션에서 얻어온 로그인한 회원 정보
	 * @return
	 */
	@Override
	public int changePw(String currentPw, String newPw, Member loginMember) {
		
		// 1) 입력받은 현재 비밀번호가 로그인한 회원의 비밀번호와 일치하는지 검사
		// 		(BCryptPasswordEncoder.matches(평문, 암호문) 이용, 일반비교로는 비교 못함)
		if( encoder.matches( currentPw, loginMember.getMemberPw() ) == false ) {// 실패시
			return 0;
		}
		
		// 2) 새 비밀번호 암호화
		String encPw = encoder.encode(newPw);
		
		// 3) DB 비밀번호 변경(회원 번호, 암호화된 새 비밀번호)
		loginMember.setMemberPw(encPw); // 세션에 저장된 회원 정보 중 PW 변경
		
		// 4) DB 비밀번호를 변경(회원번호, 암호화된 새 비밀번호)
		return mapper.changePw(loginMember.getMemberNo(), encPw);
	}
	
	/**
	 * 회원 탈퇴
	 */
	@Override
	public int secession(String memberPw, Member loginMember) {
		
		// 1) 비밀번호 일치 검사
		if( encoder.matches( memberPw, loginMember.getMemberPw() ) == false) {
			return 0; // 비밀번호가 일치하지 않으면 0 반환
		}
		
		// 2) 회원 탈퇴 Mapper 호출
		return mapper.secession( loginMember.getMemberNo() );
	}
	
	//로그인한 회원의 프로필 이미지 수정
	@Override
	public String profile(MultipartFile profileImg, int memberNo) {
		
		// 1) 이미지 확인
		if(profileImg.isEmpty()) {
			
			// 제출된 파일이 없음 == X버튼을 눌러 기본이미지로 변경
			// == DB에 저장된 이미지 경로가 NULL
			int result = mapper.profile(null, memberNo);
			return null;
		}
		
		// 2) 파일 명 변경
		String rename = FileUtil.rename(profileImg.getOriginalFilename());
		
		// 3) DB update 준비 준비 : 웹접근경로 + 변경된 파일명
		String url = profileWebPath + rename;
		
		// 4) DB update
		int result = mapper.profile(url, memberNo);
		
		// 5) 결과처리
		if(result == 0) return null;
		
		try {
			// 폴더 없으면 생성
			File folder = new File(profileFolderPath);
			if(!folder.exists()) {
				// 폴더 없으면 폴더경로 생성
				folder.mkdirs();
			}
			
			// 업로드되어 임시저장된 이미지를 지정된 경로로 옮기기
			profileImg.transferTo(new File(profileFolderPath + rename));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException("프로필 이미지 수정 실패");
		}
		
		
		return profileWebPath + rename;
	}

}
