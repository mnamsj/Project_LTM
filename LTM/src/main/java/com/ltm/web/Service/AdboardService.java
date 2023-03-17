package com.ltm.web.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ltm.web.DataNotFoundException;
import com.ltm.web.entity.Adboard;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.repository.AdboardRepository;
import com.ltm.web.repository.CboardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdboardService {
	
	private final AdboardRepository adboardRepository;
		
		/*공지사항 조회*/
		public List<Adboard> getList(){
			return this.adboardRepository.findAll();
		}
		
		public Adboard getAdboard(Integer id) {
			Optional<Adboard> adboard = this.adboardRepository.findById(id);
			if(adboard.isPresent()) {
				return adboard.get();
			} else {
				throw new DataNotFoundException("adboard not found");
			}
		}
			
		/*공지사항 등록*/
		public void create(String adtitle, String adbody, Member username, String tags) {
			Adboard ad = new Adboard();
			ad.setAdtitle(adtitle);
			ad.setAdbody(adbody);
			ad.setWdate(LocalDateTime.now());
			ad.setUsername(username); // 작성자 : user
			ad.setTags(tags); // 해시태그 
			this.adboardRepository.save(ad);
		}
		
		/*공지사항 페이징*/
		public Page<Adboard> getList(int page){
			List<Sort.Order> sorts = new ArrayList<>(); 
			sorts.add(Sort.Order.desc("wdate")); // 작성일 역순
			Pageable pageable = PageRequest.of(page, 20 , Sort.by(sorts));
			return this.adboardRepository.findAll(pageable);
		}
		
		/*공지사항 수정*/
		public void modify(Adboard adboard, String adtitle, String adbody, String tags) {
			adboard.setAdtitle(adtitle);
			adboard.setAdbody(adbody);
			adboard.setMdate(LocalDateTime.now());
			adboard.setTags(tags);
			this.adboardRepository.save(adboard);
		}
		
		/*공지사항 삭제*/
		public void delete(Adboard adboard) {
			this.adboardRepository.delete(adboard);
		}
		
		/*공지사항 조회수*/
		@Transactional
		public int updateView(Integer id) {
			return this.adboardRepository.updateView(id);
		}
}
