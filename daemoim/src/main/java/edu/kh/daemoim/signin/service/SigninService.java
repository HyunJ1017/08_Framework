package edu.kh.daemoim.signin.service;

import edu.kh.daemoim.myPage.dto.MyPage;

public interface SigninService {

   // 로그인
   MyPage login(String memberId, String memberPw);

   int authoritycheck(String memberId);



}