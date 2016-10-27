package com.musicflow.deezer.connection;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionService {

	private final ConnectionUrlBuilder connectionUrlBuilder;

	public ConnectionService() {
		connectionUrlBuilder = new ConnectionUrlBuilder();
	}

	public void connect(List<Permission> permissions) throws DeezerConnectionException {
		final HttpClient httpClient = HttpClientBuilder.create().build();
		final HttpGet httpGet = new HttpGet(connectionUrlBuilder.withPermissions(permissions).build());
		try {
			final HttpResponse response = httpClient.execute(httpGet);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
