package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.service.EditBoardService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class EditBoardController {
	
	private final EditBoardService service;
	
	/* @PathVariable 사용 시 정규 표현식 사용이 가능함.
	 * {변수명:정규표현식} 형태로 작성함
	 * 정규표현식 형식은 찾아보면 됩니다
	 * */
	
	/** 게시글 작성 화면 전환
	 * @param boardCode:[0-9]+ : 정규표현식 : boardCode는 숫자 1글자 이상만 가능
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
			@PathVariable("boardCode") int boardCode) {
		// @PathVariable 지정 시 forward한 html 파일에서도 사용 가능!!!!!
		// requestScope에 저장됨
		return "board/boardWrite";
	}
	
	/** 게시글 등록
	 * @param boardCode : 게시판 종류 번호
	 * @param inputBoard : 커맨드객체(제출된 key값이 일치하는 필드에 값이 저장된 객체)
	 * @param loginMember : 로그인 한 회원 정보
	 * @param images : 제출된 file 타입 input태그 데이터
	 * @param ra : 리다이렉트 시 request scope로 값 전달
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
			@PathVariable("boardCode") int boardCode,
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra) {
		
		/* 전달된 파라미터 확인(debug 모드)
		 * file은 열어서 filename 있는지 확인해봐야함
		 * 입력 안한 데이터의 filename은 ''로 입력됨
		 * 
		 * boardCode는 pathvariable로 가져왔는데 inputBoard에도 데이터가 들어가짐
		 * 
		 * 제목, 내용, boardCode => inputBoard
		 * 
		 * List<MultipartFile> images의 size() == 제출된 file 타입의 input 태그 개수 == 5개
		 * 
		 * * 선택된 파일이 없더라도 빈칸으로 제출이 된다!!!
		 * ex)0,2,4 인덱스만 선택 -> 0,2,4는 제출된 파일이 있고 1,3은 빈칸으로 존재
		 *  */
		
		// 1. 작성자 회원 번호를 insertBoard에 세팅
		inputBoard.setMemberNo( loginMember.getMemberNo() );
		
		// 2. 서비스 호출 후 결과(작성된 게시글 번호) 반환 받기
		int boardNo = service.boardInsert(inputBoard, images);
		
		// 3. 서비스 결과에 따라 응답제어
		String path = null;
		String message = null;
		if(boardNo == 0) {
			path = "insert";
			message="게시글 작성 실패";
		} else {
			// path = "/board/" + boardCode + "/" + boardNo;
			path = "/board/" + boardCode;//목록조회주소(임시)
			message="게시글이 작성 되었습니다";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}

}
