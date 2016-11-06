package com.musicflow.deezer.connection.ssl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

/**
 * Trust manager for mymusicflow.me request execution
 *  
 * @author Grégory
 */
public class MymusicflowTrustManager implements X509TrustManager {

	

	private KeyStoreLoader keyStoreLoader;
	private final X509TrustManager defaultTrustManager;
	private final X509TrustManager mymuiscflowTrustManager;

	/**
	 * Constructor that initializes default and mymusicflow trust managers
	 * 
	 * @throws DeezerConnectionException
	 */
	public MymusicflowTrustManager() throws DeezerConnectionException {
		keyStoreLoader = KeyStoreLoader.INSTANCE;
		this.defaultTrustManager = getDefaultTrustManager();
		this.mymuiscflowTrustManager = getMymusicflowTrustManager();
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] x509Certificates, final String authType)
			throws CertificateException {
		try {
			defaultTrustManager.checkClientTrusted(x509Certificates, authType);
		} catch (CertificateException ignored) {
			this.mymuiscflowTrustManager.checkClientTrusted(x509Certificates, authType);
		}
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] x509Certificates, final String authType)
			throws CertificateException {
		try {
			defaultTrustManager.checkServerTrusted(x509Certificates, authType);
		} catch (CertificateException ignored) {
			this.mymuiscflowTrustManager.checkServerTrusted(x509Certificates, authType);
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return this.mymuiscflowTrustManager.getAcceptedIssuers();
	}

	private X509TrustManager getMymusicflowTrustManager() throws DeezerConnectionException {
		try {
			final TrustManagerFactory customTrustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			customTrustManagerFactory.init(keyStoreLoader.getMymusicflowKeyStore());
			return (X509TrustManager)customTrustManagerFactory.getTrustManagers()[0];
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			throw new DeezerConnectionException("An error while getting letsencrypt trust managers", e);
		}
	}

	private X509TrustManager getDefaultTrustManager() throws DeezerConnectionException {
		try {
			final TrustManagerFactory defqultTrustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			defqultTrustManagerFactory.init(keyStoreLoader.getEmptyKeySXtore());
			return (X509TrustManager)defqultTrustManagerFactory.getTrustManagers()[0];
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			throw new DeezerConnectionException("An error while getting default trust managers", e);
		}
	}
}
