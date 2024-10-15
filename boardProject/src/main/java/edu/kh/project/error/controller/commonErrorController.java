package edu.kh.project.error.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// implements ErrorController : spring에서 기본제공해주는걸 사용자가 직접 만들어 덮어씌울수 있음
@Controller // 컨트롤러명시 + Bean등록
public class commonErrorController implements ErrorController{
	
	// [동작 순서]
	
	// @ControllerAdvice에서 일치하는 예외처리 메서드 찾기
	// -> 없으면 ErrorController 구현객체가 처리
	/** 공용 예외 처리 메서드
	 * @param model
	 * @param req
	 * @return
	 */
	@RequestMapping("error")
	public String errorHandler(Model model, HttpServletRequest req) {
		
		// 응답 상태 코드 얻어오기
		Object status = req.getAttribute( RequestDispatcher.ERROR_STATUS_CODE );
		
		int statusCode = Integer.parseInt(status.toString()); // int로 변환 해도되고 안해도됨
		
		// 에러 메세지 얻어오기
		Object message = req.getAttribute( RequestDispatcher.ERROR_MESSAGE );
		
		String errorMessage = (message != null) ? message.toString()
																						: "알 수 없는 오류 발생";
		
		// 끝
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("statusCode", statusCode);
		
		return "error/common-error";
	}

}
