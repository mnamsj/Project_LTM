package com.ltm.web.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ltm.web.DataNotFoundException;
import com.ltm.web.constant.MemberRole;
import com.ltm.web.entity.Member;
import com.ltm.web.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Member create(String username, String password,
			/* String nickname, */ String email, String phone, String birth, LocalDateTime joindate, MemberRole role) {
		Member member = new Member();
		member.setUsername(username);
		member.setPassword(passwordEncoder.encode(password));
//		member.setNickname(nickname);
		member.setEmail(email);
		member.setPhone(phone);
		member.setBirth(birth); 
		member.setJoindate(LocalDateTime.now());
		member.setRole(MemberRole.MEMBER);
		this.memberRepository.save(member);
		return member;
	}
	
	
	// member 조회 (2023-02-27)
	public Member getMember(String username) {
		Optional<Member> member = this.memberRepository.findByUsername(username);
		if(member.isPresent()) {
			return member.get();
		} else {
			throw new DataNotFoundException("member not found");
		}
	}
	
	//플레이리스, 위시리스트 조회할때 사용되는 회원넘버
	public Member getMemberId(Integer idNum) {
		Optional<Member> member = this.memberRepository.findByIdNum(idNum);
		if(member.isPresent()) {
			return member.get();
		} else {
			throw new DataNotFoundException("member not found");
		}
	}
	
}
