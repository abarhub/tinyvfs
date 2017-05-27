package org.tinyvfs.core.path;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.tinyvfs.core.TVFSTools.toList;
import static org.tinyvfs.core.ToolsTests.assertPath;

/**
 * Created by Alain on 14/01/2017.
 */
public class TVFSAbsolutePathTest extends TVFSCommonPathTest {

	@Before
	public void init() throws IOException {
		super.init();
	}

	@Test
	public void testIsAbsoluteOK() throws Exception {

		TVFSAbsolutePath p1 = getPath();
		assertTrue(p1.isAbsolute());

		p1 = getPath("/aaa", "bbb", "ccc");
		assertTrue(p1.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p1);
	}

	@Test
	public void testToStringOK() throws Exception {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(NAME + ":", p1.toString());

		p1 = getPath("/aaa", "bbb", "ccc");
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

		p1 = getPath("/aaa", "bbbb", "cccc");
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

		p1 = getPath("/aaaa", "bbb", "cccc");
		assertPath(toList("cccc"), p1.getFileName());

		p1 = getPath("/aaaa");
		assertPath(toList("aaaa"), p1.getFileName());
	}

	@Test
	public void testGetParent() {
		TVFSAbsolutePath p1 = getPath();
		assertNull(p1.getParent());

		p1 = getPath("/aaaa", "bbb", "cccc");
		assertPath(toList("bbb"), p1.getParent());

		p1 = getPath("/aaaa", "bbb");
		assertPath(toList("aaaa"), p1.getParent());

		p1 = getPath("/aaaa");
		assertNull(p1.getParent());
	}

	@Test
	public void testGetNameCount() {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(0, p1.getNameCount());

		p1 = getPath("/aaaa", "bbb", "cccc");
		assertEquals(3, p1.getNameCount());

		p1 = getPath("/aaaa", "bbb");
		assertEquals(2, p1.getNameCount());

		p1 = getPath("/aaaa");
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
		TVFSAbsolutePath p1 = getPath("/aaa");
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
		TVFSAbsolutePath p1 = getPath("/aaa", "bbb");
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
		TVFSAbsolutePath p1 = getPath("/aaa", "bbb", "ccc");
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

	@Test
	public void testConstructor() {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(0, p1.getNameCount());

		p1 = getPath("/");
		assertEquals(0, p1.getNameCount());

		p1 = getPath("/aaa", "bbb", "ccc");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));

		p1 = getPath("/aaa/bbb/ccc");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));

		p1 = getPath("/aaa//bbb///ccc/");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));


		p1 = getPath("\\aaa\\bbb\\ccc");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));

		p1 = getPath("\\aaa\\\\bbb\\\\\\ccc\\");
		assertEquals(3, p1.getNameCount());
		assertPath(toList("aaa"), p1.getName(0));
		assertPath(toList("bbb"), p1.getName(1));
		assertPath(toList("ccc"), p1.getName(2));
	}

	@Test
	public void testResolvePathOK() {
		Path p1, p2, p3;

		p1 = getPath("/aaa", "bbb", "ccc");

		p2 = getPathRelative("ddd");

		p3 = p1.resolve(p2);
		assertEquals(4, p3.getNameCount());
		assertPath(toList("aaa"), p3.getName(0));
		assertPath(toList("bbb"), p3.getName(1));
		assertPath(toList("ccc"), p3.getName(2));
		assertPath(toList("ddd"), p3.getName(3));
		assertPath(toList("aaa", "bbb", "ccc", "ddd"), p3);
		assertTrue(p3.isAbsolute());

		p1 = getPath("/aaa", "bbb", "ccc");

		p2 = getPath("fff");

		p3 = p1.resolve(p2);
		assertTrue(p3.isAbsolute());
		assertPath(toList("fff"), p3);

		p1 = getPath("/aaa", "bbb", "ccc");

		p2 = getPathRelative("");

		p3 = p1.resolve(p2);
		assertTrue(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p3);
	}

	@Test
	public void testResolvePathKO() {
		Path p1, p2, p3;

		p1 = getPath("/aaa", "bbb", "ccc");

		try {
			p3 = p1.resolve((Path) null);
			fail("Error");
		} catch (IllegalArgumentException e) {
			assertEquals("param must not be null", e.getMessage());
		}

		p2 = Paths.get("ddd");

		try {
			p3 = p1.resolve(p2);
			fail("Error");
		} catch (IllegalArgumentException e) {
			assertEquals("param is invalid", e.getMessage());
		}
	}

	@Test
	public void testResolveStringOK() {
		Path p1, p3;

		p1 = getPath("/aaa", "bbb", "ccc");

		p3 = p1.resolve("ddd");
		assertEquals(4, p3.getNameCount());
		assertPath(toList("aaa"), p3.getName(0));
		assertPath(toList("bbb"), p3.getName(1));
		assertPath(toList("ccc"), p3.getName(2));
		assertPath(toList("ddd"), p3.getName(3));
		assertPath(toList("aaa", "bbb", "ccc", "ddd"), p3);
		assertTrue(p3.isAbsolute());

		p1 = getPath("/aaa", "bbb", "ccc");

		p3 = p1.resolve("fff");
		assertTrue(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc", "fff"), p3);

		p1 = getPath("/aaa", "bbb", "ccc");

		p3 = p1.resolve("fff/ggg");
		assertTrue(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc", "fff", "ggg"), p3);

		p1 = getPath("/aaa", "bbb", "ccc");

		p3 = p1.resolve("");
		assertTrue(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p3);
	}

	@Test
	public void testResolveStringKO() {
		Path p1, p3;

		p1 = getPath("/aaa", "bbb", "ccc");

		try {
			p3 = p1.resolve((String) null);
			fail("Error");
		} catch (IllegalArgumentException e) {
			assertEquals("param must not be null", e.getMessage());
		}
	}

	@Test
	public void testEqualsOK() {
		Path p1, p2;

		p1 = getPath("/aaa", "bbb", "ccc");
		p2 = getPath("/aaa", "bbb", "ccc");
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		p1 = getPath("aaa", "bbb", "ccc");
		p2 = getPath("aaa/bbb/ccc");
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		p1 = getPath("aaa", "bbb", "ccc");
		p2 = getPath("aaa//bbb/ccc");
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		p1 = getPath("aaa", "bbb", "ccc");
		p2 = getPath("aaa/bbb///ccc/");
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));

		p1 = getPath("aaa", "bbb", "ccc2");
		p2 = getPath("aaa/bbb/ccc");
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));

		p1 = getPath("aaa", "bbb", "ccc");
		p2 = getPath("aaa/bbb/ccc/ddd");
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}

}