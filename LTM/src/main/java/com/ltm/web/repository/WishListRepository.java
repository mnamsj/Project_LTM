package com.ltm.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ltm.web.entity.playlist.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long>{

	//위시리스트 조회
	@Query(value = "select *"
			+ " from wish_list w"
			+ " where w.id_num = :memberId" , nativeQuery =true)
	public List<WishList> findWishList(@Param(value = "memberId") Integer memberId);
}
