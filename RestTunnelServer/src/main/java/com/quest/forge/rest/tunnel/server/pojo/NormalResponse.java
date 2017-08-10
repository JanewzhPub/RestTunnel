package com.quest.forge.rest.tunnel.server.pojo;

public class NormalResponse<T> extends CommonResponse<T> {

	public NormalResponse(T data) {
		super(1, data);
	}

}
