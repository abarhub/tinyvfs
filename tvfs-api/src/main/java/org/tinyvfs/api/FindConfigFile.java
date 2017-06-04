package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSTools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alain on 28/05/2017.
 */
public class FindConfigFile {

	public final static Logger LOGGER = LoggerFactory.getLogger(FindConfigFile.class);

	public static final String TVFS_PROPERTIES = "TVFS_CONFIG_FILE";

	public static final String TVFS_DIRECTORY = "conf";

	public static final String TVFS_CONFIGFILE_TEST = "tvfsconfig_test.properties";

	public static final String TVFS_CONFIGFILE = "tvfsconfig.properties";

	private List<String> configFileNames = Arrays.asList(TVFS_CONFIGFILE_TEST, TVFS_CONFIGFILE);

	public Path findFile() throws IOException {
		Path res = null;
		String tvfsProperties = getFileFromProperties();
		String tvfsEnv = getFileFromEnv();
		if (tvfsProperties != null) {
			LOGGER.info("config file from propertie '{}' = {}", TVFS_PROPERTIES, tvfsProperties);
			res = Paths.get(tvfsProperties);
		} else if (tvfsEnv != null) {
			LOGGER.info("config file from env '{}' = {}", TVFS_PROPERTIES, tvfsEnv);
			res = Paths.get(tvfsEnv);
		} else {
			String currentdir = System.getProperty("user.dir", "");
			Path currentPath;
			if (currentdir != null && !currentdir.isEmpty()) {
				currentPath = Paths.get(currentdir);

				for (String name : configFileNames) {
					Path p = currentPath.resolve(TVFS_DIRECTORY + "/" + name);
					if (Files.exists(p)) {
						LOGGER.info("config file from path = {}", p);
						res = p;
						break;
					} else {
						LOGGER.info("config file from path not exists = {}", p);
					}
				}
			} else {
				LOGGER.info("no current directory");
			}

			if (res == null) {
				for (String name : configFileNames) {
					LOGGER.trace("find config file '{}' from resource", name);
					URL url = FindConfigFile.class.getClassLoader().getResource(name);
					LOGGER.trace("config file from resource {}", url);
					if (url != null) {
						try {
							Path p = Paths.get(url.toURI());
							res = p;
						} catch (URISyntaxException e) {
							LOGGER.error("Error : {}", e.getMessage(), e);
						}
					}
					if (res != null) {
						break;
					}
				}
			}

		}
		if (res != null) {
			LOGGER.trace("config file = {}", res);
			if (!Files.exists(res)) {
				LOGGER.error("File {} not exists", res);
				throw new FileNotFoundException("File " + res + " not exists");
			} else if (Files.isDirectory(res)) {
				LOGGER.error("File {} is directory", res);
				throw new IOException("File " + res + " is directory");
			}
		} else {
			LOGGER.error("no config file found");
		}
		return res;
	}

	protected String getFileFromProperties() {
		String tvfsProperties = System.getProperty(TVFS_PROPERTIES);
		if (tvfsProperties != null && tvfsProperties.trim().length() > 0) {
			return tvfsProperties;
		} else {
			return null;
		}
	}

	protected String getFileFromEnv() {
		String tvfsProperties = System.getenv(TVFS_PROPERTIES);
		if (tvfsProperties != null && tvfsProperties.trim().length() > 0) {
			return tvfsProperties;
		} else {
			return null;
		}
	}

	public List<String> getConfigFileNames() {
		return configFileNames;
	}

	public void setConfigFileNames(List<String> configFileNames) {
		TVFSTools.checkParamNotNull(configFileNames, "param configFileNames must not be null");
		TVFSTools.checkParam(!configFileNames.isEmpty(), "param configFileNames must not be empty");
		this.configFileNames = configFileNames;
	}
}
