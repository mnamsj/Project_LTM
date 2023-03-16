package com.ltm.web.Service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ltm.web.entity.playlist.PlSong;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.repository.PlSongRepository;
import com.ltm.web.repository.PlayListRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PlSongService {

	private final PlayListRepository playListRepository;
	private final PlSongRepository plSongRepository;
	
	@Transactional
	public PlSong plSong(Long plId,String songTitle,String singer, String image) {
		//엔티티 조회
		PlayList playList = playListRepository.findById(plId)
				.orElseThrow(EntityNotFoundException::new);

		//플레이리스트를 연결해주는 노래 저장 공간 생성
		PlSong plSong = PlSong.createPlSong(playList,songTitle,singer,image);
		
		//저장
		playList.addPlSongs(plSong);
		
		return plSongRepository.save(plSong);
	}
	
	public List<PlSong> findPlSongs(Long plId){
		return plSongRepository.findSongList(plId);
	}
	
	public PlSong findOne(Long plSongId) {
		return plSongRepository.findById(plSongId)
				.orElseThrow(EntityNotFoundException::new);
	}
	
}
