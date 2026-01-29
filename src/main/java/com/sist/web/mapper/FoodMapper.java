package com.sist.web.mapper;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.*;
// <mybatis-spring basepackage="">
@Mapper
@Repository
public interface FoodMapper {

	@Select("SELECT fno,name,poster "
			+ "FROM menupan_food "
			+ "ORDER BY fno ASC "
			+ "OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY")
	public List<FoodVO> foodListData(int start);
	
	@Select("SELECT CEIL(COUNT(*)/12.0) FROM menupan_food")
	public int foodTotalPage();
	
	@Select("SELECT fno,name,poster "
			+ "FROM (SELECT fno,name,poster "
			+ "FROM menupan_food "
			+ "WHERE address LIKE '%'||#{address}||'%' "
			+ "ORDER BY fno ASC) "
			+ "OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY")
	public List<FoodVO> foodFindData(@Param("start") int start, @Param("address") String address);
	
	@Select("SELECT CEIL(COUNT(*)/12.0) FROM menupan_food "
			+ "WHERE address LIKE '%'||#{address}||'%'")
	public int foodFindTotalPage(String address);
	
	@Select("SELECT fno,name,poster,address,phone,type,price,"
			+ "time,parking,score,theme,content "
			+ "FROM menupan_food "
			+ "WHERE fno=#{fno}")
	public FoodVO foodDetailData(int fno);
}
