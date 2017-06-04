package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.api.ast.GlobalConfig;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.spi.TVFSConfigurator;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Alain on 27/05/2017.
 */
public class TVFSConfigure extends TVFSConfigurator {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfig.class);

	public TVFSConfigure() {
		LOGGER.info("TVFSConfigure");
	}

	@Override
	public void configure(TVFSConfig tvfsConfig) {
		LOGGER.debug("start configure");

		FindConfigFile findConfigFile = new FindConfigFile();
		Path p = findConfigFile.findFile();
		LOGGER.info("p={}", p);
		if (p != null) {
			ParseConfigFile parseConfigFile = new ParseConfigFile();
			try {
				GlobalConfig conf = parseConfigFile.parse(p);
				LOGGER.info("conf={}", conf);

				InitConfig initConfig = new InitConfig();

				LOGGER.info("init conf ...");
				initConfig.init(tvfsConfig, conf);

				LOGGER.info("init conf OK");
			} catch (IOException e) {
				LOGGER.error("Error: {}", e.getMessage(), e);
			}
		}

		LOGGER.debug("end configure");
	}
}
