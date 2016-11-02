package com.musicflow.deezer.connection;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicflow.deezer.connection.exception.BadCredentialsException;
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

	public void connect(Credentials credentials, List<Permission> permissions)
			throws DeezerConnectionException, BadCredentialsException {
			login(credentials);
			authorizeMyMusicFlowApplication(permissions);
	}

	private void login(Credentials credentials) throws BadCredentialsException, DeezerConnectionException {
		HttpUriRequest loginPage = RequestBuilder.post().setUri(
				"https://connect.deezer.com/login.php?app_id=212422&redirect_type=refresh&redirect_link=https%3A%2F%2Fconnect.deezer.com%2Foauth%2Fauth.php%3Fperms%3Dbasic_access%252Cmanage_library%252Clistening_history%26format%3Dwindow%26app_id%3D212422%26redirect_uri%3Dhttps%253A%252F%252Fmymusicflow.me%252Fdeezer%252Fconnected").build();
		
		try {
			CloseableHttpResponse response = httpClient.execute(loginPage);
			final List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					LOGGER.info("- " + cookies.get(i).toString());
				}
			}
		} catch(IOException e) {
			
		}
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
			tryClosingLoginResponse(loginResponse);
		}

	}

	private void tryClosingLoginResponse(CloseableHttpResponse loginResponse) {
		if (loginResponse != null) {
			try {
				loginResponse.close();
			} catch (IOException e) {
				LOGGER.error("Can't close login response", e);
			}
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

	private String getCheckCode(List<Permission> permissions)
			throws DeezerConnectionException {
		final HttpPost httpPost = new HttpPost(connectionUrlBuilder.withPermissions(permissions).build());
		try {
			BasicHttpContext context = new BasicHttpContext();
			httpClient.execute(httpPost, new BasicResponseHandler(), context);
//			LOGGER.info("Response to login form page access : " + oAuthResponse.getStatusLine());
//
//			final HttpEntity entity = oAuthResponse.getEntity();
//			String code = CheckCodeFinder.getCheckCode(entity.getContent());
//			final List<Cookie> cookies = cookieStore.getCookies();
//			if (cookies.isEmpty()) {
//				System.out.println("None");
//			} else {
//				for (int i = 0; i < cookies.size(); i++) {
//					LOGGER.info("- " + cookies.get(i).toString());
//				}
//			}
//			oAuthResponse.close();
			return "";
		} catch (UnsupportedOperationException | IOException e) {
			throw new DeezerConnectionException("An error occurred while executing request to find check code", e);
		}


	}
	
	private void authorizeMyMusicFlowApplication(List<Permission> permissions) throws DeezerConnectionException {
//		String checkCode = getCheckCode(permissions);
		final HttpUriRequest httpPost = RequestBuilder.post().setUri(connectionUrlBuilder.withPermissions(permissions).build())
				//.addParameter("checkCode",checkCode)
				.addParameter("allow","")
				.build();
		try {
			final CloseableHttpResponse oAuthResponse = httpClient.execute(httpPost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
