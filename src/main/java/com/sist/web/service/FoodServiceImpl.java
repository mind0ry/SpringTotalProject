package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.FoodMapper;
import com.sist.web.vo.FoodVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

	private final FoodMapper mapper;

	@Override
	public List<FoodVO> foodListData(int start) {
		// TODO Auto-generated method stub
		return mapper.foodListData(start);
	}

	@Override
	public int foodTotalPage() {
		// TODO Auto-generated method stub
		return mapper.foodTotalPage();
	}

	@Override
	public List<FoodVO> foodFindData(int start, String address) {
		// TODO Auto-generated method stub
		return mapper.foodFindData(start, address);
	}

	@Override
	public int foodFindTotalPage(String address) {
		// TODO Auto-generated method stub
		return mapper.foodFindTotalPage(address);
	}

	@Override
	public FoodVO foodDetailData(int fno) {
		// TODO Auto-generated method stub
		return mapper.foodDetailData(fno);
	}
	
	
}
