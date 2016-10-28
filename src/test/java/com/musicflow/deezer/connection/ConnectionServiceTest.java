package com.musicflow.deezer.connection;

import java.util.Arrays;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionServiceTest {

	@Test
	public void test_login_ok() throws DeezerConnectionException {
		final Credentials credentials = new Credentials("gregbest@free.fr", "9Kc1qmOv806E");
		new ConnectionService().connect(credentials,
				Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY, Permission.LISTENING_HISTORY));
	}

}
