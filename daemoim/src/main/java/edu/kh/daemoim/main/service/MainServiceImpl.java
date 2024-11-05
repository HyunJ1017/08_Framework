package edu.kh.daemoim.main.service;

import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.main.mapper.MainMapper;
import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final MainMapper mapper;

    // 추천 모임 목록 조회
    @Override
    public List<MainDTO> getRecommendedGroups() {
        List<MainDTO> groups = mapper.selectRecommendedGroups();
        
        // 각 모임의 멤버 이미지 조회 및 설정
        for (MainDTO group : groups) {
            List<MyPage> members = mapper.selectMemberImagesByGroupNo(group.getGroupNo());
            group.setMembers(members); // 조회한 멤버 목록 설정
        }
        return groups;
    }

    // 최근 채팅 모임 목록 조회
    @Override
    public List<MainDTO> getRecentChatGroups() {
        List<MainDTO> groups = mapper.selectRecentChatGroups();

        // 각 모임의 멤버 이미지 조회 및 설정
        for (MainDTO group : groups) {
            List<MyPage> members = mapper.selectMemberImagesByGroupNo(group.getGroupNo());
            group.setMembers(members); // 조회한 멤버 목록 설정
        }
        return groups;
    }

    // 로그인한 회원의 가입한 모임 조회
    @Override
    public List<MainDTO> selectJoinGroups(int memberNo) {
        List<MainDTO> groups = mapper.selectJoinGroups(memberNo);

        // 각 모임의 멤버 이미지 조회 및 설정
        for (MainDTO group : groups) {
            List<MyPage> members = mapper.selectMemberImagesByGroupNo(group.getGroupNo());
            group.setMembers(members); // 조회한 멤버 목록 설정
        }
        return groups;
    }

    @Override
    public MainDTO findGroupByNo(int groupNo) {
        MainDTO group = mapper.findGroupByNo(groupNo);
        List<MyPage> members = mapper.selectMemberImagesByGroupNo(groupNo);
        group.setMembers(members); // 조회한 멤버 목록 설정
        return group;
    }
}

