package com.musicflow.deezer.connection;

import java.util.Arrays;

import com.musicflow.deezer.connection.exception.BadCredentialsException;
import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class Main {

	public static void main(String[] args) {
		try {
			final Credentials credentials = new Credentials("gregbest@free.fr", "9Kc1qmOv806E");
			String code = new AuthorizationService().connect(credentials,
					Arrays.asList(Permission.BASIC_ACCESS, Permission.MANAGE_LIBRARY, Permission.LISTENING_HISTORY));
			System.out.println(code);
		} catch (DeezerConnectionException e) {
			e.printStackTrace();
		} catch (BadCredentialsException e) {
			e.printStackTrace();
		}

	}

}
