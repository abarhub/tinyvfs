package org.tinyvfs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.config.TVFSConfigParam;
import org.tinyvfs.path.TVFSAbstractPath;
import org.tinyvfs.path.TVFSRootName;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Created by Alain on 01/01/2017.
 */
public class VirtualFSProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();


	@Before
	public void init() throws Exception {

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
		URI uri = URI.create("tvfs://$test1/aaa/bbb/ccc");
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();

		FileSystem fs = virtualFSProvider.newFileSystem(uri, null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;
		fs2.add(new TVFSConfigParam(new TVFSRootName("test1"), folder.newFolder().toPath(), true));
		Path path = virtualFSProvider.getPath(uri);
		assertNotNull(path);
		assertTrue(path instanceof TVFSAbstractPath);

		TVFSAbstractPath p = (TVFSAbstractPath) path;
		assertEquals(3, p.getNameCount());
		assertEquals("/aaa", p.getName(0).toString());
		assertEquals("/bbb", p.getName(1).toString());
		assertEquals("/ccc", p.getName(2).toString());
		assertTrue(fs == p.getFileSystem());
		assertEquals("/aaa/bbb/ccc", p.toString());
	}
}