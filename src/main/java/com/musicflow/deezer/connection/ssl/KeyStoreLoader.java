package com.musicflow.deezer.connection.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

/**
 * Key store loader. 
 * 
 * @author Grégory
 */
public enum KeyStoreLoader {
	INSTANCE;

	/**
	 * Get the  key store from trustore containing mymusicflow.me certificates
	 * 
	 * @return Key store for mymusicflow.me certificates
	 * @throws DeezerConnectionException
	 */
	public KeyStore getMymusicflowKeyStore() throws DeezerConnectionException {
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			loadKeyStore(ks);
			return ks;
		} catch (KeyStoreException kse) {
			throw new DeezerConnectionException("Can't get key store instance", kse);
		}
	}
	
	/**
	 * Get a null keystore for default trustmanager
	 * 
	 * @return null key store
	 */
	public KeyStore getEmptyKeySXtore() {
		return null;
	}

	private void loadKeyStore(KeyStore ks) throws DeezerConnectionException {
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("cert/mymusicflow-truststore")) {
			ks.load(is, "4fFjr1vHZL6x".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new DeezerConnectionException("Key store can't be loaded", e);
		}
	}

}
