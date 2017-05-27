package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.path.TVFSRootName;
import org.tinyvfs.core.spi.TVFSConfigurator;

import java.io.IOException;
import java.nio.file.Files;
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

		Path folder = null;
		try {
			folder = Files.createTempDirectory("temp");

			TVFSRootName name = new TVFSRootName("nom1");
			TVFSConfigParam param = new TVFSConfigParam(name, folder, false);
			tvfsConfig.add(name, param);

		} catch (IOException e) {
			LOGGER.error("Error: {}", e.getMessage(), e);
		}

		LOGGER.debug("end configure");
	}
}
