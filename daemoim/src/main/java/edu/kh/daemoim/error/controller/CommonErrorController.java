package edu.kh.daemoim.error.controller;



import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller 
public class CommonErrorController implements ErrorController {

	
	
	/** 공용 예외 처리 메서드
	 * @param model
	 * @param req
	 * @return 
	 */
	@RequestMapping("error")
	public String errorHandler(Model model, HttpServletRequest req) {
		
		// 응답 상태 코드 얻어오기
		Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int statusCode = Integer.parseInt(status.toString());

		// 에러 메시지 얻어오기
		Object message = req.getAttribute(RequestDispatcher.ERROR_MESSAGE);
		String errorMessage
				= (message != null) ? message.toString()
																								: "알 수 없는 오류 발생";
		
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("statusCode", statusCode);
		
		return "error/common-error";
	}
	
	
	
	
}
