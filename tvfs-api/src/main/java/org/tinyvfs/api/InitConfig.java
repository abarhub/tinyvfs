package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.api.ast.DirectoryConfig;
import org.tinyvfs.api.ast.GlobalConfig;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Alain on 03/06/2017.
 */
public class InitConfig {

	public final static Logger LOGGER = LoggerFactory.getLogger(InitConfig.class);

	public void init(TVFSConfig tvfsConfig, GlobalConfig conf) {
		TVFSTools.checkParamNotNull(tvfsConfig, "tvfsConfig must not be null");
		TVFSTools.checkParamNotNull(conf, "conf must not be null");

		LOGGER.trace("start init");

		if (conf.getDirectoryConfigs() != null && !conf.getDirectoryConfigs().isEmpty()) {
			try {
				for (DirectoryConfig directoryConfig : conf.getDirectoryConfigs()) {
					LOGGER.trace("directoryConfig={}", directoryConfig);
					String name = directoryConfig.getName();
					String path = directoryConfig.getPath();
					boolean isReadOnly = directoryConfig.isReadOnly();

					LOGGER.debug("name={},path={},isReadOnly={}", name, path, isReadOnly);

					TVFSRootName name2 = getName(tvfsConfig, name);
					LOGGER.trace("name2={}", name2);
					Path folder = getPath(path);
					LOGGER.trace("folder={}", folder);
					TVFSConfigParam param = new TVFSConfigParam(name2, folder, isReadOnly);
					LOGGER.trace("add param={}", param);
					tvfsConfig.add(name2, param);
				}

			} catch (IOException e) {
				LOGGER.error("Error: {}", e.getMessage(), e);
			}
		}
		LOGGER.trace("end init");
	}

	private TVFSRootName getName(TVFSConfig tvfsConfig, String name) {
		if (tvfsConfig == null) {
			LOGGER.error("tvfsConfig must not be null");
			throw new IllegalArgumentException("tvfsConfig must not be null");
		} else if (name == null || name.trim().length() == 0) {
			LOGGER.error("Path must not be null or empty");
			throw new IllegalArgumentException("Path must not be null or empty");
		} else if (!TVFSTools.isNameValide(name)) {
			LOGGER.error("Name '" + name + "' is invalide");
			throw new IllegalArgumentException("Name '" + name + "' is invalide");
		} else {
			TVFSRootName tvfsRootName = new TVFSRootName(name);
			if (tvfsConfig.contains(tvfsRootName)) {
				LOGGER.error("Name '" + name + "' already exist");
				throw new IllegalArgumentException("Name '" + name + "' already exist");
			}
			return tvfsRootName;
		}
	}

	private Path getPath(String path) throws IOException {
		if (path == null || path.trim().length() == 0) {
			LOGGER.error("Path must not be null or empty");
			throw new IllegalArgumentException("Path must not be null or empty");
		} else if (path.startsWith("${") && path.endsWith("}")) {
			String var = path.substring(2, path.length() - 1);
			if (var.equals("TEMP")) {
				Path p = Files.createTempDirectory("temptvfs");
				LOGGER.trace("Var TEMP={}", p);
				return p;
			} else {
				LOGGER.error("Var '{}' is not defined", var);
				throw new IllegalArgumentException("Var '" + var + "' not define");
			}
		} else {
			Path p = Paths.get(path);
			if (!p.isAbsolute()) {
				LOGGER.error("Path '{}' is not absolute", p);
				throw new IllegalArgumentException("Path '" + p + "' is note absolute");
			} else {
				LOGGER.trace("p={}", p);
				return p;
			}
		}
	}
}
