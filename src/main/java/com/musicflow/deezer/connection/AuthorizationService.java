package com.musicflow.deezer.connection;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicflow.deezer.connection.exception.BadCredentialsException;
import com.musicflow.deezer.connection.exception.DeezerConnectionException;
import com.musicflow.deezer.connection.ssl.SSLContextProvider;

/**
 * Service that handles user authorization for mymuicflow.me
 * 
 * @author Grégory
 *
 */
public class AuthorizationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);

	private final ConnectionUrlBuilder connectionUrlBuilder;

	private final CloseableHttpClient httpClient;
	
	static {
		System.setProperty("https.protocols", "TLSv1.2");
	}

	/**
	 * Constructors that initializes http client for further requests
	 * 
	 * @throws DeezerConnectionException
	 */
	public AuthorizationService() throws DeezerConnectionException {
		connectionUrlBuilder = new ConnectionUrlBuilder();
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
				SSLContextProvider.INSTANCE.createSSLContext());
		httpClient = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).setSSLSocketFactory(sslSocketFactory)
				.setRedirectStrategy(new LaxRedirectStrategy())
				.build();
	}

	public String connect(Credentials credentials, List<Permission> permissions)
			throws DeezerConnectionException, BadCredentialsException {
		login(credentials);
		String code = authorizeMyMusicFlowApplication(permissions);
		LOGGER.debug("Connection code = " + code);
		return code;
	}

	private void login(Credentials credentials) throws BadCredentialsException, DeezerConnectionException {
		final HttpUriRequest login = RequestBuilder.post().setUri("https://connect.deezer.com/ajax/action.php")
				.addParameter("type", "login").addParameter("mail", credentials.getUsername())
				.addParameter("password", credentials.getPassword()).build();

		CloseableHttpResponse loginResponse = null;
		try {
			loginResponse = httpClient.execute(login);
			checkConnectionSuccess(loginResponse);
		} catch (IOException e) {
			throw new DeezerConnectionException("An error occurred during login request", e);
		} finally {
			tryClosingResponse(loginResponse);
		}

	}

	private String authorizeMyMusicFlowApplication(List<Permission> permissions) throws DeezerConnectionException {
		final HttpUriRequest httpPost = RequestBuilder.post()
				.setUri(connectionUrlBuilder.withPermissions(permissions).build()).addParameter("allow", "").build();
		CloseableHttpResponse oAuthResponse = null;
		try {

			HttpContext context = new BasicHttpContext();
			oAuthResponse = httpClient.execute(httpPost, context);
			return getAuthorizationCode(context);
		} catch (IOException ioe) {
			throw new DeezerConnectionException("An error occurred while executing authorization request", ioe);
		} finally {
			tryClosingResponse(oAuthResponse);
		}

	}

	private void checkConnectionSuccess(final HttpResponse loginResponse) throws IOException, BadCredentialsException {
		final HttpEntity loginEntity = loginResponse.getEntity();
		if (!getLoginResponseContent(loginEntity).equals("success")) {
			throw new BadCredentialsException();
		}
	}

	private String getLoginResponseContent(HttpEntity loginEntity) throws IOException {
		try (Scanner scanner = new Scanner(loginEntity.getContent(), "utf-8")) {
			return scanner.next();
		}
	}

	private String getAuthorizationCode(HttpContext context) throws DeezerConnectionException {
		RedirectLocations locations = (RedirectLocations) context
				.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
		if (locations != null) {
			URI finalUrl = locations.getAll().get(locations.getAll().size() - 1);
			return finalUrl.toString().replace(DeezerApplication.getInstance().getRedirectUrl(), "").split("=")[1];
		} else {
			throw new DeezerConnectionException(
					"Unable to get authorization code after authorization request executioné");
		}
	}

	private void tryClosingResponse(CloseableHttpResponse httpResponse) {
		if (httpResponse != null) {
			try {
				httpResponse.close();
			} catch (IOException ioe) {
				LOGGER.error("Unable to close response", ioe);
			}
		}
	}

}
