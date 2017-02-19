package org.tinyvfs.fs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.Test1;
import org.tinyvfs.fs.TVFileSystem;
import org.tinyvfs.fs.VirtualFS;
import org.tinyvfs.path.TVFSAbsolutePath;
import org.tinyvfs.path.TVFSRootName;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alain on 01/01/2017.
 */
public class VirtualFSTest {

	final static Logger LOGGER = LoggerFactory.getLogger(Test1.class);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private TVFileSystem tvFileSystem;

	@Before
	public void init() {
		tvFileSystem = mock(TVFileSystem.class);
		when(tvFileSystem.getSeparator()).thenReturn("/");
	}

	@Test
	public void testGetOK() throws Exception {

		LOGGER.info("test testGetOK");

		TVFSRootName name = new TVFSRootName("test1");

		Path rootPath = folder.newFolder().toPath();

		VirtualFS virtualFS = new VirtualFS(tvFileSystem, name, rootPath);

		Path p = virtualFS.get("/a");

		assertTrue(p instanceof TVFSAbsolutePath);

		TVFSAbsolutePath p2 = (TVFSAbsolutePath) p;

		LOGGER.info("ref:" + rootPath.resolve("a"));
		LOGGER.info("realPath:" + p2.getRealPath());

		assertEquals(rootPath.resolve("a"), p2.getRealPath());
	}

	@Test
	public void testGet2OK() throws Exception {

		LOGGER.info("test testGet2OK");

		TVFSRootName name = new TVFSRootName("test1");

		Path rootPath = folder.newFolder().toPath();

		VirtualFS virtualFS = new VirtualFS(tvFileSystem, name, rootPath);

		Path p = virtualFS.get("/auuuu", "bssss", "cdf");

		assertTrue(p instanceof TVFSAbsolutePath);

		TVFSAbsolutePath p2 = (TVFSAbsolutePath) p;

		LOGGER.info("ref:" + rootPath.resolve(Paths.get("auuuu", "bssss", "cdf")));
		LOGGER.info("realPath:" + p2.getRealPath());

		assertEquals(rootPath.resolve(Paths.get("auuuu", "bssss", "cdf")), p2.getRealPath());
	}


	@Test
	public void testGet3OK() throws Exception {

		LOGGER.info("test testGet3OK");

		TVFSRootName name = new TVFSRootName("test1");

		Path rootPath = folder.newFolder().toPath();

		VirtualFS virtualFS = new VirtualFS(tvFileSystem, name, rootPath);

		Path p = virtualFS.get("/gsdf/vghj/cvnjkl");

		assertTrue(p instanceof TVFSAbsolutePath);

		TVFSAbsolutePath p2 = (TVFSAbsolutePath) p;

		LOGGER.info("ref:" + rootPath.resolve("gsdf/vghj/cvnjkl"));
		LOGGER.info("realPath:" + p2.getRealPath());

		assertEquals(rootPath.resolve("gsdf/vghj/cvnjkl"), p2.getRealPath());
	}

}