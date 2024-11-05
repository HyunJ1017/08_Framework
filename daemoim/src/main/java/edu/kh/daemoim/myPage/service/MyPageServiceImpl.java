package edu.kh.daemoim.myPage.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.common.exception.FileUploadFailException;
import edu.kh.daemoim.common.util.FileUtil;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.myPage.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	
	 private final BCryptPasswordEncoder encoder;
		
	 @Value("${daemoim.profile.web-path}")
	 private String profileWebPath;
	 
	 @Value("${daemoim.profile.folder-path}")
	 private String profileFolderPath;
	  
	@Override
	public int updateInfo(MyPage inputMember) {
		
		return mapper.updateMember(inputMember);
	}
	
	@Override
	public int withdrawal(String memberPw, MyPage loginMember) {

		if(encoder.matches(memberPw, loginMember.getMemberPw())== false) {
			return 0;
		}
		
	
		
		return mapper.withdrawal(loginMember.getMemberNo());
	}
	
	@Override
	public int checkNickname(String input) {
		return mapper.checkNickname(input);
	}
	
	// 비밀번호 변경
	// 비밀번호 변경
		@Override
		public int changePw(String currentPw, String newPw, MyPage loginMember) {
			
			// 1) 입력 받은 현재 비밀번호가
			//    로그인한 회원의 비밀번호와 일치하는지 검사
			//    (BCryptPasswordEncoder.matches(평문, 암호문) 이용)
			
			// 비밀번호가 일치하지 않으면
			if( encoder.matches(currentPw, loginMember.getMemberPw()) == false ) {
				return 0;
			}
			
			// 2) 새 비밀번호 암호화
			String encPw = encoder.encode(newPw);
			
			// 3) DB 비밀번호 변경(회원 번호, 암호화된 새 비밀번호)
			loginMember.setMemberPw(encPw); // 세션에 저장된 회원 정보 중 PW 변경
			return mapper.changePw(loginMember.getMemberNo(), encPw);
		}
		
		@Override
		public String profile(MultipartFile profileImg, int memberNo) {

			// 1) 파일 업로드 확인
			if(profileImg.isEmpty()) return null;
			
			String rename = FileUtil.rename(profileImg.getOriginalFilename());
			
			String url = profileWebPath + rename;
			
			int result = mapper.profile(url, memberNo);
			
			if(result == 0) return null;
			
			try {
				
				File folder = new File(profileFolderPath);
				if(!folder.exists()) folder.mkdir();
				
				profileImg.transferTo(
						new File(profileFolderPath + rename));
				
				
			} catch (Exception e) {
				e.printStackTrace();
				
				throw new FileUploadFailException("프로필 이미지 수정 실패");
			}
			
			return profileWebPath + rename;
		}
		
	@Override
	public MyPage findMemberNickname(String memberNickname) {
		
		return mapper.selectMemberNickname(memberNickname);
	}
	
	
	@Override
	public List<MainDTO> findMyGroup(int memberNo) {
		return mapper.findMyGroup(memberNo);
	}

	@Override
	public int groupOut(int memberNo, int groupNo) {

		return mapper.groupOut(memberNo,groupNo );
	}
	@Override
	public GroupMemberManageDto getGroupMemberInfo(int memberNo, int groupNo) {
	return null;
}
}


