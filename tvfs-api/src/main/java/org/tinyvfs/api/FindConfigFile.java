package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Alain on 28/05/2017.
 */
public class FindConfigFile {

	public final static Logger LOGGER = LoggerFactory.getLogger(FindConfigFile.class);

	public static String TVFS_PROPERTIES = "TVFS_CONFIG_FILE";

	public static String CONFIG_FILE_NAME = "config.properties";

	public Path findFile() {
		String tvfsProperties = System.getProperty(TVFS_PROPERTIES);
		if (tvfsProperties != null && tvfsProperties.trim().length() > 0) {
			LOGGER.info("config file = {}", tvfsProperties);
			Path p = Paths.get(tvfsProperties);
			return p;
		} else {
			URL url = FindConfigFile.class.getResource(CONFIG_FILE_NAME);
			LOGGER.info("config file = {}", url);
			if (url != null) {
				Path p = null;
				try {
					p = Paths.get(url.toURI());
				} catch (URISyntaxException e) {
					LOGGER.error("Error : {}", e.getMessage(), e);
				}
				return p;
			} else {
				return null;
			}
		}

	}
}
