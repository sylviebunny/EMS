package com.enfec.demo.repository;

import com.enfec.demo.model.SeatCategory;

public interface SeatCategoryRepository {
	
	public int createSeatCategory(SeatCategory sc);
	public SeatCategory getSeatCategoryInfo(int Category_ID);
	public int updateSeatCategory(SeatCategory sc);	
	public int deleteSeatCategory(int Category_ID);
}
