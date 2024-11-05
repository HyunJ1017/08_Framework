package edu.kh.daemoim.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.daemoim.report.service.ReportService;
import edu.kh.daemoim.siteManage.dto.StopMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {
	
	private final ReportService service;
	
	@PostMapping("invite")
	public String insertReport(
			@ModelAttribute StopMember member,
			@RequestHeader("referer") String referer,
			RedirectAttributes ra
		) {
		
		log.info("StopMember : {}", member.toString());
		
		service.insertReport(member);
		
		ra.addFlashAttribute("message", "신고 완료");
		
		return "redirect:/";
	}
}
