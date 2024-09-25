package edu.kh.project.main.service;

import java.util.List;

import edu.kh.project.member.dto.Member;

public interface MainService {

	/** 멤버리스트 불러오기
	 * @return memberLsit
	 */
	List<Member> selectMemberList();

}
