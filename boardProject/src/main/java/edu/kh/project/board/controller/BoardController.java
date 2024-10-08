package edu.kh.project.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Pagination;
import edu.kh.project.board.service.BoardService;
import edu.kh.project.member.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j // 로그 남기기용
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

	private final BoardService service;
	
	/** 게시글 목록 조회
	 * @param boardCode : 게시판 종류 번호
	 * @param model : forward시 데이터 전달하는 용도의 객체
	 * @param cp : 현재 조회하려는 목록의 페이지 번호
	 *             (필수 아님, 없으면 1)
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(
			@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
			Model model) {
		
		// 서비스 호출 후 결과 반환
		// 목록 조회인데 Map 으로 반환 받는 이유
		// - 서비스에서 여러 결과를 묶어서 받아와야 해서
		// -- 메서드는 결과를 1개만 반환할 수 있음
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		// map에 묶여있는 값 풀어놓기
		List<Board> boardList = (List<Board>) map.get("boardList");
		Pagination pagination = (Pagination) map.get("pagination");
		
		// 정상 조회되었는지 log 확인
//		for(Board b : boardList) log.debug(b.toString());
//		log.debug(pagination.toString());
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagination", pagination);
		
		return "board/boardList";
	}
	
	/** 게시글 상세 조회
	 * @param boardCode   : 게시판 종류
	 * @param boardNo     : 게시글 번호
	 * @param model       : forward 시 request scope 값 전달 객체
	 * @param ra				  : redirect시 request scope 값 전달 객체
	 * @param loginMember : 로그인한 회원정보, 로그인 안되어 있으면 null
	 * @param req         : 요청관련 데이터를 담고 있는 객체(쿠키포함)
	 * @param resp        : 응답방법을 담고있는 객체
	 *                         (쿠키생성, 쿠키를 클라이언트에게 전달)
	 * @return
	 * @throws ParseException 
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			Model model,
			RedirectAttributes ra,
			@SessionAttribute(value = "loginMember", required=false) Member loginMember, //-> 비 로그인 회원 접근할수 있게
			HttpServletRequest req,
			HttpServletResponse resp
			) throws ParseException {
		
		// 1. sql 수행에 필요한 파라미터들 Map으로 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		/* 로그인이 되어있는 경우 memberNo을 map에 추가 */
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo() );
		}
		
		// 2) 서비스 호출 후 결과 반환받기
		Board board = service.selectDetail(map);
		
		/* 게시글 상세조회 결과가 없을경우 */
		if(board == null) {
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
			return "redirect:/board/"+boardCode;
		}
		
		/* --------- 조회수 증가 시작 --------- */
		// 로그인한 회원이 작성한 글이 아닌경우
		if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo() ) {
			
			// 요청에 담겨있는 모든 쿠키 얻어오기
			Cookie[] cookies = null;
			Cookie c = null;
			if(req.getCookies() != null) {
			cookies = req.getCookies(); //Cookie : jakarta.servlet.http.Cookie;
			
				for(Cookie temp : cookies) {
					// Cookie는 Map형식("name"="value")
					
					// 클라이언트로 부터 전달받은 쿠키중 "readBoardNo"가 있는경우
					if(temp.getName().equals("readBoardNo") ) {
						c = temp;
						break;
					}
					
				}// for end
				
			}
			
			// 서비스 호출 결과 저장
			int result = 0;
			
			
			if(c == null) {
				// 쿠키 중에 "readBoardNo"가 없을 경우 새 쿠키 생성
				
				// 쿠키형상 : readBoardNo = [200][152][413][202]...
				c = new Cookie("readBoardNo", "["+ boardNo +"]"); // 쿠키에 ',' '.' '/' '-' '_' 못씀
				
				// DB에서 해당 게시글의 조회 수를 1 증가시키는 서비스 호출
				result = service.updateReadCount(boardNo);
				
			} else {
				// 쿠키 중에 "readBoardNo" 가 있을경우
				
				// 현재 읽은 게시글 번호가 쿠키에 없다면, 처음읽은 글이라면
				if(c.getValue().contains(boardNo + "") == false) {
					
					c.setValue(c.getValue() + "[" + boardNo + "]");
					
					// DB에 조회수 증가
					result = service.updateReadCount(boardNo);
				}// if end
			}// if end
			
			// 조회수 증가 후 동기화
			if(result > 0 ) {
				
				board.setReadCount(board.getReadCount() +1);
				
				// 어떤 주소 요청 시 서버로 전달될지 지정
				c.setPath("/"); // "/"이하 모든 요청에 쿠키 전달
				
				/* 쿠키의 수명 지정 */
				// 다음날 00시 00분 00초가 되면 삭제
				// => 오늘 23시 59분 59초까지 유지
				
				// 다음날 00시 00분 00초까지 남은 시간을 계산해서 쿠키에 세팅

				// Calendar 객체 : 시간을 저장하는 객체
				// Calendar.getInstance() : 코드 실행시 시간이 담긴 객체를 반환해주는 메서드
				Calendar calendar = Calendar.getInstance();
				// 날자데이터를 2분 더하기
				calendar.add(calendar.MINUTE, 5);
				// 날자데이터를 1일 더하기
//				 calendar.add(calendar.DATE, 1);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				// java.util
				Date date = new Date(); // 현제시간
				
				// calendar: 내일(24시간 후)
				// temp : Calendar -> Date 타입으로 변환
				Date temp = new Date(calendar.getTimeInMillis());
				
				// sdf.format(temp) == "2024-10-09"  (10월 8일 기준)
				// sdf.parse( "2024-10-09" ) == 2024-10-09 00:00:00
				Date midnight = sdf.parse( sdf.format(temp) );
				
				// 다음날 자정 - 현제시간의 결과를 ms 단위로 얻어오기
				// Date.getTime() : 시간을 long 반환
				// 다음날 00시 00분 00초까지 남은시간을 계산 ( ms에서 1000 나눴으니까 초단위)
				long diff = (midnight.getTime() - date.getTime() ) / 1000;
				
				// 쿠키 수명 설정
				c.setMaxAge((int)diff);
				
				// 응답객체에 쿠키를 추가해서 응답시 클라이언트에게 전달할수 있게 세팅
				resp.addCookie(c);
				
			}
			
			
		}
		
		
		/* --------- 조회수 증가 끝!! --------- */
		
		model.addAttribute("board", board);
		
		// 게시판 이미지를 출력하기위한 이미지리스트의 이미지 시작번호(썸네일 구분)
		int start = 0;
		
		// 조회된 이미지 목록이 있을 경우
		if ( board.getImageList().isEmpty() == false) {
			// 썸네일이 있을 경우
			if(board.getThumbnail() != null) {
				start = 1;
			}
		}
		
		model.addAttribute("start", start);
		
		return "board/boardDetail";
	}
	
	/** 좋아요 체크, 해제
	 * @param boardNo     : 좋아요를 누른 게시글 번호
	 * @param loginMember : 로그인한 멤버의 멤버번호
	 * @return map : (check,clear / 좋아요 개수)
	 */
	@ResponseBody
	@PostMapping("like")
	public Map<String, Object> boardLike(
			@RequestBody int boardNo,
			@SessionAttribute("loginMember") Member loginMember) {
		return service.boardLike(boardNo, loginMember.getMemberNo());
	}
	
	
}