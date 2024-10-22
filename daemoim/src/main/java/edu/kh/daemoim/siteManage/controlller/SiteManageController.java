package edu.kh.daemoim.siteManage.controlller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.siteManage.service.SiteManageService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SiteManageController {

	private final SiteManageService service;
	
	@GetMapping("siteManage")
	public String siteManage() {
		return "siteManage/main";
	}
	
	
}
