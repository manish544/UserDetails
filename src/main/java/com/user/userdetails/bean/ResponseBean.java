package com.user.userdetails.bean;

import lombok.Data;

@Data
public class ResponseBean {

	private Boolean isValid;
	private String message;
	private User userDetails;
	private String token;
}
