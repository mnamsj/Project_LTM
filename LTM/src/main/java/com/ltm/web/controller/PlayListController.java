package com.ltm.web.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ltm.web.Dto.PlayListFormDto;
import com.ltm.web.Service.MemberService;
import com.ltm.web.Service.PlSongService;
import com.ltm.web.Service.PlayListService;
import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.repository.PlSongRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/playlist")
public class PlayListController {

	private final PlayListService playListService;
	private final PlSongService plSongService;
	private final MemberService memberService;
	private final PlSongRepository plSongRepository;
	
	//플레이리스트 만들기
	@GetMapping("/new")
	public String createForm(Model model) {
		//로그인된 회원 정보 가져오기.
		//@RequestParam("memberId") Long memberId
		//Member member = memberService.findOne(memberId);
		//model.addAttribute("member",member);
		
		model.addAttribute("playListForm", new PlayListFormDto());
		return "playlist/createPlForm";
	}
	
	@PostMapping("/new")
	public String create(@Valid PlayListFormDto form, BindingResult result) {
		
		if(result.hasErrors()) {//에러가 있으면 다시 회원가입폼으로 이동
			return "playlist/createPlForm";
		}
		
		//Dto 생성 필요
		PlayList playList = new PlayList();
		playList.setTitle(form.getTitle());
		playList.setDiscription(form.getDiscription());
		playList.setMember(form.getMember());
			
		playListService.savePl(playList);
		return "redirect:/list";
		
	}
	
//	//전체 플레이리스트 조회
//	@GetMapping("/playlist/list")
//	public String showAll(Model model) {
//		
//		List<Member> members = memberService.findMembers();
//		List<PlayList> lists = playListService.findPl();
//		
//		model.addAttribute("lists",lists);
//		model.addAttribute("members",members);
//		
//		return "AllPlayList";
//	}
	//페이징 처리된 전체 플레이리스트 소현님.ver
	@GetMapping("/list")
    public String list(Model model, @RequestParam(value="page" , defaultValue="0") int page,
    		 @RequestParam(value = "kw", defaultValue = "") String kw) {
        //List<PlayList> playList = this.playlistService.getlist(); //레퍼지토리를 바로 불러와서 쓰지않고 서비스를 통해서 사용하도록 작성
        //model.addAttribute("playList2", playList);
    	
    	System.out.println("***************************");
    	System.out.println(page);
    	System.out.println(kw);
    	System.out.println("***************************");
    	
    	if("".compareTo(kw) == 0) {
    		Page<PlayList> paging = this.playListService.getlist(page);
    		model.addAttribute("paging",paging);
        	model.addAttribute("kw", kw);
            return "playlist/Pl_main";
    	}else {
    		Page<PlayList> paging = this.playListService.getlistkeyword(page,kw);
    		model.addAttribute("paging",paging);
        	model.addAttribute("kw", kw);
            return "playlist/Pl_main";
    	}
    
    }
	
	//플레이리스트 담긴 노래 조회
//	@GetMapping("/playlist/{id}/song")
//	public String plSongForm(@PathVariable("id") Long plId, Model model) {
//		PlayList playList = playListService.findOne(plId);
//		List<PlSong> songs = plSongService.findPlSongs(plId);
//		
//		model.addAttribute("playlist",playList);
//		model.addAttribute("songs",songs);
//		return "PlayListSongs";
//	}
	//소현님.ver
	@GetMapping("/{id}/song")
    public String pldetail(Model model, @PathVariable("id") Long plId) {
    	PlayList playlist = this.playListService.findOne(plId);
    	model.addAttribute("playList22",playlist);
    	
    	List<PlSong> songs = plSongService.findPlSongs(plId);//리스트로 담는 것도 생각해보기, plsong 연결필요
    	model.addAttribute("song22",songs);
    	return "playlist/Pl_detail";
    }
	
	//플레이리스트 노래 삭제
	@PostMapping("/{plId}/song")
	public String removeSong(@RequestParam("plSongId") Long plSongId) {
		PlSong plSong = plSongService.findOne(plSongId);
		plSongRepository.delete(plSong);
		return "redirect:/{plId}/song";
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
//	@GetMapping("/song") //API관련 서비스 로직 구현 필요
//	public String songList(Model model) {
//		
//		List<Song> songList = songService.findSongs();
//		
//		model.addAttribute("songList",songList);
//		return "SongList";
//	}
		
	//내 플레이리스트에 노래 넣기
	@GetMapping("/inputsong")
	public String inputSongDetail(@RequestParam("songTitle") String songTitle,
			                      @RequestParam("singer") String singer,
			                      @RequestParam("id") Long memberId, Model model) {
		
		List<PlayList> myPlayList = playListService.findMemberPl(memberId);
		
		model.addAttribute("myList",myPlayList);
		model.addAttribute("Title",songTitle);
		model.addAttribute("singer",singer);
		return "playlist/InputSong";
	}
	
	@PostMapping("/inputsong")
	public String inputSong(@RequestParam("plId") Long plId,
							@RequestParam("songTitle") String songTitle,
							@RequestParam("singer") String singer) {
			
		plSongService.plSong(plId, songTitle, singer);//담은 노래의 id

		return "redirect:/search";
	}
	
	
//	@GetMapping("/inputsong/{songtitle}/{singer}")
//	public String inputSongDetail(@PathVariable("songtitle") String songTitle,
//								  @PathVariable("singer") String singer,
//								  Model model) {
//		//회원 id와 연결된 플레이리스트 찾아오기 - 쿼리문 필요
//		//'리스트에 담기'했을 때, 그 회원의 플레이리스트들을 가져와야 함.
//		//List<PlayList> memberPl = playListService.findMemberPl(회원id);
//		//model.addattribute("memberPl",memberPl);
//		List<PlayList> lists = playListService.findPl();
//		
//		
//		model.addAttribute("lists",lists); //회원의 플레이리스트 출력
//		model.addAttribute("title",songTitle);
//		model.addAttribute("singer",singer);
//		return "InputSong";
//		
//	}
//	
//	
//	@PostMapping("/inputsong")
//	public String inputSong(@RequestParam("plId") Long plId,
//							@RequestParam("title") String songTitle,
//							@RequestParam("singer") String singer) {
//			
//		plSongService.plSong(plId, songTitle, singer);//담은 노래의 id
//
//		return "redirect:/song";
//	}
}
