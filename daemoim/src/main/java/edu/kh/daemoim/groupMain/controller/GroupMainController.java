package edu.kh.daemoim.groupMain.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.service.GroupMainService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GroupMainController {

    private final GroupMainService service;

    /** 페이지 전환
     * @param model
     * @return
     */
    @GetMapping("groupMain")
    public String groupMain(Model model) {
        // 공지사항 목록을 미리 불러와서 model에 추가
        List<Notice> boardList = service.getBoardList();
        model.addAttribute("boardList", boardList);  // 모델에 추가하여 템플릿에서 사용
        return "groupMain/main";  // 뷰 이름 반환
    }

    /** 공지사항 목록 불러오기
     * @return
     */
    @GetMapping("/board/boardList")
    @ResponseBody  // JSON으로 응답s
    public List<Notice> selectBoardList() {
        return service.selectBoardList();
    }
}
