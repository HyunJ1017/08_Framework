package edu.kh.daemoim.myPage.service;

import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.myPage.dto.MyPage;

public interface MyPageService {


	// 회원 정보 수정
	int updateInfo(MyPage inputMember);

	//탈퇴
	int withdrawal(String memberPw, MyPage loginMember);

	// 닉네임 변경
	int checkNickname(String input);

	// 비밀번호 변경
	int changePw(String currentPw, String newPw, MyPage loginMember);

	// 프로필 수정
	String profile(MultipartFile profileImg, int memberNo) ;

	// 상대 회원 정보 보기
	MyPage findMemberNickname(String memberNickname);


	// 가입한 모임 이름
	List<MainDTO> findMyGroup(int memberNo);
	

	GroupMemberManageDto getGroupMemberInfo(int memberNo, int groupNo);

	int groupOut(int memberNo, int groupNo);

	
	

	
}
