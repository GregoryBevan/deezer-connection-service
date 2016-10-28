package com.musicflow.deezer.connection;

public class Credentials {

	private final String username;

	private final String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	};

}
