package org.tinyvfs.core.utilisation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TestFixture;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFSProvider;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alain on 11/12/2016.
 */
public class Test1 {

	final static Logger LOGGER = LoggerFactory.getLogger(Test1.class);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private VirtualFSProvider virtualFSProvider;

	@Before
	public void init() {
		TVFSRepository.clearInstance();
		virtualFSProvider = new VirtualFSProvider();
	}

	@Test
	public void test1() throws IOException {
		TVFileSystem tvFileSystem;

		LOGGER.info("Test 1");

//		VirtualFSProvider virtualFSProvider = mock(VirtualFSProvider.class);
//		TVFSConfig tvfsConfig = new TVFSConfig();
//		tvFileSystem = new TVFileSystem(virtualFSProvider, tvfsConfig, FileSystems.getDefault());

		//tvFileSystem.add(new TVFSConfigParam(new TVFSRootName("test1"), newTemp(), false));
		tvFileSystem = (TVFileSystem) createFileSystem("test1", null);

		Path p = tvFileSystem.getPath("test1", "/toto.txt");

		LOGGER.info("p=" + p);

		assertNotNull(p);
	}

	@Test
	public void test2() throws IOException {
		TVFileSystem tvFileSystem;

		LOGGER.info("Test 2");

		//virtualFSProvider = new VirtualFSProvider();

		//tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());
		tvFileSystem = (TVFileSystem) createFileSystem("test", null);

		//virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), newTemp(), false));

		Path p = tvFileSystem.getPath("/toto2.txt");

		LOGGER.info("p={}", p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());
		assertTrue(p.getFileSystem().provider() == virtualFSProvider);

		//Files.createFile(p);
		Files.createFile(p);

		Set<OpenOption> set = new HashSet<>();
		//set.add(StandardOpenOption.READ);
		set.add(StandardOpenOption.CREATE);
		set.add(StandardOpenOption.WRITE);
		SeekableByteChannel s = virtualFSProvider.newByteChannel(p, set, new FileAttribute[0]);
		assertNotNull(s);
	}

	@Test
	public void test3() throws IOException {
		TVFileSystem tvFileSystem;

		LOGGER.info("Test 3");

		//tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());
		tvFileSystem = (TVFileSystem) createFileSystem("test", null);

		virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), newTemp(), false));

		Path p = tvFileSystem.getPath("/toto2.txt");

		LOGGER.info("p=" + p);


		Path p2 = tvFileSystem.getPath("/toto3.txt");

		LOGGER.info("p2=" + p2);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());
		assertTrue(p.getFileSystem().provider() == virtualFSProvider);

		Files.createFile(p);

		String buf0 = "test 5";

		LOGGER.info("write=" + buf0);

		Files.write(p, buf0.getBytes("UTF-8"), StandardOpenOption.APPEND);

		Files.copy(p, p2);

		byte[] buf = Files.readAllBytes(p2);

		String s = new String(buf, "UTF-8");

		LOGGER.info("read=" + s);

	}

	// methodes utilitaires

	private Path newTemp() throws IOException {
		File f = folder.newFolder();
		return f.toPath();
	}

	private URI createUri(String name) {
		return TestFixture.createUri(name);
	}

	private URI createUri(String name, String path) {
		return TestFixture.createUri(name, path);
	}

	private FileSystem createFileSystem(String name, String path) throws IOException {
		return TestFixture.createFileSystem(virtualFSProvider, name, path, createEnv());
	}

	private FileSystem createFileSystem(URI uri) throws IOException {
		return TestFixture.createFileSystem(virtualFSProvider, uri, createEnv());
	}

	private Map<String, String> createEnv(Path rootPath) {
		return TestFixture.createEnv(rootPath);
	}

	private Map<String, String> createEnv() throws IOException {
		return createEnv(folder.newFolder().toPath());
	}

}
