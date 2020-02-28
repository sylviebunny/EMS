package com.enfec.eventapi.model;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;

/************************************************
*
* Author: Heidi Huo
* Assignment: Comparator to compare events based on their start time
* Class: EventComparatorByStartTime
*
************************************************/
public class EventComparatorByStartTime implements Comparator<Map> {

    /**
     * Compares two events based on start time. Returns -1 if e1 is before e2, 1 if e2 is before e1, 0 if they are at the same time
     * @param e1: event 1
     * @param e2: event 2
     * @return comparing result
     * @throws SQLException
     */
	@Override
	public int compare(Map e1, Map e2) {
		if (!e1.containsKey("event_start_time") && !e2.containsKey("event_start_time")) {
			return 0;
		}

		if (!e1.containsKey("event_start_time")) {
			return e2.get("event_start_time") == null ? 0 : 1;
		} else if (!e2.containsKey("event_start_time")) {
			return e1.get("event_start_time") == null ? 0 : -1;
		}

		Timestamp e1_event_start_time = Timestamp.valueOf(e1.get("event_start_time").toString());
		Timestamp e2_event_start_time = Timestamp.valueOf(e2.get("event_start_time").toString());

		if (e1_event_start_time.equals(e2_event_start_time)) {
			return 0;
		}
		return e1_event_start_time.before(e2_event_start_time) ? -1 : 1;
	}

}
