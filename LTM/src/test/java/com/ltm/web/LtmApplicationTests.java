package com.ltm.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ltm.web.Service.CboardService;

@SpringBootTest
class LtmApplicationTests {
	
	@Autowired
	private CboardService cboardService;
	
	 @Test
     void 게시글_생성() {
         for (int i = 1; i <= 100; i++) {
             String ctitle = String.format("테스트 데이터입니다:[%03d]", i);
             String cbody = "내용무";
             String tags = "아무노래";
             this.cboardService.create(ctitle, cbody, null, tags);
         }
     }
}
