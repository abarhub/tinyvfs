package org.tinyvfs.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Alain on 04/06/2017.
 */
public class FindConfigFileTest {


	public final static Logger LOGGER = LoggerFactory.getLogger(FindConfigFileTest.class);

	@Before
	public void init() throws NoSuchFieldException, IllegalAccessException, IOException {
		LOGGER.info("init FindConfigFileTest");
		TVFSTestTools.reinitConfig();

		LOGGER.info("init FindConfigFileTest end");
	}

	@After
	public void terminate() throws NoSuchFieldException, IllegalAccessException {
		LOGGER.info("terminate FindConfigFileTest");
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, "");
		TVFSTestTools.reinitConfig();
		LOGGER.info("terminate FindConfigFileTest end");
	}

	@Test
	public void testFindFilePropertiesOK() throws Exception {
		LOGGER.info("testFindFilePropertiesOK");

		Path pRef = ObjectTools.createConfigFile();
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, pRef.toString());
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile();
		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertEquals(pRef, p);
	}

	@Test
	public void testFindFileEnvOK() throws Exception {
		LOGGER.info("testFindFileEnvOK");

		Path pRef = ObjectTools.createConfigFile();
		final String pathRef = pRef.toString();
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile() {
			@Override
			protected String getFileFromEnv() {
				return pathRef;
			}
		};
		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertEquals(pRef, p);
	}

	@Test
	public void testFindFileResource1OK() throws Exception {
		LOGGER.info("testFindFileResource1OK");

		String nomFichier = "examples/conf/config1.properties";
		LOGGER.info("nomFichier={}", nomFichier);

		URL url = FindConfigFileTest.class.getClassLoader().getResource(nomFichier);
		LOGGER.info("url={}", url);
		assertNotNull(url);
		Path pRef = Paths.get(url.toURI());
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile();
		List<String> liste = Arrays.asList(nomFichier);
		findConfigFile.setConfigFileNames(liste);

		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertEquals(pRef, p);
	}

	@Test
	public void testFindFileResource2OK() throws Exception {
		LOGGER.info("testFindFileResource2OK");

		String nomFichier = "examples/conf/config1.properties";
		LOGGER.info("nomFichier={}", nomFichier);

		URL url = FindConfigFileTest.class.getClassLoader().getResource(nomFichier);
		LOGGER.info("url={}", url);
		assertNotNull(url);
		Path pRef = Paths.get(url.toURI());
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile();
		List<String> liste = Arrays.asList("toto.properties", nomFichier);
		findConfigFile.setConfigFileNames(liste);

		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertEquals(pRef, p);
	}

	@Test
	public void testFindFilePropertiesNoFileKO() throws Exception {
		LOGGER.info("testFindFilePropertiesNoFileKO");

		String s = System.getProperty(FindConfigFile.TVFS_PROPERTIES);
		assertNull(s);

		FindConfigFile findConfigFile = new FindConfigFile();
		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertNull(p);
	}

}