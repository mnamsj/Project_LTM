package com.ltm.web.Service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ltm.web.DataNotFoundException;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.entity.playlist.WishList;
import com.ltm.web.repository.PlSongRepository;
import com.ltm.web.repository.PlayListRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayListService {

	private final PlayListRepository playListRepository;
	private final PlSongRepository plSongRepository;

	@Transactional
	public Long savePl(PlayList playList) {
		playListRepository.save(playList);
		return playList.getId();
	}

	// 전체 플레이리스트 조회
	public List<PlayList> findPl() {
		return playListRepository.findAll();
	}

	// 플레이리스트 조회
	public PlayList findOne(Long plId) {
		return playListRepository.findById(plId).orElseThrow(EntityNotFoundException::new);
	}

	// 플레이리스트 업데이트
	public PlayList updatePl(Long plId, String title, String discription) throws Exception {
		Optional<PlayList> selectPl = playListRepository.findById(plId);

		PlayList updatePlayList;
		if (selectPl.isPresent()) {
			PlayList playList = selectPl.get();

			playList.setTitle(title);
			playList.setDiscription(discription);

			updatePlayList = playListRepository.save(playList);
		} else {
			throw new Exception();
		}

		return updatePlayList;
	}

	// 플레이리스트 삭제
	public void deletePl(Long plId) throws Exception {
		Optional<PlayList> selectPl = playListRepository.findById(plId);

		if (selectPl.isPresent()) {
			PlayList playList = selectPl.get();

			playListRepository.delete(playList);
		} else {
			throw new Exception();
		}
	}
	
//	public String findImage(Long plId) {
//		return playListRepository.findPlayListImage(plId);
//	}

	// 내 플레이리스트 조회 + 노래 넣을 플레이리스트 목록 조회
	public List<PlayList> findMemberPl(Integer memberId) {
		return playListRepository.findMemberPlayList(memberId);
	}

	// 전체 플레이리스트
	public Page<PlayList> getlist(int page) {
		Pageable pageable = PageRequest.of(page, 9); // page는 조회할 페이지의 번호, 2는 한페이지에 보여줄 게시물의 개수
		return playListRepository.findAll(pageable);
		// 목록을 조회하여 리턴하는 getlist 메서드 추가. 컨트롤러에서 레포지토리를 사용했던 부분을 그대로 옮긴 것
		// 엔티티클래스와 데이터베이스가 직접맞닿아서 컨트롤러나 타임리프같은 템플릿 엔진에 전달하여 사용하는 것은 좋지 않기 때문에 서비스를 사용
	}

	// 검색 결과 플레이리스트
	public Page<PlayList> getlistkeyword(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 9); // page는 조회할 페이지의 번호, 2는 한페이지에 보여줄 게시물의 개수
		return playListRepository.findByKeyword(kw, pageable);
	}

	public PlayList getPlayList(Long id) {
		Optional<PlayList> playlist = this.playListRepository.findById(id);
		if (playlist.isPresent()) {
			return playlist.get();
		} else {
			throw new DataNotFoundException("playlist not found");
		}
	}
	
	//플레이리스트 노래목록 삭제하기
	public void deletePlsong(PlSong plSong) {
		this.plSongRepository.delete(plSong);
	}
	
	public PlSong getPlsong(Long id) {
		Optional<PlSong> plSong = this.plSongRepository.findById(id);
		if(plSong.isPresent()) {
			return plSong.get();
		} else {
			throw new DataNotFoundException("not found");
		}
	}
	public PlayList getPl(Long id) {
		Optional<PlayList> playList = this.playListRepository.findById(id);
		if(playList.isPresent()) {
			return playList.get();
		} else {
			throw new DataNotFoundException("not found");
		}
	}
	
	

}
