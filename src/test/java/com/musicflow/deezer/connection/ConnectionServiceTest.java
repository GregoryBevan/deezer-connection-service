package com.musicflow.deezer.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.BadCredentialsException;
import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionServiceTest {

	@Test
	public void test_login_ok() throws DeezerConnectionException, BadCredentialsException {
		final Credentials credentials = new Credentials("gregbest@free.fr", "9Kc1qmOv806E");
		String code = new AuthorizationService().connect(credentials,
				Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY, Permission.LISTENING_HISTORY));
		assertNotNull(code);
		assertEquals(32, code.length());
	}

	@Test(expected=BadCredentialsException.class)
	public void test_login_ko() throws DeezerConnectionException, BadCredentialsException {
		final Credentials credentials = new Credentials("bad@free.fre", "dfvvfec");
		new AuthorizationService().connect(credentials,
				Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY, Permission.LISTENING_HISTORY));
	}

}
