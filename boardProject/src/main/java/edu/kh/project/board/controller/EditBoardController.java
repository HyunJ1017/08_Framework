package edu.kh.project.board.controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import edu.kh.project.board.service.BoardService;
import edu.kh.project.board.service.EditBoardService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@Slf4j
@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class EditBoardController {
	
	private final EditBoardService service;
	
	// 수성시 상세조회 서비스를 호출하기 위한 객체 의존성 주입
	private final BoardService boardService;
	
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
			path = "/board/" + boardCode + "/" + boardNo;
			message="게시글이 작성 되었습니다";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	
	/** 게시글 삭제 
	 * - DB에서 boardNo, memberNo가 일치하는
	 *   "BOARD" TABLE의 행의 BOARD_DEL_FL 컬럼 값을 'Y'로 변경
	 * @param boardNo
	 * @param loginMember
	 * @param ra
	 * @param referer
	 * @return 
	 *  - 삭제 성공 시 : "삭제 되었습니다" 메시지 전달
	 *                    + 해당 게시판 목록으로 redirect
	 *                    
	 *  - 삭제 실패 시 : "삭제 실패" 메시지 전달
	 *  								  + 삭제 하려던 게시글 상세조회 페이지
	 *                              + 해당 게시판 목록으로 redirect
	 *                    
	 *  - 삭제 실패 시 : "삭제 실패" 메시지 전달
	 *  								  + 삭제 하려던 게시글 상세조회 페이지
	 *                      redirect
	 */
	@PostMapping("delete")
	public String postMethodName(
			@RequestParam("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			@RequestHeader("referer") String referer
			) {
		
		log.debug("referer : {}", referer);
		// 브라우저가 요청을 보낼때 같이보내는 데이터 중에서 요청주소를 얻어옴
		// referer : http://localhost/board/1/2020
		
		String boardCode = referer.split("/")[4];
		
		Board board = Board.builder()
				.boardNo(boardNo)
				.memberNo(loginMember.getMemberNo())
				.build();
		
		int result = service.boardDelete(board);
		
		// 결과에 따른 경로수정
		
		String path = null;
		String massage = null;
		
		/*정규표현식으로 주소찾기*/
		String regExp = "/board/[0-9]+";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(referer);
		
		if(matcher.find()) {
			path = matcher.group();
		}
		
		if(result > 0) {
			massage = boardNo + "번 게시글이 삭제 되었습니다.";
		} else {
			path = path + "/" + boardNo;
			massage = "삭제 실패.";
		}
		ra.addFlashAttribute("message", massage);
		
		return "redirect:" + path;
	}
	
	/** 게시글 수정 화면
	 * @param boardCode   : 게시판 종류
	 * @param boardNo     : 수정할 게시글 번호
	 * @param loginMember : 로그인한 회원 정보(session)
	 * @param ra          : redirect시 requestScope로 데이터 전달
	 * @param model       : forward 시 requestScope로 데이터 전달
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/updateView")
	public String updateView(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			Model model) {
		
		Map<String, Integer> map = Map.of("boardCode", boardCode, "boardNo", boardNo);
		
		Board board = boardService.selectDetail(map);
		
		// 게시글이 존제하지 않는경우
		if(board == null) {
			ra.addFlashAttribute("message", "해당 게시글이 존재하지 않습니다.");
			return "redirect:/board/" + boardCode;
		}
		
		// 게시글 작성자가 로그인한 회원이 아닌경우
		if(board.getMemberNo() != loginMember.getMemberNo()) {
			ra.addFlashAttribute("message", "작성자만 수정 가능합니다.");
			return String.format("redirect:/board/%d/%d", boardCode, boardNo);
		}
		
		// 게시글도 있고, 로그인한 회원도 맞으면 수정화면으로 foward
		model.addAttribute("board", board);
		return "board/boardUpdate";
	}
	
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String update(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			@RequestParam(value = "deleteOrderList", required = false) String deleteOrderList,
			RedirectAttributes ra) {
		
		// 1. 커맨드 객체 inputBoard에 로그인한 회원번호 만 추가
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// inputBoard에 세팅된 값
		// boardNo, boardTitle, boardContent, memberNo, boardCode
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환
		int result = service.boardUpdate(inputBoard, images, deleteOrderList);
		
		String message = null;
		if(result > 0) {
			message = "게시글이 수정 되었습니다.";
		} else {
			message = "게시글 수정 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return String.format("redirect:/board/%d/%d", boardCode, boardNo);
	}
	

}
/*

[문자 추출하기]

input  = http://localhost/board/2/2018
result = /board/2

1) split() ***
String[] arr = input.split("/");
System.out.println(Arrays.toString(arr));

String result1 = "/" + arr[3] + "/" + arr[4];
System.out.println(result1);


2) substring() ***
int start = input.indexOf("/board");
int end   = input.lastIndexOf("/");

								// start 이상 end 미만
String result2 = input.substring(start, end);
System.out.println(result2);

3) 정규표현식 ***
String regExp = "/board/[0-9]+";

// 정규식이 적용되 낮바 객체
Pattern patern = Pattern.compile(regExp);

// input에서 정규식과 일치하는 부분을 찾아 저장하는 객체
Matcher matcher = pattern.matcher(input);

if(matcher.find()){	// 일치하는 부분을 찾은 경우
	String result3 = matcher.group();
	System.out.println(result3);
}
*/