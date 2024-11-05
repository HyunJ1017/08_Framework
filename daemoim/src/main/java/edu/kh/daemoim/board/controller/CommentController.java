package edu.kh.daemoim.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.board.service.CommentService;
import edu.kh.daemoim.myPage.dto.MyPage;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService service;

	 
	/** 댓글 등록
	 * @param comment
	 * @param loginMember
	 * @return
	 */
	@ResponseBody
	@PostMapping("comment")
	public int commentInsert(
		@RequestBody Comment comment,
		@SessionAttribute("loginMember") MyPage loginMember
		) {
		
		comment.setMemberNo(loginMember.getMemberNo());
		
		return service.commentInsert(comment);
	}
	
	/** 댓글 수정
	 * @param comment
	 * @param loginMember
	 * @return
	 */
	@ResponseBody
	@PutMapping("comment")
	public int commentUpdate(
		@RequestBody Comment comment,
		@SessionAttribute("loginMember") MyPage loginMember) {
		
		comment.setMemberNo(loginMember.getMemberNo());
		
		return service.commentUpdate(comment);
	}
	
	/** 댓글 삭제
	 * @param commentNo
	 * @param loginMember
	 * @return
	 */
	@ResponseBody
	@DeleteMapping("/comment")
	public int commentDelete(
		@RequestBody int commentNo,
		@SessionAttribute("loginMember") MyPage loginMember
		) {
		
		int memberNo = loginMember.getMemberNo();
		
		
		
		return service.commentDelete(commentNo, memberNo);
	}
	
	
	
}
