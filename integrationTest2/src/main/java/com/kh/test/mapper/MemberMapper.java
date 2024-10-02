package com.kh.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.test.dto.Member;

@Mapper
public interface MemberMapper {

	List<Member> selectAllList();

}
