package com.ltm.web.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ltm.web.Service.MemberService;
import com.ltm.web.Service.PlayListService;
import com.ltm.web.Service.WishListService;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.entity.playlist.WishList;
import com.ltm.web.repository.PlayListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WishListController {

	private final WishListService wishListService;
	private final MemberService memberService;
	private final PlayListService playListService;
	
	//플레이리스트를 위시리스트에 넣을 때
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/getwishlist")
	public String getWishList(@RequestParam("plId") Long plId, Principal principal) {
		
		//로그인된 회원 조회
		Member member = this.memberService.getMember(principal.getName());

		wishListService.saveWishList(member.getIdNum(), plId);
		
		return "redirect:/main"; 
	}
	
	//내 위시리스트 페이지
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/wishlist")
	public String showWishList(Principal principal, Model model) {
		
		//로그인된 회원 조회
		Member member = this.memberService.getMember(principal.getName());
		
		List<PlayList> findWl = wishListService.findWl(member.getIdNum());
		
		model.addAttribute("wishlist",findWl);
		model.addAttribute("memberInfo", member);
		return "playlist/WishList";
	}
	
	
	//위시리스트에서 플레이리스트 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String wl_plDelete(Principal principal, @RequestParam("id") Integer id) {
		WishList wishList = this.wishListService.getWishList(Long.valueOf(id));
	
		this.wishListService.deletePl(wishList);
		return "redirect:/main";
	}
	

}
