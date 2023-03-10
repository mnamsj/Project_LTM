package com.ltm.web.entity.playlist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.ltm.web.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WishList {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "wl_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id") 
	private Member member;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pl_id")
	private PlayList playList;


}
