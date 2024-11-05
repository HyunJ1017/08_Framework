package edu.kh.daemoim.myPage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.myPage.dto.MyPage;

@Mapper
public interface MyPageMapper {
	

		List<MainDTO> findGroupByNo = null;

		// 회원 정보 수정
		int updateMember(MyPage inputMember);

		// 회원 탈퇴
		int withdrawal(int memberNo);

		// 닉네임 중복 검사
		int checkNickname(String input);
		
		// 비밀번호 변경
		int changePw(
				@Param("memberNo") int memberNo, 
				@Param("encPw") String encPw);

		// 프로필 변경
		int profile(
				@Param("url") String url,
				@Param("memberNo") int memberNo);
		
		// 회원 정보 보기
		MyPage selectMemberNickname(String memberNickname);

		// 가입한 모임 
		List<MainDTO> findMyGroup(int memberNo);

		// 모임 탈퇴 
		int groupOut(@Param("memberNo") int memberNo, @Param("groupNo") int groupNo);

		
		
		
		

}
