package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class Ping {

	private String msg;
	
	public Ping(String msg) {
		this.msg = msg;
	}
	public Ping() {}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
