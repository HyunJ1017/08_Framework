package edu.kh.daemoim.main.mapper;

import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.myPage.dto.MyPage;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {

    // 추천 모임 목록 조회
    List<MainDTO> selectRecommendedGroups();

    // 로그인한 회원의 가입한 모임 조회
    List<MainDTO> selectJoinGroups(int memberNo);

    List<MainDTO> selectRecentChatGroups();

    List<MyPage> selectMemberImagesByGroupNo(int groupNo);
    
	MainDTO findGroupByNo(int groupNo);


}
