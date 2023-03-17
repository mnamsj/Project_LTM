package com.ltm.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ltm.web.constant.MemberRole;
import com.ltm.web.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	Optional<Member> findByUsername(String username);
	
	Optional<Member> findByIdNum(Integer idNum);
}
