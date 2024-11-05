package edu.kh.daemoim.main.controller;

import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.main.service.MainService;
import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@SessionAttributes("loginMember")
public class MainController {

    private final MainService service;

    // 시간 계산
    public String getTimeAgo(LocalDateTime latestChatDate) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(latestChatDate, now);
        long hours = ChronoUnit.HOURS.between(latestChatDate, now);
        long days = ChronoUnit.DAYS.between(latestChatDate, now);

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            return days + "일 전";
        }
    }
    
    
    @RequestMapping("/")
    public String mainPage(HttpSession session, Model model) {
        MyPage loginMember = (MyPage) session.getAttribute("loginMember");

        List<MainDTO> recommendedGroups = service.getRecommendedGroups();
        List<MainDTO> recentChatGroups = service.getRecentChatGroups();
        
        
        
        
        model.addAttribute("recommendedGroups", recommendedGroups);
        model.addAttribute("recentChatGroups", recentChatGroups);
        
        
        // 시간 차이 계산
        Map<Integer, String> recentChatTimes = new HashMap<>();
        for (MainDTO group : recentChatGroups) {
            recentChatTimes.put(group.getGroupNo(), getTimeAgo(group.getLatestChatDate()));
        }
        model.addAttribute("recentChatTimes", recentChatTimes);

        if (loginMember != null) {
            int memberNo = loginMember.getMemberNo();
            List<MainDTO> joinGroups = service.selectJoinGroups(memberNo);
            model.addAttribute("joinGroups", joinGroups);
        }
        log.info("로그인한 사용자 정보: {}", loginMember);
        return "common/main";
    }
    
    @RequestMapping("/groupMain/{groupNo}")
    public String groupMainPage(@PathVariable int groupNo, Model model) {
        MainDTO group = service.findGroupByNo(groupNo);
        model.addAttribute("group", group);
        return "groupMain"; 
    }
}

