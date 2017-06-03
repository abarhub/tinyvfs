package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.api.ast.DirectoryConfig;
import org.tinyvfs.api.ast.GlobalConfig;
import org.tinyvfs.api.exception.TVFSParseException;
import org.tinyvfs.core.TVFSTools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Alain on 28/05/2017.
 */
public class ParseConfigFile {

	public final static Logger LOGGER = LoggerFactory.getLogger(ParseConfigFile.class);

	public static final String BEGIN_PROPERTIE = "tvfs";
	public static final String PROPERTIES_DIR = "dir";
	public static final String PROPERTIES_NAME = "name";
	public static final String PROPERTIES_DIRECTORY = "directory";
	public static final String PROPERTIES_READONLY = "readonly";

	public GlobalConfig parse(Path p) throws IOException {
		TVFSTools.checkParamNotNull(p, "Path must not be null");

		LOGGER.debug("Start read config file : {}", p);

		if (!Files.exists(p)) {
			LOGGER.error("Path '{}' must exists", p);
			throw new FileNotFoundException("Path '" + p + "' must exists");
		}

		Properties properties = new Properties();
		properties.load(new FileReader(p.toFile()));

		Map<String, DirectoryConfig> mapDir = new LinkedHashMap<>();

		Map<String, String> map = convertToMap(properties);

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			name = name.trim();
			if (value != null) {
				value = value.trim();
			}
			if (name.startsWith(BEGIN_PROPERTIE + "." + PROPERTIES_DIR)) {
				int pos = name.indexOf('.', BEGIN_PROPERTIE.length() + 2);
				if (pos > 0 && pos + 1 < name.length()) {
					String dirName = name.substring(BEGIN_PROPERTIE.length() + 2, pos);

					String suite = name.substring(pos + 1);

					DirectoryConfig directoryConfig;

					if (!mapDir.containsKey(dirName)) {
						directoryConfig = new DirectoryConfig();
						mapDir.put(dirName, directoryConfig);
					} else {
						directoryConfig = mapDir.get(dirName);
					}

					if (suite.equals(PROPERTIES_NAME)) {
						directoryConfig.setName(value);
					} else if (suite.equals(PROPERTIES_DIRECTORY)) {
						directoryConfig.setPath(value);
					} else if (suite.equals(PROPERTIES_READONLY)) {
						if ("true".equals(value)) {
							directoryConfig.setReadOnly(true);
						} else if ("false".equals(value)) {
							directoryConfig.setReadOnly(false);
						} else {
							LOGGER.error("key '{}' has bad value '{}'", name, value);
							throw new TVFSParseException("Value invalid for key '" + entry.getKey() + "' for file : " + p);
						}
					} else {
						LOGGER.error("key '{}' invalid : key unknow", name);
						throw new TVFSParseException("Key invalid '" + entry.getKey() + "' for file : " + p);
					}
				} else {
					LOGGER.error("key '{}' invalid : bad format for key name", name);
					throw new TVFSParseException("Key invalid '" + entry.getKey() + "' for file : " + p);
				}
			} else {
				LOGGER.error("key '{}' invalid : must start by '{}'",
						name, BEGIN_PROPERTIE + "." + PROPERTIES_DIR);
				throw new TVFSParseException("Key invalid '" + entry.getKey() + "' for file : " + p);
			}
		}

		List<DirectoryConfig> directoryConfigList = new ArrayList<>();

		for (Map.Entry<String, DirectoryConfig> entry : mapDir.entrySet()) {
			DirectoryConfig dir = entry.getValue();
			if (dir.getName() == null || dir.getName().trim().length() == 0) {
				throw new TVFSParseException("Name is empty for '" + entry.getKey() + "'");
			}
			if (!TVFSTools.isNameValide(dir.getName())) {
				throw new TVFSParseException("Name is invalid '" + dir.getName() + "'");
			}
			if (dir.getPath() == null || dir.getPath().trim().length() == 0) {
				throw new TVFSParseException("Path is empty for '" + entry.getKey() + "'");
			}
			directoryConfigList.add(dir);
		}

		GlobalConfig globalConfig = new GlobalConfig(directoryConfigList);

		LOGGER.debug("End read config file : {}", p);

		return globalConfig;
	}

	private Map<String, String> convertToMap(Properties properties) {
		Map<String, String> map = new TreeMap<>();
		Enumeration<String> pn = (Enumeration<String>) properties.propertyNames();
		while (pn.hasMoreElements()) {
			String key = pn.nextElement();
			String value = properties.getProperty(key);
			map.put(key, value);
		}
		return map;
	}

}
