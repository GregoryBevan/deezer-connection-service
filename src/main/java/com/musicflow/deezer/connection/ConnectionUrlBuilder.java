package com.musicflow.deezer.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionUrlBuilder {

	private static final String PERMISSIONS = "PERMISSIONS";
	private static final String YOUR_REDIRECT_URI = "YOUR_REDIRECT_URI";
	private static final String YOUR_APP_ID = "YOUR_APP_ID";

	private final DeezerApplication deezerApplication;
	private String connectionUrl;

	public ConnectionUrlBuilder() {
		deezerApplication = DeezerApplication.getInstance();
		connectionUrl = deezerApplication.getConnectUrl();
	}

	public ConnectionUrlBuilder withPermissions(final List<Permission> permissions) {
		connectionUrl = connectionUrl.replace(PERMISSIONS, getPermissionsAsString(permissions));
		return this;
	}

	public String build() throws DeezerConnectionException {
		connectionUrl = connectionUrl.replace(YOUR_APP_ID, deezerApplication.getId());
		connectionUrl = connectionUrl.replace(YOUR_REDIRECT_URI, getRedirectUrl());

		return connectionUrl;
	}

	private String getRedirectUrl() throws DeezerConnectionException {
		try {
			final String redirectUrl = deezerApplication.getRedirectUrl();
			return URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8.toString());
		} catch (final UnsupportedEncodingException e) {
			throw new DeezerConnectionException("An error occurred while encoding redirect URL in connection URL", e);
		}
	}

	private String getPermissionsAsString(List<Permission> permissions) {
		return permissions.stream().map(p -> p.name().toLowerCase()).collect(Collectors.joining(","));
	}

}
