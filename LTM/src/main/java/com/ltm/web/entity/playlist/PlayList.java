package com.ltm.web.entity.playlist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ltm.web.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayList {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pl_id")
	private Long id;
	
	@Column(name = "pl_title")
	private String title;
	
	private String discription;
	
	private String image;
	
	@OneToOne(mappedBy = "playList",fetch = FetchType.LAZY)
	private WishList wishList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idNum")
	private Member member;
	
	@OneToMany(mappedBy = "playList", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PlSong> plSongs = new ArrayList<>();
	
	
	//==연관관계 메서드==//
	public void addPlSongs(PlSong plSong) {
		this.getPlSongs().add(plSong);
		plSong.setPlayList(this);
	}
	
}
