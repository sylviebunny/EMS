package com.enfec.sb.eventapi.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Event row mapper for event table
 * @author heidi huo
 *
 */
public class EventRowmapper implements RowMapper<EventTable> {

    /**
     * {@inheritDoc}}
     */
	@Override
	public EventTable mapRow(ResultSet rs, int rowNum) throws SQLException {

		EventTable et = new EventTable();

		et.setEvent_id(hasColumn(rs, "Event_ID") ? rs.getInt("Event_ID") : Integer.MIN_VALUE);

		et.setEvent_status_code(
				hasColumn(rs, "Event_Status_Code") ? rs.getInt("Event_Status_Code") : Integer.MIN_VALUE);

		et.setEvent_type_code(hasColumn(rs, "Event_Type_Code") ? rs.getInt("Event_Type_Code") : Integer.MIN_VALUE);

		et.setCommercial_type(hasColumn(rs, "Commercial_Type") ? rs.getString("Commercial_Type") : null);

		et.setOrganizer_id(hasColumn(rs, "Organizer_ID") ? rs.getInt("Organizer_ID") : Integer.MIN_VALUE);

		et.setVenue_id(hasColumn(rs, "Venue_ID") ? rs.getInt("Venue_ID") : Integer.MIN_VALUE);

		et.setEvent_name(hasColumn(rs, "Event_Name") ? rs.getString("Event_Name") : null);

		et.setEvent_start_time(hasColumn(rs, "Event_Start_Time") ? rs.getString("Event_Start_Time") : null);

		et.setEvent_end_time(hasColumn(rs, "Event_End_Time") ? rs.getString("Event_End_Time") : null);

		et.setTimezone(hasColumn(rs, "Timezone") ? rs.getString("Timezone") : "UTC");

		et.setNumber_of_participants(
				hasColumn(rs, "Number_of_Participants") ? rs.getInt("Number_of_Participants") : null);

		et.setDerived_days_duration(
				hasColumn(rs, "Derived_Days_Duration") ? rs.getString("Derived_Days_Duration") : null);

		et.setEvent_cost(hasColumn(rs, "Event_Cost") ? rs.getDouble("Event_Cost") : null);

		et.setDiscount(hasColumn(rs, "Discount") ? rs.getDouble("Discount") : null);

		et.setComments(hasColumn(rs, "Comments") ? rs.getString("Comments") : null);

		// Venue and Address Information
		et.setVenue_name(hasColumn(rs, "Venue_Name") ? rs.getString("Venue_Name") : null);

		et.setOther_details(hasColumn(rs, "Other_Details") ? rs.getString("Other_Details") : null);

		et.setStreet1(hasColumn(rs, "Street1") ? rs.getString("Street1") : null);

		et.setStreet2(hasColumn(rs, "Street2") ? rs.getString("Street2") : null);

		et.setCity(hasColumn(rs, "City") ? rs.getString("City") : null);

		et.setState(hasColumn(rs, "State") ? rs.getString("State") : null);

		et.setZipcode(hasColumn(rs, "Zipcode") ? rs.getInt("Zipcode") : null);

		et.setLatitude(hasColumn(rs, "Latitude") ? rs.getDouble("Latitude") : null);

		et.setLongitude(hasColumn(rs, "Longitude") ? rs.getDouble("Longitude") : null);

		et.setEvent_type_description(
				hasColumn(rs, "Event_Type_Description") ? rs.getString("Event_Type_Description") : null);

		et.setEvent_status_description(
				hasColumn(rs, "Event_Status_Description") ? rs.getString("Event_Status_Description") : null);

		et.setOrganizer_name(hasColumn(rs, "Organizer_Name") ? rs.getString("Organizer_Name") : null);

		return et;
	}

	/**
	 * Returns true if the given result set has the given column
	 * @param rs result set
	 * @param columnName column name
	 * @return indication of whether the given result set has the given column
	 * @throws SQLException
	 */
	private static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		for (int x = 1; x <= columns; x++) {
			if (columnName.equals(rsmd.getColumnName(x))) {
				return true;
			}
		}
		return false;
	}

}
