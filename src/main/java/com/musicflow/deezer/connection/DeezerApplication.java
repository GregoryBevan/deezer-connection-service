package com.musicflow.deezer.connection;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Contains every
 *
 * @author gbe
 */
public class DeezerApplication {

	private static final DeezerApplication DEEZER_APPLICATION;

	static {
		DEEZER_APPLICATION = parseYaml();
	}

	private String name;
	private String id;
	private String secretKey;
	private String domain;
	private String redirectUrl;
	private String connectUrl;

	private DeezerApplication() {

	}

	public static DeezerApplication getInstance() {
		return DEEZER_APPLICATION;
	}

	private static InputStream getDeezerYamlInputStream() {
		return DeezerApplication.class.getClassLoader().getResourceAsStream("deezer-application.yml");
	}

	private static DeezerApplication parseYaml() {
		try {
			final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
			return objectMapper.readValue(getDeezerYamlInputStream(), DeezerApplication.class);
		} catch (final IOException e) {
			throw new RuntimeException("Can't start application, deezer-application.yml couldn't be parsed", e);
		}
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public String getDomain() {
		return domain;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getSecretKey() {
		return secretKey;
	}

}
