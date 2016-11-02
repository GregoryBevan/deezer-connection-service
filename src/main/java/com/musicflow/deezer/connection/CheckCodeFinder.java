package com.musicflow.deezer.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.musicflow.deezer.connection.exception.DeezerConnectionException;

public class CheckCodeFinder {
	private static final String CHECK_FORM_VARIABLE = "var checkForm = '";
	
	public static String getCheckCode(InputStream inputStream) throws DeezerConnectionException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().filter(l -> l.contains(CHECK_FORM_VARIABLE)).map(l -> l.replace(CHECK_FORM_VARIABLE, "").replaceAll("';", "")).collect(Collectors.joining("\n"));
        } catch (IOException e) {
			throw new DeezerConnectionException("Can't find check code in authorization page", e);
		}
    }

}
