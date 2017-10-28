package org.tinyvfs.core.utilisation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSPaths;
import org.tinyvfs.core.TVFSTestTools;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFSProvider;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

public class TestUseFiles {

	private final static Logger LOGGER = LoggerFactory.getLogger(TestUseFiles.class);
	private final String TEST_NAME = "test05";
	private final String TEST_NAME2 = "test06";

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Rule
	public TestName name = new TestName();

	@Before
	public void init() throws NoSuchFieldException, IllegalAccessException {
		//TVFSRepository.clearInstance();
		TVFSTestTools.reinitConfig();
	}

	@After
	public void tearDown() throws Exception {
		TVFSTestTools.reinitConfig();
	}

	@Test
	public void test1() throws IOException {
		TVFileSystem tvFileSystem;
		VirtualFSProvider virtualFSProvider;

		LOGGER.info("Test 1");

		virtualFSProvider = new VirtualFSProvider();

		//tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());
		tvFileSystem = (TVFileSystem) virtualFSProvider.newFileSystem(URI.create("tvfs://test"), null);

		tvFileSystem.add(new TVFSConfigParam(new TVFSRootName("test1"), newTemp(), false));

		Path p = tvFileSystem.getPath("test1", "/toto2.txt");

		LOGGER.info("p=" + p);


		Path p2 = tvFileSystem.getPath("test1", "/toto3.txt");

		LOGGER.info("p2=" + p2);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());
		assertTrue(p.getFileSystem().provider() == virtualFSProvider);

		//Files.createFile(p);
		Files.createFile(p);

		String buf0 = "test 5";

		LOGGER.info("write=" + buf0);

		Files.write(p, buf0.getBytes("UTF-8"), StandardOpenOption.APPEND);

		Files.copy(p, p2);

		byte[] buf = Files.readAllBytes(p2);

		String s = new String(buf, "UTF-8");

		LOGGER.info("read=" + s);

	}

	@Test
	public void testCopie() throws IOException {
		LOGGER.info("testCopie");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME, "fichier1.txt");

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		Path p2 = TVFSPaths.getAbsolutePath(TEST_NAME, "fichier2.txt");

		LOGGER.info("p2={}", p2);

		assertFalse(Files.exists(p));
		assertFalse(Files.exists(p2));

		LOGGER.info("creation fichier={}", p);
		Files.createFile(p);

		final String buf0 = "test 8";

		LOGGER.info("write={}", buf0);

		Files.write(p, buf0.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);

		assertTrue(Files.exists(p));
		assertFalse(Files.exists(p2));

		Files.copy(p, p2);

		assertTrue(Files.exists(p));
		assertTrue(Files.exists(p2));

		byte[] buf = Files.readAllBytes(p2);

		final String s = new String(buf, StandardCharsets.UTF_8);

		LOGGER.info("read={}", s);

		assertEquals(buf0, s);

	}

	@Test
	public void testCreateFile() throws IOException {
		LOGGER.info("testCreateFile");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME, "fichier01.txt");

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		assertFalse(Files.exists(p));

		// test
		LOGGER.info("creation fichier {}", p);
		Files.createFile(p);

		// vérifications
		assertTrue(Files.exists(p));
		assertFalse(Files.isDirectory(p));
	}

	@Test
	public void testCreateDirectory() throws IOException {
		LOGGER.info("testCreateDirectory");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME, "myrep");

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		assertFalse(Files.exists(p));

		// test
		Files.createDirectory(p);

		// vérifications
		assertTrue(Files.exists(p));
		assertTrue(Files.isDirectory(p));
	}

	@Test
	//@Ignore
	public void testCreateMultipeDirectories() throws IOException {
		LOGGER.info("testCreateMultipeDirectories");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME, "myrep/myrep2");

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		assertFalse(Files.exists(p));

		// test
		Files.createDirectories(p);

		// vérifications
		assertTrue(Files.exists(p));
		assertTrue(Files.isDirectory(p));
	}

	@Test
	public void testRootExists() throws IOException {
		LOGGER.info("testRootExists");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME);

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		// test
		boolean exists = Files.exists(p);

		// vérifications
		assertTrue(exists);
	}

	@Test
	public void testParentExists() throws IOException {
		LOGGER.info("testParentExists");

		configFS();

		Path p = TVFSPaths.getAbsolutePath(TEST_NAME, "myrep");

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());

		// test
		boolean exists = Files.exists(p.getParent());

		// vérifications
		assertTrue(exists);
	}

	// methodes utilitaires

	private void configFS() throws IOException {
		configFS(TEST_NAME);
	}

	private void configFS(String name) throws IOException {
		assertNotNull(name);
		TVFSRootName test1 = new TVFSRootName(name);
		Path tempdir = newTemp();
		LOGGER.debug("tempdir={}", tempdir);
		boolean exists = Files.exists(tempdir);
		LOGGER.debug("exists={}", exists);
		TVFSConfigParam configParam = new TVFSConfigParam(test1, tempdir, false);

		TVFSRepository.getInstance().add(test1, configParam);
	}

	private Path newTemp() throws IOException {
		File f = folder.newFolder();
		return f.toPath();
	}
}
