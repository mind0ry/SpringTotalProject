package com.sist.web.service;

import java.util.List;

import com.sist.web.vo.FoodVO;

public interface FoodService {

	public List<FoodVO> foodListData(int start);
	public int foodTotalPage();
	public List<FoodVO> foodFindData(int start, String address);
	public int foodFindTotalPage(String address);
	public FoodVO foodDetailData(int fno);
}
