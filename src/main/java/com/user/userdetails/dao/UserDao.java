package com.user.userdetails.dao;

import com.user.userdetails.bean.ResponseBean;
import com.user.userdetails.bean.User;

public interface UserDao {

	public ResponseBean userSignUp(User user);
	public ResponseBean userLogin(User user);
}
