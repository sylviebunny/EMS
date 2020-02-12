package com.enfec.sb.eventapi.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class sortByZipcode implements Comparator<Map>{
	
	private final int ZIPCODE; 
	
	public sortByZipcode(int zipcode) {
		ZIPCODE = zipcode; 
	}
	
	@Override
	public int compare(Map e1, Map e2) {
		if (!e1.containsKey("zipcode") || !e2.containsKey("zipcode")) {
			return 0; 
		}
		
		// if e1.zipcode == null || e2.zipcode == null
		if (e1.get("zipcode") == null || (int)e1.get("zipcode") == 0) {
			return e2.get("zipcode") == null ? 0 : 1; 
		} else if (e2.get("zipcode") == null || (int)e2.get("zipcode") == 0) {
			return e1.get("zipcode") == null ? 0 : -1; 
		}
		
		int e1_zipcode = (int)e1.get("zipcode"); 
		int e2_zipcode = (int)e2.get("zipcode"); 
		
		int diff1 = Math.abs(e1_zipcode - ZIPCODE); 
		int diff2 = Math.abs(e2_zipcode - ZIPCODE); 
		if (diff1 == diff2) {
			return 0; 
		}
		return diff1 < diff2 ? -1 : 1; 
	}
}
