package org.tinyvfs;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.config.TVFSConfig2;
import org.tinyvfs.config.TVFSConfigParam;
import org.tinyvfs.config.TVFSRepository;
import org.tinyvfs.path.TVFSAbsolutePath;
import org.tinyvfs.path.TVFSRootName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.tinyvfs.TVFSTools.toList;
import static org.tinyvfs.ToolsTests.assertPath;

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

		TVFSConfig2 conf = TVFSRepository.getInstance();
		TVFSRootName name = new TVFSRootName("nom1");
		TVFSConfigParam param = new TVFSConfigParam(name, folder, false);
		conf.add(name, param);

		Path p = TVFSPaths.getAbsolutePath("nom1", "\\aa/bb/cc");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		assertPath(toList("aa", "bb", "cc"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "/aa/bb/cc");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList("aa", "bb", "cc"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa2/bb2/cc2");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList("aa2", "bb2", "cc2"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa3");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList("aa3"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "aa3", "aa4", "aa5", "aa6");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList("aa3", "aa4", "aa5", "aa6"), p);

		p = TVFSPaths.getAbsolutePath("nom1", "");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList(), p);

		p = TVFSPaths.getAbsolutePath("nom1");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList(), p);
	}

	@Test
	public void getAbsolutePath2OK() throws Exception {

		Path folder = getNewFolder();
		assertNotNull(folder);
		assertTrue(folder.isAbsolute());

		TVFSConfig2 conf = TVFSRepository.getInstance();
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
		assertPath(toList("fff", "ggg"), p);
		assertEquals("nom1", ((TVFSAbsolutePath) p).getVirtualFS().getName().getName());
		assertEquals(name, ((TVFSAbsolutePath) p).getVirtualFS().getName());

		p = TVFSPaths.getAbsolutePath("nom2", "fff/ggg");

		assertNotNull(p);
		assertTrue(p.isAbsolute());
		assertPath(toList("fff", "ggg"), p);
		assertEquals("nom2", ((TVFSAbsolutePath) p).getVirtualFS().getName().getName());
		assertEquals(name2, ((TVFSAbsolutePath) p).getVirtualFS().getName());
	}

	@Test
	public void getRelativePath() throws Exception {
		Path p = TVFSPaths.getRelativePath("aa2/bb2/cc2");

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		assertPath(toList("aa2", "bb2", "cc2"), p);

		p = TVFSPaths.getRelativePath("aa3", "bb3", "cc3");

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(3, p.getNameCount());
		assertPath(toList("aa3", "bb3", "cc3"), p);

		p = TVFSPaths.getRelativePath();

		assertNotNull(p);
		assertFalse(p.isAbsolute());
		assertEquals(0, p.getNameCount());
		assertPath(toList(), p);
	}

	// tools method

	private Path getNewFolder() throws IOException {
		File f = folder.newFolder();
		return f.toPath();
	}

}