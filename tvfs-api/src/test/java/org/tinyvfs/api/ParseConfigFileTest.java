package org.tinyvfs.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.api.ast.GlobalConfig;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Alain on 28/05/2017.
 */
public class ParseConfigFileTest {

	public final static Logger LOGGER = LoggerFactory.getLogger(ParseConfigFileTest.class);

	private ParseConfigFile parseConfigFile = new ParseConfigFile();

	@Test
	public void testParseKO() throws Exception {
		LOGGER.info("testParseKO");

		Path p = getPath("config1.properties");

		// methode testé
		GlobalConfig g = parseConfigFile.parse(p);

		// vérifications

	}

	// methodes utilitaires

	private Path getPath(String filename) throws URISyntaxException {
		URL file = ParseConfigFileTest.class.getClassLoader().getResource("examples/" + filename);
		return Paths.get(file.toURI());
	}
}