package edu.kh.daemoim.signin.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.myPage.dto.MyPage;

@Mapper
public interface SigninMapper {

   MyPage login(String memberId);

   int authoritycheck(String memberId);


}
