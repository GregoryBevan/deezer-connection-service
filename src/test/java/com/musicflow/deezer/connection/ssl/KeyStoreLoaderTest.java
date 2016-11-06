package com.musicflow.deezer.connection.ssl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class KeyStoreLoaderTest {

	@Test
	public void test_ok() throws DeezerConnectionException {
		assertNotNull(KeyStoreLoader.INSTANCE.getMymusicflowKeyStore());
	}

}
