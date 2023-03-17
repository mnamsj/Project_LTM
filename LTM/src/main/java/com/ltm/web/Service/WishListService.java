package com.ltm.web.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ltm.web.entity.Member;
import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.entity.playlist.WishList;
import com.ltm.web.repository.WishListRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WishListService {

	private final WishListRepository wishListRepository;
	private final PlayListService playListService;
	private final MemberService memberService;
	
	public WishList saveWishList(Integer memberId, Long plId) {
		
		//플레이리스트 조회
		PlayList selectPlayList = playListService.findOne(plId);
		
		//회원 조회
		Member selectMemeber = memberService.getMemberId(memberId);
		
		//위시리스트에 넣어주기. 준영속
		WishList saveWishList = WishList.createWishList(selectPlayList, selectMemeber);
		
		//저장
		return wishListRepository.save(saveWishList);
	}
	
	//위시리스트 조회
	public List<PlayList> findWl(Integer memberId){
		List<Long> selectWl = wishListRepository.findWishList(memberId);
		
		List<PlayList> arrayPl = new ArrayList<>();
		for (int i = 0; i < selectWl.size(); i++) {
			PlayList pl = playListService.findOne(selectWl.get(i));
			arrayPl.add(pl);
		}
		System.out.println("결과값: " + arrayPl.size());
		return arrayPl;
	}
	
	
}
