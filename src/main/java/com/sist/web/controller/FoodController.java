package com.sist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import com.sist.web.vo.*;

import lombok.RequiredArgsConstructor;

import com.sist.web.service.*;

@Controller
@RequiredArgsConstructor
public class FoodController {

	private final FoodService fService;
	
	@GetMapping("/")
	public String food_list(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {
		
		int curpage=Integer.parseInt(page);
		List<FoodVO> list=fService.foodListData((curpage-1)*12);
		int totalpage=fService.foodTotalPage();
		
		final int BLOCK=10;
		int startPage=((curpage-1)/BLOCK*BLOCK)+1;
		int endPage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
		
		if(endPage>totalpage)
			endPage=totalpage;
		
		model.addAttribute("list", list);
		model.addAttribute("curpage", curpage);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "list";
	}
	
	@GetMapping("/detail")
	public String detail(@RequestParam("fno") int fno, Model model) {
		
		FoodVO vo=fService.foodDetailData(fno);
		
		model.addAttribute("vo", vo);
		return "detail";
	}
}
