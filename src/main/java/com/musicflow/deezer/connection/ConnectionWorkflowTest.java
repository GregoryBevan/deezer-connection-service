package com.musicflow.deezer.connection;

import java.util.Arrays;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionWorkflowTest {

	@Test
	public void test() throws DeezerConnectionException {
		new ConnectionService().connect(
				Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY, Permission.LISTENING_HISTORY));
	}

}
