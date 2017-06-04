package org.tinyvfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Alain on 04/06/2017.
 */
public class ObjectTools {

	public final static Logger LOGGER = LoggerFactory.getLogger(ObjectTools.class);

	public static String getConfig1() {
		return "tvfs.dir1.name=nom1\n" +
				"tvfs.dir1.directory=${TEMP}\n" +
				"tvfs.dir1.readonly=false";
	}

	public static Path createConfigFile() throws IOException {
		Path p = Files.createTempDirectory("testTVFSConfigureTest");
		LOGGER.info("p={}", p);
		String contenu = ObjectTools.getConfig1();
		Path p2 = p.resolve("conf.properties");
		LOGGER.info("p2={}", p2);
		Files.write(p2, contenu.getBytes(StandardCharsets.UTF_8));
		return p2;
	}
}
