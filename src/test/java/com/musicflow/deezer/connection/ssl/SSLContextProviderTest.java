package com.musicflow.deezer.connection.ssl;

import static org.junit.Assert.*;

import javax.net.ssl.SSLContext;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class SSLContextProviderTest {

	@Test
	public void test_ok() throws DeezerConnectionException {
		SSLContext sslContext = SSLContextProvider.INSTANCE.createSSLContext();
		assertNotNull(sslContext);
		assertEquals("TLS", sslContext.getProtocol());
	}

}
