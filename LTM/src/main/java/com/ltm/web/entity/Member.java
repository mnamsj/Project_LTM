package com.ltm.web.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ltm.web.constant.MemberRole;
import com.ltm.web.entity.playlist.PlayList;
import com.ltm.web.entity.playlist.WishList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idNum;
	
	@Column(unique = true)
	private String username;
	
	private String password;

	@Column(nullable = false)
	private String email;
	
	private String phone;
	
	@Column(nullable = false)
	private String birth;
	
	@Column(nullable = false)
	private LocalDateTime joindate; //테스트
	
	@Enumerated(EnumType.STRING)
	private MemberRole role;
	
	//위시리스트
	@OneToMany(mappedBy = "member")
	private List<WishList> wishList = new ArrayList<>();
	
	//플레이리스트
	@OneToMany(mappedBy = "member")
	private List<PlayList> playList = new ArrayList<>();
	


}
