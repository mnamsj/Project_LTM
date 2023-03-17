package com.ltm.web.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ltm.web.DataNotFoundException;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.Reply;
import com.ltm.web.repository.CboardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CboardService {
	
	private final CboardRepository cboardRepository;
		
		/*게시글 조회*/
		public List<Cboard> getList(){
			return this.cboardRepository.findAll();
		}
		
		public Cboard getCboard(Integer id) {
			Optional<Cboard> cboard = this.cboardRepository.findById(id);
			if(cboard.isPresent()) {
				return cboard.get();
			} else {
				throw new DataNotFoundException("cboard not found");
			}
		}
			
		/*게시글 등록*/
		public void create(String ctitle, String cbody, Member username, String tags) {
			Cboard c = new Cboard();
			c.setCtitle(ctitle);
			c.setCbody(cbody);
			c.setWdate(LocalDateTime.now());
			c.setUsername(username); // 작성자 : user
			c.setTags(tags); // 해시태그 
			this.cboardRepository.save(c);
		}
		
		/*게시글 페이징*/
		public Page<Cboard> getList(int page, String kw){
			List<Sort.Order> sorts = new ArrayList<>(); 
			sorts.add(Sort.Order.desc("wdate")); // 작성일 역순
			Pageable pageable = PageRequest.of(page, 20 , Sort.by(sorts));
			Specification<Cboard> spec = search(kw); // 검색
			return this.cboardRepository.findAll(spec, pageable);
		}
		/*검색*/
		private Specification<Cboard> search(String kw) {
	        return new Specification<>() {
	            private static final long serialVersionUID = 1L;
	            @Override
	            public Predicate toPredicate(Root<Cboard> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
	                query.distinct(true);  // 중복을 제거 
	                Join<Cboard, Member> u1 = q.join("username", JoinType.LEFT);
	                Join<Cboard, Reply> a = q.join("replyList", JoinType.LEFT);
	                Join<Reply, Member> u2 = a.join("username", JoinType.LEFT);
	                return cb.or(cb.like(q.get("ctitle"), "%" + kw + "%"),
	                        cb.like(q.get("cbody"), "%" + kw + "%"),
	                        cb.like(u1.get("username"), "%" + kw + "%"), 
	                        cb.like(a.get("rbody"), "%" + kw + "%"),
	                        cb.like(u2.get("username"), "%" + kw + "%"));
	            }
	        };
	    }
		
		
		
		/*게시글 수정*/
		public void modify(Cboard cboard, String ctitle, String cbody, String tags) {
			cboard.setCtitle(ctitle);
			cboard.setCbody(cbody);
			cboard.setMdate(LocalDateTime.now());
			cboard.setTags(tags);
			this.cboardRepository.save(cboard);
		}
		
		/*게시글 삭제*/
		public void delete(Cboard cboard) {
			this.cboardRepository.delete(cboard);
		}
		
		/*게시글 조회수*/
		@Transactional
		public int updateView(Integer id) {
			return this.cboardRepository.updateView(id);
		}
		
		/*게시글 추천*/
		public void vote(Cboard cboard, Member member) {
			cboard.getVoter().add(member);
			this.cboardRepository.save(cboard);
		}
}
