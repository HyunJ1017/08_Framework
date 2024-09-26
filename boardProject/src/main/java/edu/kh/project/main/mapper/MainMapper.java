package edu.kh.project.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.member.dto.Member;

@Mapper
public interface MainMapper {

	List<Member> selectMemberList();

	Member directLogin(int memberNo);

	int resetPw(
			@Param("memberNo") int memberNo,
			@Param("encPw") String encPw);

	int changeStatus(int memberNo);
	
	
	
}
