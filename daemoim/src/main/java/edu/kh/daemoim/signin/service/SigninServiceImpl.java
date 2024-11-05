package edu.kh.daemoim.signin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.signin.mapper.SigninMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigninServiceImpl implements SigninService{
   
   private final SigninMapper mapper;
   
   /* 비밀번호 암호화 하기 */
   private final BCryptPasswordEncoder encoder;
   
   @Override
   public MyPage login(String memberId, String memberPw) {
      
      
      //아이디 일치하는 회원 정보 조회
      MyPage loginMember = mapper.login(memberId);
      
      
      if(loginMember == null) return null;
      
      
      if(!encoder.matches(memberPw, loginMember.getMemberPw())) {
         return null;
      }
      
      return loginMember;
   }
   @Override
		public int authoritycheck(String memberId) {
	   
		return mapper.authoritycheck(memberId);
		
		
	}
}
