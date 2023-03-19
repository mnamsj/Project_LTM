
$(document).ready(function(){
	        
		    // 이중탭
		    $(".tabonoff > .cont-wrap").children().css("display", "none"); 
		    $(".tabonoff > .cont-wrap div:first-child").css("display", "block");
		    $(".tabonoff > .jq_tab li:first-child").addClass("on"); 

		    $(".tabonoff").find(".jq_tab > li").on("click", function() { 
		        var index = $(this).parent().children().index(this);
				
		        $(this).siblings().removeClass();
		        $(this).addClass("on");
		       // console.log($(this).parent().children());
		        $(this).parent().next(".cont-wrap").children().hide().eq(index).show();
		    });

		     // 설치/미설치 탭메뉴 이미지교체 
		     $("#tabMenu > li").click(function(){
		        var $this = $(this);
		        $("#tabMenu > li").each(function(idx){
		            var $li = $(this);
		            $li.find("img").attr("src", $(this).find("img").attr("src").replace("_on","_off"));
		        });
		        $this.find("img").attr("src", $this.find("img").attr("src").replace("_off","_on"));
		    });   

		});