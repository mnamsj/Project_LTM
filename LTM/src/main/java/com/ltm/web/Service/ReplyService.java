package com.ltm.web.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ltm.web.DataNotFoundException;
import com.ltm.web.entity.Cboard;
import com.ltm.web.entity.Member;
import com.ltm.web.entity.Reply;
import com.ltm.web.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {
	
	private final ReplyRepository replyRepository;
		
		/*댓글 작성*/
		public void create(Cboard cboard, String rbody, Member username) {
			Reply reply = new Reply();
			reply.setRbody(rbody);
			reply.setWdate(LocalDateTime.now());
			reply.setCboard(cboard);
			reply.setUsername(username);
			this.replyRepository.save(reply);
		}
		
		/*댓글 조회*/
		public Reply getReply(Integer id) {
			Optional<Reply> reply = this.replyRepository.findById(id);
			if(reply.isPresent()) {
				return reply.get();
			} else {
				throw new DataNotFoundException("reply noy found");
			}
		}	
			
		/*댓글 수정*/
		public void modify(Reply reply, String rbody) {
			reply.setRbody(rbody);
			reply.setMdate(LocalDateTime.now());
			this.replyRepository.save(reply);
		}
		
		/*댓글 삭제*/
		public void delete(Reply reply) {
			this.replyRepository.delete(reply);
		}
		
		/*댓글 페이징*/
		public Page<Reply> getList(int page){
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("wdate"));
			Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
			return this.replyRepository.findAll(pageable);
		}
		
		/*댓글 추천*/
		public void vote(Reply reply, Member member) {
			reply.getVoter().add(member);
			this.replyRepository.save(reply);
		}
}
