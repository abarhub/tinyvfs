package org.tinyvfs.core;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.tinyvfs.core.TVFSTools.toList;

/**
 * Created by Alain on 19/02/2017.
 */
public class TVFSPathsTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@After
	public void terminate() {
		TVFileSystem.deconnect();
	}

	@Test
	public void getAbsolutePathOK() throws Exception {

		Path folder = getNewFolder();
		assertNotNull(folder);
		assertTrue(folder.isAbsolute());

		TVFSConfig conf = TVFSRepository.getInstance();
		TVFSRootName name = new TVFSRootName("nom1");
		TVFSConfigParam param = new TVFSConfigParam(name, folder, false);
		conf.add(name, param);

		Path p = TVFSPaths.getAbsolutePath("nom1", "\\aa/bb/cc");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		ToolsTests.assertPath(toList("aa", "bb", "cc"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "/aa/bb/cc");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("aa", "bb", "cc"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa2/bb2/cc2");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("aa2", "bb2", "cc2"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa3");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("aa3"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa3", "aa4", "aa5", "aa6");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("aa3", "aa4", "aa5", "aa6"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList(), p);

		p = TVFSPaths.getAbsolutePath("nom1");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList(), p);
	}

	@Test
	public void getAbsolutePath2OK() throws Exception {

		Path folder = getNewFolder();
		assertNotNull(folder);
		assertTrue(folder.isAbsolute());

		TVFSConfig conf = TVFSRepository.getInstance();
		TVFSRootName name = new TVFSRootName("nom1");
		TVFSConfigParam param = new TVFSConfigParam(name, folder, false);
		conf.add(name, param);

		folder = getNewFolder();
		TVFSRootName name2 = new TVFSRootName("nom2");
		param = new TVFSConfigParam(name2, folder, false);
		conf.add(name2, param);

		Path p = TVFSPaths.getAbsolutePath("nom1", "fff/ggg");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("fff", "ggg"), p);
		assertEquals("nom1", ((TVFileSystem) p.getFileSystem()).getName().getName());
		assertEquals(name, ((TVFileSystem) p.getFileSystem()).getName());

		p = TVFSPaths.getAbsolutePath("nom2", "fff/ggg");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		ToolsTests.assertPath(toList("fff", "ggg"), p);
		assertEquals("nom2", ((TVFileSystem) p.getFileSystem()).getName().getName());
		assertEquals(name2, ((TVFileSystem) p.getFileSystem()).getName());
	}

	@Test
	public void getRelativePath() throws Exception {
		Path p = TVFSPaths.getRelativePath("aa2/bb2/cc2");

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		ToolsTests.assertPath(toList("aa2", "bb2", "cc2"), p);

		p = TVFSPaths.getRelativePath("aa3", "bb3", "cc3");

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		ToolsTests.assertPath(toList("aa3", "bb3", "cc3"), p);

		p = TVFSPaths.getRelativePath();

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(0, p.getNameCount());
		ToolsTests.assertPath(toList(), p);
	}

	// tools method

	private Path getNewFolder() throws IOException {
		File f = folder.newFolder();
		return f.toPath();
	}

}