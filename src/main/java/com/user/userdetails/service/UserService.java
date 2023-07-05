package com.user.userdetails.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.user.userdetails.bean.ResponseBean;
import com.user.userdetails.bean.User;
import com.user.userdetails.dao.UserDao;
import com.user.userdetails.mapper.UserMapper;
import com.user.userdetails.util.JwtAuth;

@Service
public class UserService implements UserDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JwtAuth jwtAuth;
	
	//For user signup
	@Override
	public ResponseBean userSignUp(User user) {
		ResponseBean response = new ResponseBean();
		
		try {
			String queryForCheckingUser = "select case when exists(select username from users where username = '"+user.getUserName()+"') "
					+ "then 'true' else 'false' end";
			Boolean ifExists = jdbcTemplate.queryForObject(queryForCheckingUser, Boolean.class);

			if(ifExists) {
				response.setIsValid(false);
				response.setMessage("User already exists..");
			}else {
				String queryForInsert = "insert into users (username, password, userstatus) values (?,?,?)";
				int result = jdbcTemplate.update(queryForInsert, user.getUserName(), user.getPassword(), "active");

				if(result > 0) {
					response.setIsValid(true);
					response.setMessage("Sign-up Successfully...");
				}else {
					response.setIsValid(false);
					response.setMessage("Failure..");
				}
			}
		}catch (Exception e) {
			response.setIsValid(false);
			response.setMessage("Something went wrong..");
			LOGGER.error("Error while Signup" + e.getMessage());
		}
		return response;
	}
	
	//For user Login
	@Override
	public ResponseBean userLogin(User user) {
		ResponseBean response = new ResponseBean();
		
		String queryForLogin = "select username, password from users where username = ? and password = ? and userstatus = 'active'";
		
		RowMapper<User> mapper = new UserMapper();
		
		try {
			User userRes = jdbcTemplate.queryForObject(queryForLogin, mapper, user.getUserName(), user.getPassword());
			
			if(userRes != null) {
				//Generating user token
				response.setToken(jwtAuth.generateToken(userRes));
				response.setUserDetails(user);
				response.setIsValid(true);
				response.setMessage("Login Successfully..");
			}else {
				response.setIsValid(false);
				response.setMessage("Invalid username or password.");
			}
		}catch (EmptyResultDataAccessException e) {
			LOGGER.error("login exception"+e.getMessage());
			response.setIsValid(false);
			response.setMessage("Invalid username or password.");
		}
		return response;
	}
}
