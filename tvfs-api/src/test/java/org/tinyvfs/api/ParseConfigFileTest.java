package org.tinyvfs.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.api.ast.DirectoryConfig;
import org.tinyvfs.api.ast.GlobalConfig;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by Alain on 28/05/2017.
 */
public class ParseConfigFileTest {

	public final static Logger LOGGER = LoggerFactory.getLogger(ParseConfigFileTest.class);

	private ParseConfigFile parseConfigFile = new ParseConfigFile();

	@Test
	public void testParseOK() throws Exception {
		LOGGER.info("testParseOK");

		Path p = getPath("config1.properties");

		// methode testé
		GlobalConfig g = parseConfigFile.parse(p);

		// vérifications
		assertNotNull(g);
		assertNotNull(g.getDirectoryConfigs());
		assertEquals(1, g.getDirectoryConfigs().size());
		DirectoryConfig dir = g.getDirectoryConfigs().get(0);
		assertEquals("test1", dir.getName());
		assertEquals("c:\\temp", dir.getPath());
		assertEquals(true, dir.isReadOnly());
	}

	@Test
	public void testParse2OK() throws Exception {
		LOGGER.info("testParse2OK");

		Path p = getPath("config2.properties");

		// methode testé
		GlobalConfig g = parseConfigFile.parse(p);

		// vérifications
		assertNotNull(g);
		assertNotNull(g.getDirectoryConfigs());
		assertEquals(2, g.getDirectoryConfigs().size());

		DirectoryConfig dir = g.getDirectoryConfigs().get(0);
		assertEquals("test1", dir.getName());
		assertEquals("c:\\temp1", dir.getPath());
		assertEquals(false, dir.isReadOnly());

		dir = g.getDirectoryConfigs().get(1);
		assertEquals("test2", dir.getName());
		assertEquals("c:\\temp2", dir.getPath());
		assertEquals(true, dir.isReadOnly());
	}

	@Test
	public void testParseFileNotExistsOK() throws Exception {
		LOGGER.info("testParseFileNotExistsOK");

		Path p = getPath("config2.properties")
				.resolve("abc.properties");

		try {
			// methode testé
			GlobalConfig g = parseConfigFile.parse(p);

			fail("Error");

		} catch (FileNotFoundException e) {
			assertEquals("Path '" + p + "' must exists", e.getMessage());
		}
	}

	// methodes utilitaires

	private Path getPath(String filename) throws URISyntaxException {
		URL file = ParseConfigFileTest.class.getClassLoader().getResource("examples/" + filename);
		return Paths.get(file.toURI());
	}
}