package org.tinyvfs.path;

import org.junit.Before;
import org.junit.Test;
import org.tinyvfs.VirtualFS;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.tinyvfs.TVFSTools.toList;
import static org.tinyvfs.ToolsTests.assertPath;

/**
 * Created by Alain on 14/01/2017.
 */
public class TVFSAbsolutePathTest {

	private static final String NAME = "root1";
	private VirtualFS virtualFS;

	@Before
	public void init() {
		virtualFS = mock(VirtualFS.class);

		TVFSRootName name = new TVFSRootName(NAME);
		when(virtualFS.getName()).thenReturn(name);
	}

	@Test
	public void testIsAbsoluteOK() throws Exception {

		TVFSAbsolutePath p1 = getPath();
		assertTrue(p1.isAbsolute());

		p1 = getPath("aaa", "bbb", "ccc");
		assertTrue(p1.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p1);
	}

	@Test
	public void testToStringOK() throws Exception {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(NAME + ":", p1.toString());

		p1 = getPath("aaa", "bbb", "ccc");
		assertEquals(NAME + ":/aaa/bbb/ccc", p1.toString());
		assertPath(toList("aaa", "bbb", "ccc"), p1);
	}

	@Test
	public void testGetRootOK() throws Exception {
		TVFSAbsolutePath p1 = getPath();
		assertPath(toList(), p1);
		Path p = p1.getRoot();
		assertTrue(p instanceof TVFSAbsolutePath);
		assertEquals(NAME + ":", p.toString());
		assertPath(toList(), p);

		p1 = getPath("aaa", "bbbb", "cccc");
		assertPath(toList("aaa", "bbbb", "cccc"), p1);
		p = p1.getRoot();
		assertTrue(p instanceof TVFSAbsolutePath);
		assertEquals(NAME + ":", p.toString());
		assertPath(toList(), p);
	}

	@Test
	public void testGetFileName() {
		TVFSAbsolutePath p1 = getPath();
		assertNull(p1.getFileName());

		p1 = getPath("aaaa", "bbb", "cccc");
		assertPath(toList("cccc"), p1.getFileName());

		p1 = getPath("aaaa");
		assertPath(toList("aaaa"), p1.getFileName());
	}

	@Test
	public void testGetParent() {
		TVFSAbsolutePath p1 = getPath();
		assertNull(p1.getParent());

		p1 = getPath("aaaa", "bbb", "cccc");
		assertPath(toList("bbb"), p1.getParent());

		p1 = getPath("aaaa", "bbb");
		assertPath(toList("aaaa"), p1.getParent());

		p1 = getPath("aaaa");
		assertNull(p1.getParent());
	}

	@Test
	public void testGetNameCount() {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(0, p1.getNameCount());

		p1 = getPath("aaaa", "bbb", "cccc");
		assertEquals(3, p1.getNameCount());

		p1 = getPath("aaaa", "bbb");
		assertEquals(2, p1.getNameCount());

		p1 = getPath("aaaa");
		assertEquals(1, p1.getNameCount());
	}

	@Test
	public void testGetNameVide() {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(0, p1.getNameCount());
		try {
			p1.getName(0);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index 0 invalide", e.getMessage());
		}
		try {
			p1.getName(-1);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index -1 invalide", e.getMessage());
		}
	}

	@Test
	public void testGetName1Path() {
		TVFSAbsolutePath p1 = getPath("aaa");
		assertEquals(1, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		try {
			p1.getName(1);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index 1 invalide", e.getMessage());
		}
		try {
			p1.getName(-1);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index -1 invalide", e.getMessage());
		}
	}

	@Test
	public void testGetName2Path() {
		TVFSAbsolutePath p1 = getPath("aaa", "bbb");
		assertEquals(2, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		try {
			p1.getName(2);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index 2 invalide", e.getMessage());
		}
		try {
			p1.getName(-1);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index -1 invalide", e.getMessage());
		}
	}

	@Test
	public void testGetName3Path() {
		TVFSAbsolutePath p1 = getPath("aaa", "bbb", "ccc");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));
		try {
			p1.getName(3);
			fail("Erreur");
		} catch (IllegalArgumentException e) {
			assertEquals("Index 3 invalide", e.getMessage());
		}
	}

	// tools method

	private TVFSAbsolutePath getPath(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(virtualFS, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSAbsolutePath(virtualFS, liste);
		}
	}

}