package com.enfec.sb.eventapi.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortByDistance implements Comparator<Map>{
	
	@Override
	public int compare(Map e1, Map e2) {
		if (!e1.containsKey("distance_between") && !e2.containsKey("distance_between")) {
			return 0; 
		}
		
		if (!e1.containsKey("distance_between")) {
			return e2.get("distance_between") == null ? 0 : 1; 
		} else if (!e2.containsKey("distance_between")) {
			return e1.get("distance_between") == null ? 0 : -1; 
		}
		
		double e1_distance_between = (double)e1.get("distance_between"); 
		double e2_distance_between = (double)e2.get("distance_between"); 
		
		if (e1_distance_between == e2_distance_between) {
			return 0; 
		}
		return e1_distance_between < e2_distance_between ? -1 : 1; 
	}
}
