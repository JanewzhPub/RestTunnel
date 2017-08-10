package com.quest.forge.rest.tunnel.server.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CommonResponse<T extends Object> {

	private int status;
	private String code;
	private T data;
	
	public CommonResponse(int status, String code) {
		this.status = status;
		this.code = code;
	}
	
	public CommonResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
