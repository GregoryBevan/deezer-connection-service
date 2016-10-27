package com.musicflow.deezer.connection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeezerApplicationTest {

	@Test
	public void test() {
		final DeezerApplication deezerApplication = DeezerApplication.getInstance();
		assertEquals("mymusicflow", deezerApplication.getName());
		assertEquals("212422", deezerApplication.getId());
		assertEquals("5fd674526716689e817431e34e7ac214", deezerApplication.getSecretKey());
		assertEquals("mymusicflow.me", deezerApplication.getDomain());
		assertEquals("https://mymusicflow.me/deezer/connected", deezerApplication.getRedirectUrl());
		assertEquals(
				"https://connect.deezer.com/oauth/auth.php?app_id=YOUR_APP_ID&redirect_uri=YOUR_REDIRECT_URI&perms=basic_access,email",
				deezerApplication.getConnectUrl());
	}

}
