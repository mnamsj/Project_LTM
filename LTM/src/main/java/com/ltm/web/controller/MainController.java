package com.ltm.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Controller
public class MainController {
	
	@GetMapping("/main")
	public String root() {
		
		return "main/main"; 
	}
	@GetMapping("/")
	public String door() {
		
		return "main/door"; 
	}
	@Bean
	public ClassLoaderTemplateResolver secondaryTemplateResolver3() {
	    ClassLoaderTemplateResolver secondaryTemplateResolver3 = new ClassLoaderTemplateResolver();
	    secondaryTemplateResolver3.setPrefix("templates/main");
	    secondaryTemplateResolver3.setSuffix(".html");
	    secondaryTemplateResolver3.setTemplateMode(TemplateMode.HTML);
	    secondaryTemplateResolver3.setCharacterEncoding("UTF-8");
	    secondaryTemplateResolver3.setOrder(1);
	    secondaryTemplateResolver3.setCheckExistence(true);
	        
	    return secondaryTemplateResolver3;
	}
}
