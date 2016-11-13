package com.musicflow.deezer.connection.ssl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.ssl.SSLContexts;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

/**
 * Provider of mymusicflow.me SSL context (Singleton)
 * 
 * @author Gr�gory
 */
public enum SSLContextProvider {
	INSTANCE;

	/**
	 * Create a SSL context based on default and mymusicflow trust managers
	 * 
	 * @return SSL context
	 * @throws DeezerConnectionException
	 */
	public SSLContext createSSLContext() throws DeezerConnectionException {
		try {
			SSLContext sslContext = SSLContexts.createDefault();
			sslContext.init(null,getTrustManagers(), SecureRandom.getInstance("NativePRNGNonBlocking"));
			return sslContext;
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			throw new DeezerConnectionException("An error occurred while build SSL context", e);
		}
	}

	private TrustManager[] getTrustManagers()
			throws NoSuchAlgorithmException, KeyStoreException, DeezerConnectionException {
		return new TrustManager[]{new MymusicflowTrustManager()};
	}

}
