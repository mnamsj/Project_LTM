package com.ltm.web.entity.playlist;

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
public class WishList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wl_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idNum")
	private Member member;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pl_id")
	private PlayList playList;

	// ==생성 메서드==//
	// 위시리스트 생성
	public static WishList createWishList(PlayList playList, Member memberId) {
		WishList wishList = new WishList();
		wishList.setPlayList(playList);
		wishList.setMember(memberId);

		return wishList;
	}

}
