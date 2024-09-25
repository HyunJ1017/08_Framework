package edu.kh.project.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.dto.Member;

@Mapper
public interface MainMapper {

	List<Member> selectMemberList();

}
