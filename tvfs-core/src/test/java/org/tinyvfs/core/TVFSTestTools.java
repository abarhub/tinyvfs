package org.tinyvfs.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.config.TVFSRepository;

import java.lang.reflect.Field;
import java.nio.file.spi.FileSystemProvider;

/**
 * Created by Alain on 04/06/2017.
 */
public class TVFSTestTools {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSTestTools.class);

	public static void reinitConfig() throws NoSuchFieldException, IllegalAccessException {
		LOGGER.info("reinitConfig ...");
		TVFSRepository.clearInstance();
		reinitProviders();
		TVFSPaths.clear();
		LOGGER.info("reinitConfig OK");
	}

	private static void reinitProviders() throws NoSuchFieldException, IllegalAccessException {
		LOGGER.info("reinitProviders ...");
		Field f = FileSystemProvider.class.getDeclaredField("installedProviders");
		f.setAccessible(true);
		f.set(null, null);

		Field f2 = FileSystemProvider.class.getDeclaredField("loadingProviders");
		f2.setAccessible(true);
		f2.setBoolean(null, false);
		LOGGER.info("reinitProviders OK");
	}
}
