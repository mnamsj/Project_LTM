package com.ltm.web.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.ltm.web.Dto.CboardFormDto;
import com.ltm.web.Dto.ReplyFormDto;
import com.ltm.web.Service.CboardService;
import com.ltm.web.Service.MemberService;
import com.ltm.web.Service.ReplyService;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.Reply;

import lombok.RequiredArgsConstructor;

@RequestMapping("/cboard")
@RequiredArgsConstructor
@Controller
public class CboardController {
	
	private final CboardService cboardService;
	private final ReplyService replyService;
	private final MemberService memberService;
	
	
		/*게시글 조회 + 페이징 구현*/
		@GetMapping("/list")
		public String list(Model model, @RequestParam(value= "page", defaultValue = "0") int page,@RequestParam(value = "kw", defaultValue = "") String kw) {
			Page<Cboard> paging = this.cboardService.getList(page, kw);
			model.addAttribute("paging",paging);
			List<Cboard> cboardList = this.cboardService.getList();
			model.addAttribute("cboardList", cboardList);
			model.addAttribute("kw", kw);
			return "cboard/cboard_list";
		}
		
		/*게시글 상세 + 댓글 페이징*/
		@GetMapping("/detail/{id}")
		public String detail(Model model, @PathVariable("id") Integer id,ReplyFormDto replyForm,
				HttpServletRequest request,HttpServletResponse response,
				@RequestParam(value="page", defaultValue="0") int page) {
			
			Cboard cboard = this.cboardService.getCboard(id);
			Page<Reply> paging = this.replyService.getList(page);
			model.addAttribute("paging", paging);
			
			/*조회수 로직*/
			Cookie oldCookie = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("postView")) {
						oldCookie = cookie;
					}
				}
			}
			/*쿠키 시간*/
			if (oldCookie != null) {
				if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
					this.cboardService.updateView(id);
					oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
					oldCookie.setPath("/");
					oldCookie.setMaxAge(1); 							
					response.addCookie(oldCookie);
				}
			} else {
				this.cboardService.updateView(id);
				Cookie newCookie = new Cookie("postView", "[" + id + "]");				
				newCookie.setPath("/");
				newCookie.setMaxAge(1); 								
				response.addCookie(newCookie);
			}
			
			model.addAttribute("cboard", cboard);
			return "cboard/cboard_detail";
		}
		
		/*게시글 작성*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/create")
		public String cboardCreate(CboardFormDto cboardForm, Principal principal) {
			return "cboard/cboard_form";
		}
			
		/*게시글 등록*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/create")
		public String cboardCreate(@Valid CboardFormDto cboardForm,
				BindingResult bindingResult, Principal principal){		
			if(bindingResult.hasErrors()) {
				return "cboard/cboard_form";
			}
			Member member = this.memberService.getMember(principal.getName());
			this.cboardService.create(cboardForm.getCtitle(), cboardForm.getCbody(), member, cboardForm.getTags());
			return "redirect:/cboard/list"; // 등록후 목록으로		
		}
		
		/*게시글 수정*/
		@GetMapping("/mdate/{id}")
		public String cboardModify(CboardFormDto cboardForm, @PathVariable("id") Integer id,
				Principal principal) {
			Cboard cboard = this.cboardService.getCboard(id);
			if(!cboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
			}
			cboardForm.setCtitle(cboard.getCtitle());
			cboardForm.setCbody(cboard.getCbody());
			cboardForm.setTags(cboard.getTags());
			return "cboard/cboard_form";
		}
		
		/*게시글 수정 저장*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/mdate/{id}")
		public String cboardModify(@Valid CboardFormDto cboardForm, BindingResult bindingResult, @PathVariable("id")Integer id,
				Principal principal) {
			if(bindingResult.hasErrors()) {
				return "cboard/cboard_form";
			}
			Cboard cboard = this.cboardService.getCboard(id);
			if(!cboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
			}
			this.cboardService.modify(cboard, cboardForm.getCtitle(), cboardForm.getCbody(), cboardForm.getTags());
			return String.format("redirect:/cboard/detail/%s", id);	
		}
		
		/*게시글 삭제*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/delete/{id}")
		public String cboardDelete(Principal principal, @PathVariable("id") Integer id) {
			Cboard cboard = this.cboardService.getCboard(id);
			if(!cboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
			}
		
			this.cboardService.delete(cboard);
			return "redirect:/cboard/list";
		}
		
		/*게시글 추천*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/vote/{id}")
		public String question(Principal principal,@PathVariable("id")Integer id) {
			Cboard cboard = this.cboardService.getCboard(id);
			Member member = this.memberService.getMember(principal.getName());
			this.cboardService.vote(cboard, member);
			return String.format("redirect:/cboard/detail/%s", id);
		}
		
		/*templates 다중경로 설정*/
		@Bean
		public ClassLoaderTemplateResolver secondaryTemplateResolver() {
		    ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		    secondaryTemplateResolver.setPrefix("templates/cboard/");
		    secondaryTemplateResolver.setSuffix(".html");
		    secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		    secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		    secondaryTemplateResolver.setOrder(1);
		    secondaryTemplateResolver.setCheckExistence(true);
		        
		    return secondaryTemplateResolver;
		}
}
