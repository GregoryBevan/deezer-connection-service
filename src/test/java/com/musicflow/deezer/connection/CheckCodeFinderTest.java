package com.musicflow.deezer.connection;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class CheckCodeFinderTest {

	@Test
	public void test_get_check_code_ok() throws DeezerConnectionException {
		InputStream inputStream = CheckCodeFinderTest.class.getClassLoader().getResourceAsStream("authorizationPage.html");
		assertEquals("a9af7004224e0b0fa349fb0a3219ec3c", CheckCodeFinder.getCheckCode(inputStream));
	}


	@Test
	public void test_get_check_code_ko() throws DeezerConnectionException {
		InputStream inputStream = CheckCodeFinderTest.class.getClassLoader().getResourceAsStream("wrongAuthorizationPage.html");
		assertEquals("", CheckCodeFinder.getCheckCode(inputStream));
	}

}
