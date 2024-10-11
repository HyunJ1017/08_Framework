package edu.kh.project.board.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.board.dto.Comment;
import edu.kh.project.board.service.CommentService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

/*@Controller*/
@RestController	// @Controller + @ResponseBody
								// 비동기요청 전용 컨트롤러
								// 리턴되는 모든 값이 있는 그대로 호출부로 반환
/* @SessionAttributes({"loginMember"}) */
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService service;

	/** 댓글 등록
	 * @param comment :
	 *  요청시 body에 JSON 형태로 담겨저 제출된 데이터를
	 *  HttpMessageConverter가 DTO로 변환한 객체
	 *  (boardNo, commentContent, parentCommentNo)
	 * @param loginMember : 로그인회원정보
	 * @return : 삽입된 댓글 번호
	 */
	@PostMapping("comment")	// POST == CREAT/INSERT
	public int commentInsert(
			@RequestBody Comment comment,
			@SessionAttribute("loginMember") Member loginMember) {
		
		// 로그인된 회원번호 comment 에 세팅
		comment.setMemberNo( loginMember.getMemberNo() );
		
		return service.commentInsert(comment);
	}
	
	/* @ResponseBody */
	/** 댓글 수정하기
	 * @param comment : 수정할 댓글
	 * @param loginMember : 로그인한 멤버
	 * @return 0||1
	 */
	@PutMapping("comment")	// PUT == update
	public int commentUpdate(
			@RequestBody Comment comment,
			@SessionAttribute("loginMember") Member loginMember) {
		comment.setMemberNo(loginMember.getMemberNo());
		return service.commentUpdate(comment);
	}
	
	/* @ResponseBody */
	/** 댓글 삭제
	 * @return
	 */
	@DeleteMapping("comment")	// DELETE == DELETE
	public int commentDelete(
			@RequestBody int commentNo,
			@SessionAttribute("loginMember") Member loginMember) {
		
		Comment comment = Comment.builder().commentNo(commentNo).memberNo(loginMember.getMemberNo()).build();
		
		return service.commentDelete(comment);
	}
	
}
