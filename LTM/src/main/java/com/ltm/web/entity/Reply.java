package com.ltm.web.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Reply {
	
	/*댓글번호*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/*댓글*/
	@Column(columnDefinition = "TEXT")
	private String rbody;
	
	/*작성일*/
	private LocalDateTime wdate;
	
	/*수정일*/
	private LocalDateTime mdate;
	
	/* N:1 */
	@ManyToOne
	private Cboard cboard;
	
	/*작성자*/
	@ManyToOne
	private Member username; // 작성자
	
	/*추천*/
	@ManyToMany
    Set<Member> voter;

}
