package com.user.userdetails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.http.util.TextUtils;
import com.user.userdetails.bean.ResponseBean;
import com.user.userdetails.bean.User;
import com.user.userdetails.dao.UserDao;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserDao dao;
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = "application/json")
	public ResponseBean userSignUp(@RequestBody User user) {
		ResponseBean response = new ResponseBean();
		if((TextUtils.isBlank(user.getUserName()) || user.getUserName() == null)) {
			response.setIsValid(false);
			response.setMessage("Please enter correct username");
		}else if((TextUtils.isBlank(user.getPassword()) || user.getPassword() == null)) {
			response.setIsValid(false);
			response.setMessage("Please enter correct password");
		}else {
			response = dao.userSignUp(user);
		}
		return response;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public ResponseBean userLogin(@RequestBody User user) {
		ResponseBean response = new ResponseBean();
		if((TextUtils.isBlank(user.getUserName()) || user.getUserName() == null)) {
			response.setIsValid(false);
			response.setMessage("Please enter correct username");
		}else if((TextUtils.isBlank(user.getPassword()) || user.getPassword() == null)) {
			response.setIsValid(false);
			response.setMessage("Please enter correct password");
		}else {
			response = dao.userLogin(user);
		}
		return response;
	}
}
