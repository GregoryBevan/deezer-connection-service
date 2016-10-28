package com.musicflow.deezer.connection;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class ConnectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);

	private final ConnectionUrlBuilder connectionUrlBuilder;

	private final CookieStore cookieStore;

	private final CloseableHttpClient httpClient;

	public ConnectionService() {
		connectionUrlBuilder = new ConnectionUrlBuilder();
		cookieStore = new BasicCookieStore();
		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore)
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
	}

	public void connect(Credentials credentials, List<Permission> permissions) throws DeezerConnectionException {
		try {

			login(credentials);
			acceptMyMusicFlowApplication(permissions);
		} catch (final IOException e) {
			throw new DeezerConnectionException("An error occurred while accessing to login page", e);
		}
		// } finally {
		// e.printStackTrace();
		// }
	}

	private void login(Credentials credentials) throws IOException, ClientProtocolException {
		final HttpUriRequest login = RequestBuilder.post()
				.setUri("https://connect.deezer.com/login.php?app_id=212422&redirect_type=refresh&redirect_link=https%3A%2F%2Fconnect.deezer.com%2Foauth%2Fauth.php%3Fperms%3Dbasic_access%252Cmanage_library%252Clistening_history%26format%3Dwindow%26app_id%3D212422%26redirect_uri%3Dhttps%253A%252F%252Fmymusicflow.me%252Fdeezer%252Fconnected")
				.addParameter("login_mail", credentials.getUsername())
				.addParameter("login_password", credentials.getPassword()).build();

		final CloseableHttpResponse loginResponse = httpClient.execute(login);
		final HttpEntity loginEntity = loginResponse.getEntity();
		LOGGER.info("Login form get: " + loginResponse.getStatusLine());
		EntityUtils.consume(loginEntity);

		LOGGER.info("Post logon cookies:");
		final List<Cookie> cookies = cookieStore.getCookies();
		if (cookies.isEmpty()) {
			LOGGER.info("None");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				LOGGER.info("- " + cookies.get(i).toString());
			}
		}
		loginResponse.close();
	}

	private void acceptMyMusicFlowApplication(List<Permission> permissions)
			throws DeezerConnectionException, IOException, ClientProtocolException {
		final HttpPost httpPost = new HttpPost(connectionUrlBuilder.withPermissions(permissions).build());
		final HttpResponse oAuthResponse = httpClient.execute(httpPost);
		LOGGER.info("Response to login form page access : " + oAuthResponse.getStatusLine());

		final HttpEntity entity = oAuthResponse.getEntity();
		EntityUtils.consume(entity);

		LOGGER.info("Initial set of cookies:");
		final List<Cookie> cookies = cookieStore.getCookies();
		if (cookies.isEmpty()) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				LOGGER.info("- " + cookies.get(i).toString());
			}
		}
	}

}
