package com.ltm.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ltm.web.Service.AdboardService;
import com.ltm.web.Service.CboardService;

@SpringBootTest
class LtmApplicationTests {
	
	@Autowired
	private CboardService cboardService;
	/*private AdboardService adboardService;*/
	
	 @Test
     void 커뮤_니티() {
         for (int i = 1; i <= 100; i++) {
             String ctitle = String.format("테스트 데이터입니다:[%03d]", i);
             String cbody = "내용무";
             String tags = "아무노래";
             this.cboardService.create(ctitle, cbody, null, tags);
         }
     }
	
/*	@Test
	void 공지_사항() {
		for (int i=1; i<= 40; i++) {
			String adtitle = String.format("[공지사항]공지사항입니다:[%03d]", i);
			String adbody = "내용무";
			String tags = "해시태그";
			this.adboardService.create(adtitle, adbody, null, tags);
		}
	}*/
}
