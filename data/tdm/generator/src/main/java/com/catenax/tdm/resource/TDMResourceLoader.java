/*
 *
 */
package com.catenax.tdm.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class TDMResourceLoader.
 */
public class TDMResourceLoader {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(TDMResourceLoader.class);

	/**
	 * Load resource.
	 *
	 * @param path the path
	 * @return the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream loadResource(String path) throws IOException {
		try {
			final ResourceLoader loader = new DefaultResourceLoader();
			final Resource resource = loader.getResource("classpath:" + path);

			// new ClassPathResource(filename).getInputStream();

			// File file = resource.getFile(); //.getFile("classpath:" + path);
			final InputStream in = resource.getInputStream(); // FileInputStream(file);
			return in;
		} catch (final IOException e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

	/**
	 * Resource to string.
	 *
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String resourceToString(String path) throws IOException {
		String result = "";

		final InputStream in = loadResource(path);

		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		final StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}

		result = out.toString();

		return result;
	}

}
