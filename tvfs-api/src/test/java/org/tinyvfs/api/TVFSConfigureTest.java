package org.tinyvfs.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSPaths;
import org.tinyvfs.core.fs.VirtualFSProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import static org.junit.Assert.*;

/**
 * Created by Alain on 27/05/2017.
 */
public class TVFSConfigureTest {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfigureTest.class);

	@Before
	public void init() throws NoSuchFieldException, IllegalAccessException, IOException {
		LOGGER.info("init TVFSRepository");
		TVFSTestTools.reinitConfig();

		Path p = ObjectTools.createConfigFile();
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, p.toString());
		LOGGER.info("init TVFSRepository end");
	}

	@After
	public void terminate() throws NoSuchFieldException, IllegalAccessException {
		LOGGER.info("terminate TVFSRepository");
		System.setProperty(FindConfigFile.TVFS_PROPERTIES, "");
		TVFSTestTools.reinitConfig();
		LOGGER.info("terminate TVFSRepository end");
	}

	@Test
	@Ignore
	public void testConfigure1Ok() throws Exception {
		LOGGER.info("configure");

		String chemin;

		chemin = "tvfs://nom1/test1.txt";
		chemin = VirtualFSProvider.SCHEME + "://nom1/test1.txt";

		File f = new File(chemin);

		File f2 = new File("tvfs://test");

		Files.write(f.toPath(), "test".getBytes(StandardCharsets.UTF_8));

		LOGGER.info("configure fini");
	}

	@Test
	public void testConfigure01Ok() throws Exception {
		LOGGER.info("configure 01");

		Path root = Paths.get(createUri("nom1", "/test01.txt"));


		final String message = "test_159";
		final byte[] messageBytes = getByte(message);

		LOGGER.info("write {}", message);

		Files.write(root, messageBytes);

		byte buf[] = Files.readAllBytes(root);

		assertArrayEquals(messageBytes, buf);

		String messageRes = new String(buf, StandardCharsets.UTF_8);
		LOGGER.info("read {}", messageRes);

		assertEquals(message, messageRes);

		LOGGER.info("configure fini");
	}

	@Test
	public void testConstructorOk() {
		LOGGER.info("configure2");

		TVFSConfigure configure = new TVFSConfigure();

		LOGGER.info("configure fini");
	}

	@Test
	public void testConfigure2Ok() throws Exception {
		LOGGER.info("configure3");
		Path p = TVFSPaths.getAbsolutePath("nom1", "test1.txt");

		final String message = "test abc";
		final byte[] messageBytes = getByte(message);

		LOGGER.info("write {}", message);

		Files.write(p, messageBytes);

		byte buf[] = Files.readAllBytes(p);

		assertArrayEquals(messageBytes, buf);

		String messageRes = new String(buf, StandardCharsets.UTF_8);
		LOGGER.info("read {}", messageRes);

		assertEquals(message, messageRes);

		LOGGER.info("configure fini");
	}


	@Test
	public void testConfigureNoFileKO() {
		LOGGER.info("testConfigureNoFileKO");

		System.setProperty(FindConfigFile.TVFS_PROPERTIES, "");

		try {
			Paths.get(createUri("nom1", "test01.txt"));

			fail("Error");
		} catch (InvalidParameterException e) {
			LOGGER.error("Error:", e);
			//assertEquals("Erreur: no FS for 'nom1'", e.getMessage());
			assertEquals("env is null", e.getMessage());
		}


		LOGGER.info("testConfigureNoFileKO fini");
	}

	// methodes utilitaires

	private byte[] getByte(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	private URI createUri(String name, String path) {
		return URI.create("tvfs:" + name + ":" + path);
	}

}