package com.musicflow.deezer.connection;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionUrlBuilderTest {

	@Test
	public void test_url_ok() throws DeezerConnectionException {
		final List<Permission> permissions = Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY,
				Permission.LISTENING_HISTORY);
		final String connectionUrl = new ConnectionUrlBuilder().withPermissions(permissions).build();
		assertEquals(
				"https://connect.deezer.com/oauth/auth.php?app_id=212422&redirect_uri=https%3A%2F%2Fmymusicflow.me%2Fdeezer%2Fconnected&perms=basic_access,manage_library,listening_history",
				connectionUrl);
	}

}
