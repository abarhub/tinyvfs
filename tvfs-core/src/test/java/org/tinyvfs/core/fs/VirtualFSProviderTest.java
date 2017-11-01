package org.tinyvfs.core.fs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.TestFixture;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.path.TVFSAbstractPath;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.Assert.*;
import static org.tinyvfs.core.TVFSTools.toList;
import static org.tinyvfs.core.ToolsTests.assertPath;

/**
 * Created by Alain on 01/01/2017.
 */
public class VirtualFSProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private VirtualFSProvider virtualFSProvider;

	@Before
	public void init() throws Exception {
		TVFSRepository.clearInstance();
		virtualFSProvider = new VirtualFSProvider();
	}

	@Test
	public void testGetSchemeOK() throws Exception {
		VirtualFSProvider virtualFSProvider = new VirtualFSProvider();
		// methode testée
		String res = virtualFSProvider.getScheme();

		// vérifications
		assertEquals("tvfs", res);
		assertEquals("tvfs", VirtualFSProvider.SCHEME);
		assertEquals(VirtualFSProvider.SCHEME, res);
	}

	@Test
	public void testNewFileSystemOK() throws Exception {
		// methode testée
		FileSystem fs = createFileSystem("test1", null);

		// vérifications
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		assertEquals("test1", ((TVFileSystem) fs).getName().getName());
	}

	@Test
	public void testNewFileSystem2FoisKO() throws Exception {
		FileSystem fs = createFileSystem("test2", null);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		assertEquals("test2", ((TVFileSystem) fs).getName().getName());
		try {
			// methode testée
			fs = createFileSystem("test2", null);
			fail("Error");
		} catch (FileSystemAlreadyExistsException e) {
			assertEquals("Le FS existe déjà", e.getMessage());
		}
	}

	@Test
	public void testGetFileSystemOK() throws Exception {
		URI uri = createUri("test1");
		FileSystem fs = createFileSystem(uri);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);

		// methode testée
		FileSystem fs2 = virtualFSProvider.getFileSystem(uri);

		// vérifications
		assertNotNull(fs2);
		assertTrue(fs == fs2);
	}

	@Test
	public void testGetFileSystemFSInexistantKO() throws Exception {
		URI uri = createUri("test1");

		try {
			// methode testée
			FileSystem fs2 = virtualFSProvider.getFileSystem(uri);
			fail("Error");
		} catch (FileSystemNotFoundException e) {
			assertEquals("Le FS 'test1' n'existe pas", e.getMessage());
		}
	}

	@Test
	public void testGetPathOK() throws Exception {
		URI uri = createUri("test1", "/aaa/bbb/ccc");

		FileSystem fs = createFileSystem(uri);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);

		// methode testée
		Path path = virtualFSProvider.getPath(uri);

		// vérifications
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
		URI uri = createUri("test1", "///aaaa/bbbbbb//cccc//dddd");

		FileSystem fs = createFileSystem(uri);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;

		// methode testée
		Path path = virtualFSProvider.getPath(uri);

		// vérifications
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
		URI uri = createUri("test1", "/aaaa/bbbbbb/cccc/dddd/");
		//VirtualFSProvider virtualFSProvider = new VirtualFSProvider();

		FileSystem fs = createFileSystem(uri);
		assertNotNull(fs);
		assertTrue(fs instanceof TVFileSystem);
		TVFileSystem fs2 = (TVFileSystem) fs;
		//virtualFSProvider.add(new TVFSConfigParam(new TVFSRootName("test1"), folder.newFolder().toPath(), true));

		// methode testée
		Path path = virtualFSProvider.getPath(uri);

		// vérifications
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

	// methodes utilitaires

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