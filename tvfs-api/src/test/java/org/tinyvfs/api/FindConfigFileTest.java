package org.tinyvfs.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
		assertTrue(s == null || s.isEmpty());

		FindConfigFile findConfigFile = new FindConfigFile();
		// methode testé
		Path p = findConfigFile.findFile();

		// vérifications
		LOGGER.info("p={}", p);
		assertNull(p);
	}

	@Test
	public void testFindFileFileIsDirectoryKO() throws Exception {
		LOGGER.info("testFindFileFileIsDirectoryKO");

		Path pRef = Files.createTempDirectory("tvfstemp");
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, pRef.toString());
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile();
		try {
			// methode testé
			findConfigFile.findFile();

			fail("Error");
		} catch (IOException e) {
			assertEquals("File " + pRef + " is directory", e.getMessage());
		}
	}

	@Test
	public void testFindFileFileNotExistKO() throws Exception {
		LOGGER.info("testFindFileFileNotExistKO");

		Path pRef = Files.createTempDirectory("tvfstemp").resolve("confInexistant.properties");
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, pRef.toString());
		LOGGER.info("pRef={}", pRef);

		FindConfigFile findConfigFile = new FindConfigFile();
		try {
			// methode testé
			findConfigFile.findFile();

			fail("Error");
		} catch (FileNotFoundException e) {
			assertEquals("File " + pRef + " not exists", e.getMessage());
		}
	}

	@Test
	public void testFindFilePathOK() throws Exception {
		LOGGER.info("testFindFilePathOK");

		String currentDirBackup = System.getProperty("user.dir");
		LOGGER.info("currentDirBackup={}", currentDirBackup);
		try {
			//Path pRef = ObjectTools.createConfigFile();
			String contenu = ObjectTools.getConfig1();

			Path p3 = Paths.get(FindConfigFile.TVFS_DIRECTORY, FindConfigFile.TVFS_CONFIGFILE);
			LOGGER.info("p3_1={}", p3);
			LOGGER.info("p3_1bis={}", p3.getName(0));
			assertFalse(p3.isAbsolute());
			while (p3.getNameCount() > 0 && p3.getName(0).toString().equals(".")) {
				p3 = p3.subpath(1, p3.getNameCount());
				LOGGER.info("p3_2={}", p3);
			}
			assertFalse(p3.isAbsolute());
			LOGGER.info("p3_3={}", p3);

			Path temp = Files.createTempDirectory("tvfstest");
			LOGGER.info("temp={}", temp);
			Path confDir = temp.resolve(p3);
			LOGGER.info("confDir={}", confDir);
			Path p2 = Files.createDirectories(confDir.getParent());
			LOGGER.info("p2={}", p2);
			//System.setProperty(FindConfigFile.TVFS_PROPERTIES, pRef.toString());
			Files.write(confDir, contenu.getBytes(StandardCharsets.UTF_8));
			LOGGER.info("pRef={}", confDir);

			System.setProperty("user.dir", temp.toString());
			LOGGER.info("currentDir={}", temp);

			FindConfigFile findConfigFile = new FindConfigFile();
			// methode testé
			Path p = findConfigFile.findFile();

			// vérifications
			LOGGER.info("p={}", p);
			assertEquals(confDir, p);
		} finally {
			System.setProperty("user.dir", currentDirBackup);
			LOGGER.info("currentDirBackup_end={}", currentDirBackup);
		}
	}
}