package org.tinyvfs.core.fs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.path.TVFSAbstractPath;
import org.tinyvfs.core.path.TVFSRootName;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.tinyvfs.core.TVFSTools.toList;
import static org.tinyvfs.core.ToolsTests.assertPath;

/**
 * Created by Alain on 01/01/2017.
 */
public class VirtualFSProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();


	@Before
	public void init() throws Exception {
		TVFSRepository.clearInstance();
	}

	@Test
	public void testGetSchemeOK() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		assertEquals("tvfs", virtualFSProvider.getScheme());
		assertEquals("tvfs", VirtualFSProvider.SCHEME);
	}

	@Test
	public void testNewFileSystemOK() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		FileSystem fs = virtualFSProvider.newFileSystem(URI.create("tvfs://test1"), null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
	}

	@Test
	public void testNewFileSystem2FoisKO() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		FileSystem fs = virtualFSProvider.newFileSystem(URI.create("tvfs://test1"), null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		try {
			fs = virtualFSProvider.newFileSystem(URI.create("tvfs://test1"), null);
			fail("Error");
		} catch (FileSystemAlreadyExistsException e) {
			assertEquals("Le FS existe déjà", e.getMessage());
		}
	}

	@Test
	public void testGetFileSystemOK() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		URI uri = URI.create("tvfs://test1");
		FileSystem fs = virtualFSProvider.newFileSystem(uri, null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);

		FileSystem fs2 = virtualFSProvider.getFileSystem(uri);
		assertNotNull(fs2);
		assertTrue(fs == fs2);
	}

	@Test
	public void testGetFileSystemFSInexistantKO() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		URI uri = URI.create("tvfs://test1");

		try {
			FileSystem fs2 = virtualFSProvider.getFileSystem(uri);
			fail("Error");
		} catch (FileSystemNotFoundException e) {
			assertEquals("Le FS n'existe pas", e.getMessage());
		}
	}

	@Test
	public void testGetPathOK() throws Exception {
		URI uri = URI.create("tvfs://test1/aaa/bbb/ccc");
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();

		FileSystem fs = virtualFSProvider.newFileSystem(uri, null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;
		virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), folder.newFolder().toPath(), true));
		Path path = virtualFSProvider.getPath(uri);
		assertNotNull(path);
		assertTrue(path instanceof TVFSAbstractPath);

		TVFSAbstractPath p = (TVFSAbstractPath) path;
		assertEquals(3, p.getNameCount());
		assertEquals("aaa", p.getName(0).toString());
		assertEquals("bbb", p.getName(1).toString());
		assertEquals("ccc", p.getName(2).toString());
		assertPath(toList("aaa", "bbb", "ccc"), p);
		assertTrue(fs == p.getFileSystem());
		assertEquals("test1:/aaa/bbb/ccc", p.toString());
	}

	@Test
	public void testGetPathSlashDoubleOK() throws Exception {
		URI uri = URI.create("tvfs://test1///aaaa/bbbbbb//cccc//dddd");
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();

		FileSystem fs = virtualFSProvider.newFileSystem(uri, null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;
		virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), folder.newFolder().toPath(), true));
		Path path = virtualFSProvider.getPath(uri);
		assertNotNull(path);
		assertTrue(path instanceof TVFSAbstractPath);

		TVFSAbstractPath p = (TVFSAbstractPath) path;
		assertEquals(4, p.getNameCount());
		assertEquals("aaaa", p.getName(0).toString());
		assertEquals("bbbbbb", p.getName(1).toString());
		assertEquals("cccc", p.getName(2).toString());
		assertEquals("dddd", p.getName(3).toString());
		assertPath(toList("aaaa", "bbbbbb", "cccc", "dddd"), p);
		assertTrue(fs == p.getFileSystem());
		assertEquals("test1:/aaaa/bbbbbb/cccc/dddd", p.toString());
	}

	@Test
	public void testGetPathSlashFinOK() throws Exception {
		URI uri = URI.create("tvfs://test1/aaaa/bbbbbb/cccc/dddd/");
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();

		FileSystem fs = virtualFSProvider.newFileSystem(uri, null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;
		virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), folder.newFolder().toPath(), true));
		Path path = virtualFSProvider.getPath(uri);
		assertNotNull(path);
		assertTrue(path instanceof TVFSAbstractPath);

		TVFSAbstractPath p = (TVFSAbstractPath) path;
		assertEquals(4, p.getNameCount());
		assertEquals("aaaa", p.getName(0).toString());
		assertEquals("bbbbbb", p.getName(1).toString());
		assertEquals("cccc", p.getName(2).toString());
		assertEquals("dddd", p.getName(3).toString());
		assertPath(toList("aaaa", "bbbbbb", "cccc", "dddd"), p);
		assertTrue(fs == p.getFileSystem());
		assertEquals("test1:/aaaa/bbbbbb/cccc/dddd", p.toString());
	}


}