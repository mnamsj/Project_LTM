package com.ltm.web.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Adboard {
	/* 공지사항 */
	
	/*게시번호*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/*제목*/
	@Column(length = 50)
	private String adtitle;
	
	/*내용*/
	@Column(columnDefinition = "TEXT")
	private String adbody; // 내용
	
	/*작성일*/
	private LocalDateTime wdate; //작성일
	
	/*수정일*/
	private LocalDateTime mdate; // 수정일
	
	/*조회수*/
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int view; // 조회수의 기본 값을 0으로 지정, null 불가 처리
	
	/*작성자*/
	@ManyToOne
	private Member username; // 작성자
	
	/*태그*/
	private String tags;

}
