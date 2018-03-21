package com.cdc.mitrais.cookie;

import javax.servlet.http.HttpSession;

public class SessionHandling {
	private HttpSession session;

	public SessionHandling() {
		
	}
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
}
