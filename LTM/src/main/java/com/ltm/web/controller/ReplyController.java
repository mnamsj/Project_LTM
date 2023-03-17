package com.ltm.web.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ltm.web.Dto.ReplyFormDto;
import com.ltm.web.Service.CboardService;
import com.ltm.web.Service.MemberService;
import com.ltm.web.Service.ReplyService;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.Reply;

import lombok.RequiredArgsConstructor;

@RequestMapping("/reply")
@RequiredArgsConstructor
@Controller
public class ReplyController {
	
	private final CboardService cboardService;
	private final ReplyService replyService;
	private final MemberService memberService;
	
	
		/*댓글 작성*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/create/{id}") // 댓글의 id
		public String createReply(Model model, @PathVariable("id") Integer id,
			@Valid ReplyFormDto replyForm, BindingResult bindingResult, Principal principal) {
			Cboard cboard = this.cboardService.getCboard(id);
			Member member = this.memberService.getMember(principal.getName());
			if(bindingResult.hasErrors()) {
				model.addAttribute("cboard", cboard);
				return "cboard/cboard_detail";
			}
			this.replyService.create(cboard, replyForm.getRbody(), member);
			return String.format("redirect:/cboard/detail/%s", id);
			
		}

		/*댓글 수정 요청*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/mdate/{id}")
		public String replyModify(Principal principal, ReplyFormDto replyForm, @PathVariable("id")Integer id) {
		Reply reply = this.replyService.getReply(id);
			if(!reply.getUsername().getUsername().equals(principal.getName())) {
				  throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
			  }
			  replyForm.setRbody(reply.getRbody());
			  return "cboard/reply_form";
		  }
		 
		
		/*댓글 수정 처리*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/mdate/{id}")
		public String replyModify(Principal principal, @Valid ReplyFormDto replyForm, BindingResult bindingResult,
				@PathVariable("id") Integer id) {
			if(bindingResult.hasErrors()) {
				return "reply_form";
			}
			Reply reply = this.replyService.getReply(id);
			if(!reply.getUsername().getUsername().equals(principal.getName())){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
			}
			{
			this.replyService.modify(reply, replyForm.getRbody());
			return String.format("redirect:/cboard/detail/%S", reply.getCboard().getId());
			
			}
		}
		
		/*댓글 삭제*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/delete/{id}")
		public String replyDelete(Principal principal, @PathVariable("id") Integer id) {
			Reply reply = this.replyService.getReply(id);
			if(!reply.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			this.replyService.delete(reply);
			return String.format("redirect:/cboard/detail/%s",
					reply.getCboard().getId());
		
		}
		
		/*댓글 추천*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/vote/{id}")
		public String replyVote(Principal principal, @PathVariable("id")Integer id) {
			Reply reply = this.replyService.getReply(id);
			Member member = this.memberService.getMember(principal.getName());
			this.replyService.vote(reply, member);
			return String.format("redirect:/cboard/detail/%s", reply.getCboard().getId());
		}
}
