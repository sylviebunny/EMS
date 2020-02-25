package com.enfec.sb.eventapi.model;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;

/**
 * Comparator to compare events based on their distances to a certain point
 * @author heidihuo
 *
 */
public class EventComparatorByDistance implements Comparator<Map> {

    /**
     * Compares two events based on their distances to a certain point. Returns -1 if e1 is closer, 1 if e2 is closer, 0 if they are the same close
     * @param e1: event 1
     * @param e2: event 2
     * @return comparing result
     * @throws SQLException
     */
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

		double e1_distance_between = (double) e1.get("distance_between");
		double e2_distance_between = (double) e2.get("distance_between");

		if (e1_distance_between == e2_distance_between) {
			return 0;
		}
		return e1_distance_between < e2_distance_between ? -1 : 1;
	}
}
