package com.ltm.web.controller;

import java.security.Principal;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ltm.web.Dto.PlayListFormDto;
import com.ltm.web.Service.MemberService;
import com.ltm.web.Service.PlSongService;
import com.ltm.web.Service.PlayListService;
import com.ltm.web.api.SongImageApi;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.entity.playlist.WishList;
import com.ltm.web.repository.PlSongRepository;
import com.ltm.web.repository.PlayListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/playlist")
public class PlayListController {

	private final PlayListService playListService;
	private final PlSongService plSongService;
	private final MemberService memberService;
	private final PlSongRepository plSongRepository;
	private final SongImageApi songImageApi;

	// 플레이리스트 만들기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/new")
	public String createForm(Model model) {

		model.addAttribute("playListFormDto", new PlayListFormDto());
		return "playlist/createPlForm";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/new")
	public String create(@ModelAttribute("playListFormDto") @Valid PlayListFormDto form,
						 BindingResult result,Principal principal) {

		if (result.hasErrors()) {// 에러가 있으면 다시 회원가입폼으로 이동
			return "playlist/createPlForm";
		}

		Member member = this.memberService.getMember(principal.getName());
		
		// Dto 생성 필요
		PlayList playList = new PlayList();
		playList.setTitle(form.getTitle());
		playList.setDiscription(form.getDiscription());
		playList.setMember(member);

		
		playListService.savePl(playList);
		return "redirect:/main";
	}

	// 내 플레이리스트 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mylist")
	public String showAll(Model model, Principal principal) {

		Member member = this.memberService.getMember(principal.getName());
		
		List<PlayList> myList = playListService.findMemberPl(member.getIdNum());

		model.addAttribute("mylist", myList);

		return "playlist/MyPlayList";
	}

	// 플레이리스트 업데이트
	@GetMapping("/{id}/update")
	public String updatePl(@PathVariable("id") Long plId, Model model) {
		PlayList selectOne = playListService.findOne(plId);

		model.addAttribute("listInfo", selectOne);
		return "playlist/UpdatePlayList";
	}

	@PostMapping("/update")
	public String updateInfo(@RequestParam("id") Long plId, @RequestParam("title") String title,
			@RequestParam("discription") String discription) throws Exception {
		playListService.updatePl(plId, title, discription);

		return "redirect:/playlist/mylist";
	}

	// 플레이리스트 삭제
	@PostMapping("/delete")
	public String deletePl(@RequestParam("plId") Long plId) throws Exception {
		playListService.deletePl(plId);
		return "redirect:/playlist/mylist";
	}

	// 페이징 처리된 전체 플레이리스트
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		// List<PlayList> playList = this.playlistService.getlist(); //레퍼지토리를 바로 불러와서
		// 쓰지않고 서비스를 통해서 사용하도록 작성
		// model.addAttribute("playList2", playList);

		System.out.println("***************************");
		System.out.println(page);
		System.out.println(kw);
		System.out.println("***************************");

		if ("".compareTo(kw) == 0) {
			Page<PlayList> paging = this.playListService.getlist(page);
			model.addAttribute("paging", paging);
			model.addAttribute("kw", kw);
			return "playlist/Pl_main";
		} else {
			Page<PlayList> paging = this.playListService.getlistkeyword(page, kw);
			model.addAttribute("paging", paging);
			model.addAttribute("kw", kw);
			return "playlist/Pl_main";
		}

	}

	//플레이리스트 상세 페이지
	@GetMapping("/{id}/song")
	public String pldetail(Model model, @PathVariable("id") Long plId) {
		PlayList playlist = this.playListService.findOne(plId);
		model.addAttribute("playList22", playlist);

		List<PlSong> songs = plSongService.findPlSongs(plId);// 리스트로 담는 것도 생각해보기, plsong 연결필요
		model.addAttribute("song22", songs);
		return "playlist/Pl_detail";
	}

	// 플레이리스트 노래 삭제
	@PostMapping("/{plId}/song")
	public String removeSong(@RequestParam("plSongId") Long plSongId) {
		PlSong plSong = plSongService.findOne(plSongId);
		plSongRepository.delete(plSong);
		return "redirect:/playlist/{plId}/song";
	}

	
	//컨트롤러 이동 생각해보기
	// 내 플레이리스트에 노래 넣기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/inputsong")
	public String inputSongDetail(@RequestParam("songTitle") String songTitle, 
							      @RequestParam("singer") String singer,
							      Principal principal,
							      Model model) {
		
		Member member = this.memberService.getMember(principal.getName());

		//내 플레이리스트 조회
		List<PlayList> myPlayList = playListService.findMemberPl(member.getIdNum());
	
		model.addAttribute("myList", myPlayList);
		model.addAttribute("Title", songTitle);
		model.addAttribute("singer", singer);
		return "playlist/InputSong";
	}

	@PostMapping("/inputsong")
	public String inputSong(@RequestParam("plId") Long plId, 
					        @RequestParam("songTitle") String songTitle,
					        @RequestParam("singer") String singer) {

		List<String> songImage = songImageApi.getImage(songTitle, singer);		
		plSongService.plSong(plId, songTitle, singer, songImage.get(0));// 담은 노래의 id
		
		PlayList playList = playListService.findOne(plId);
		if(playList.getImage() == null) {
			String plImage = songImage.get(1);
			playList.setImage(plImage);
			playListService.savePl(playList);
		}

		return "redirect:/search";
	}
	
	//플레이리스트에서 노래삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{plId}/{id}")
	public String plSongDelete(@PathVariable("plId") Integer plId, @PathVariable("id") Integer id) {
		System.out.println("4444444444444");
		
//		PlayList playList = this.playListService.getPl(Long.valueOf(plId));
		PlSong plSong = this.playListService.getPlsong(Long.valueOf(id));
		
		this.playListService.deletePlsong(plSong);
		return "redirect:/main";
	}
	
	

}
