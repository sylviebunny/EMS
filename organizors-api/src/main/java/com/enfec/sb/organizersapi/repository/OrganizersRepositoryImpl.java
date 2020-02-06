package com.enfec.sb.organizersapi.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.enfec.sb.organizersapi.model.OrganizersRowmapper;
import com.enfec.sb.organizersapi.model.OrganizersTable;

@Component
public class OrganizersRepositoryImpl implements OrganizersRepository {
	private static final Logger logger = LoggerFactory.getLogger(OrganizersRepositoryImpl.class);
	
	final String SELECT_ORGANIZERS = "select Organizer_ID, Organizer_Name, Email_Address, Password, Other_Details from Organizers where Organizer_ID =?";

	final String REGISTER_ORGANIZERS = "INSERT INTO Organizers(Organizer_ID, Organizer_Name, Email_Address, Password, Other_Details) VALUES "
			+ "(:organizer_id,:organizer_name,:email,:password,:details)";
	
	//final String UPDATE_ORGANIZERS = "UPDATE Organizers SET Password =:password, Other_Details=:details where Organizer_ID =:organizer_id AND Organizer_Name =:organizer_name" ;	
	final String UPDATE_ORGANIZERS = "UPDATE Organizers SET Organizer_Name =:organizer_name, Email_Address = :email, Password =:password, Other_Details=:details where Organizer_ID =:organizer_id" ;
	
	final String DELETE_ORGANIZERS = "DELETE FROM Organizers WHERE Organizer_ID  =?";

	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public  List<OrganizersTable> getOrganizer(String organizer_id) {
		
		return jdbcTemplate.query(SELECT_ORGANIZERS,new Object[] { organizer_id }, new OrganizersRowmapper());
	}

	@Override
	public int registerOrganizer(OrganizersTable organizersTable) {
		int affectedRow;
		Map<String, Object> param = organizersMap(organizersTable);
		
		SqlParameterSource paramSource = new MapSqlParameterSource(param);
		logger.info("Register Device Info : {} ",paramSource);
		affectedRow =namedParameterJdbcTemplate.update(REGISTER_ORGANIZERS, paramSource);
		
		return affectedRow;
	
		
	}

	@Override
	public int updateOrganizer(OrganizersTable organizersTable) {
		int affectedRow;
		Map<String, Object> param = organizersMap(organizersTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Updating organizer : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(UPDATE_ORGANIZERS, pramSource);
		
		return affectedRow;

	}
	
	@Override
	public int deleteOrganizer(OrganizersTable organizersTable) {
		int affectedRow;
		Map<String, Object> param = organizersMap(organizersTable);
		
		SqlParameterSource pramSource = new MapSqlParameterSource(param);
		logger.info("Deleting organizer : {} ",pramSource);
		affectedRow =namedParameterJdbcTemplate.update(DELETE_ORGANIZERS, pramSource);
		
		return affectedRow;

	}
	
	private Map<String, Object> organizersMap(OrganizersTable organizersTable) {
		Map<String, Object>param = new HashMap<>();
	
			if (organizersTable.getOrganizer_id() != 0) {
				param.put("organizer_id", organizersTable.getOrganizer_id());
			} else {
				logger.error("Account id missing");
				throw new NullPointerException("Organizer_id cannot be null");
			}
		
		param.put("organizer_name", organizersTable.getOrganizer_name() == null ? null:organizersTable.getOrganizer_name());
		param.put("email", organizersTable.getEmail()==null ? null:organizersTable.getEmail());
		param.put("details", organizersTable.getDetails()==null ? null:organizersTable.getDetails());
		param.put("password", organizersTable.getPassword().isEmpty() ? null : organizersTable.getPassword());
		//param.put("password", organizersTable.getPassword()==null? null : Base64.getEncoder().encode((organizersTable.getPassword().getBytes())));// encode password
		
		return param;
	}

	

}

