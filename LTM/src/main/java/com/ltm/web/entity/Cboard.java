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
public class Cboard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 게시번호
	
	@Column(length = 50)
	private String ctitle; // 제목
	
	@Column(columnDefinition = "TEXT")
	private String cbody; // 내용
	
	private LocalDateTime wdate; //작성일
	
	private LocalDateTime mdate; // 수정일
	
	@OneToMany(mappedBy = "cboard", cascade = CascadeType.REMOVE)
	private List<Reply> replyList;

	@Column(columnDefinition = "integer default 0", nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
	private int view;
	
	@ManyToOne
	private Member nickname; // 작성자
	
	@ManyToMany // 하나의 질문에 여러사람이 추천할 수 있고 한 사람이 여러 개의 질문을 추천할 수 있다
	Set<Member> voter; // 추천인 (set은 중복을 허용하지 않는 자료형)

}
