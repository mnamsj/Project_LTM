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

import com.ltm.web.Dto.AdboardFormDto;

import com.ltm.web.Service.AdboardService;

import com.ltm.web.Service.MemberService;

import com.ltm.web.entity.Adboard;

import com.ltm.web.entity.Member;


import lombok.RequiredArgsConstructor;

@RequestMapping("/adboard")
@RequiredArgsConstructor
@Controller
public class AdboardController {
	
	private final AdboardService adboardService;
	private final MemberService memberService;
	
	
		/*공지사항 조회 + 페이징 구현*/
		@GetMapping("/list")
		public String list(Model model, @RequestParam(value= "page", defaultValue = "0") int page) {
			Page<Adboard> paging = this.adboardService.getList(page);
			model.addAttribute("paging",paging);
			List<Adboard> adboardList = this.adboardService.getList();
			model.addAttribute("adboardList", adboardList);
			return "adboard/adboard_list";
		}
		
		/*공지사항 상세*/
		@GetMapping("/detail/{id}")
		public String detail(Model model, @PathVariable("id") Integer id,
				HttpServletRequest request,HttpServletResponse response) {
			
			Adboard adboard = this.adboardService.getAdboard(id);
			
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
					this.adboardService.updateView(id);
					oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
					oldCookie.setPath("/");
					oldCookie.setMaxAge(1); 							
					response.addCookie(oldCookie);
				}
			} else {
				this.adboardService.updateView(id);
				Cookie newCookie = new Cookie("postView", "[" + id + "]");
				newCookie.setPath("/");
				newCookie.setMaxAge(1); 								
				response.addCookie(newCookie);
			}
			
			model.addAttribute("adboard", adboard);
			return "adboard/adboard_detail";
		}
		
		/*공지사항 작성*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/create")
		public String adboardCreate(AdboardFormDto adboardForm, Principal principal) {
			return "adboard/adboard_form";
		}
			
		/*공지사항 등록*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/create")
		public String adboardCreate(@Valid AdboardFormDto adboardForm,
				BindingResult bindingResult, Principal principal){		
			if(bindingResult.hasErrors()) {
				return "adboard/adboard_form";
			}
			Member member = this.memberService.getMember(principal.getName());
			this.adboardService.create(adboardForm.getAdtitle(), adboardForm.getAdbody(), member, adboardForm.getTags());
			return "redirect:/adboard/list"; // 등록후 목록으로	
		}
		
		/*공지사항 수정*/
		@GetMapping("/mdate/{id}")
		public String adboardModify(AdboardFormDto adboardForm, @PathVariable("id") Integer id,
				Principal principal) {
			Adboard adboard = this.adboardService.getAdboard(id);
			if(!adboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
			}
			adboardForm.setAdtitle(adboard.getAdtitle());
			adboardForm.setAdbody(adboard.getAdbody());
			adboardForm.setTags(adboard.getTags());
			return "adboard/adboard_form";
		}
		
		/*공지사항 수정 저장*/
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/mdate/{id}")
		public String adboardModify(@Valid AdboardFormDto adboardForm, BindingResult bindingResult, @PathVariable("id")Integer id,
				Principal principal) {
			if(bindingResult.hasErrors()) {
				return "adboard/adboard_form";
			}
			Adboard adboard = this.adboardService.getAdboard(id);
			if(!adboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
			}
			this.adboardService.modify(adboard, adboardForm.getAdtitle(), adboardForm.getAdbody(), adboardForm.getTags());
			return String.format("redirect:/adboard/detail/%s", id);	
		}
		
		/*공지사항 삭제*/
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/delete/{id}")
		public String adboardDelete(Principal principal, @PathVariable("id") Integer id) {
			Adboard adboard = this.adboardService.getAdboard(id);
			if(!adboard.getUsername().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
			}
		
			this.adboardService.delete(adboard);
			return "redirect:/adboard/list";
		}
		
		/*templates 다중경로 설정*/
		@Bean
		public ClassLoaderTemplateResolver secondaryTemplateResolver() {
		    ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		    secondaryTemplateResolver.setPrefix("templates/adboard/");
		    secondaryTemplateResolver.setSuffix(".html");
		    secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		    secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		    secondaryTemplateResolver.setOrder(1);
		    secondaryTemplateResolver.setCheckExistence(true);
		        
		    return secondaryTemplateResolver;
		}
}
