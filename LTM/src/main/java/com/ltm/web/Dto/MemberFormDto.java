package com.ltm.web.Dto;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.ltm.web.constant.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
	@Size(min=4, max=12, message = "아이디는 4~12자 이내로 작성해 주세요.")
	@NotEmpty(message = "사용자 id는 필수 항목입니다.")
	private String username;
	
	@Size(min=8, max=30, message = "비밀번호는 8~30자 이내로 작성해 주세요.")
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String password1;
	
	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2;

	@AssertTrue(message = "비밀번호가 서로 같지 않습니다.")
	private boolean isPasswordValidation() {
		if(password1.equals(password2)) {
			System.out.println("참");
			passvali ="비밀번호가 서로 같지 않습니다.";
			return true;
		}
		System.out.println("거짓");
		return false;
	}
	
	@NotEmpty(message="비밀번호가 서로 같지 않습니다.")
	private String passvali;
	
//	@NotEmpty(message = "닉네임은 필수 항목입니다.")
//	private String nickname;
	
	@Email
	@NotEmpty(message = "이메일은 필수 항목입니다.")
	private String email;
	
	private String phone;
	
	
	private String birth; 
	
	private LocalDateTime joindate;
	
	@Enumerated(EnumType.STRING)
	private MemberRole role;
}
