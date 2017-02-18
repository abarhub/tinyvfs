package org.tinyvfs.path;

import org.junit.Before;
import org.junit.Test;
import org.tinyvfs.VirtualFS;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.tinyvfs.TVFSTools.toList;
import static org.tinyvfs.ToolsTests.assertPath;

/**
 * Created by Alain on 18/02/2017.
 */
public class TVFSRelativePathTest {

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

		TVFSRelativePath p1 = getPathRelative();
		assertFalse(p1.isAbsolute());

		p1 = getPathRelative("aaa", "bbb", "ccc");
		assertFalse(p1.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p1);
	}

	@Test
	public void testToStringOK() throws Exception {
		TVFSRelativePath p1 = getPathRelative();
		assertEquals("", p1.toString());

		p1 = getPathRelative("aaa", "bbb", "ccc");
		assertEquals("aaa/bbb/ccc", p1.toString());
		assertPath(toList("aaa", "bbb", "ccc"), p1);
	}

	@Test
	public void testGetFileName() {
		TVFSRelativePath p1 = getPathRelative();
		assertNull(p1.getFileName());

		p1 = getPathRelative("aaaa", "bbb", "cccc");
		assertPath(toList("cccc"), p1.getFileName());

		p1 = getPathRelative("aaaa");
		assertPath(toList("aaaa"), p1.getFileName());
	}

	@Test
	public void testGetParent() {
		TVFSRelativePath p1 = getPathRelative();
		assertNull(p1.getParent());

		p1 = getPathRelative("aaaa", "bbb", "cccc");
		assertPath(toList("bbb"), p1.getParent());

		p1 = getPathRelative("aaaa", "bbb");
		assertPath(toList("aaaa"), p1.getParent());

		p1 = getPathRelative("aaaa");
		assertNull(p1.getParent());
	}

	@Test
	public void testGetNameCount() {
		TVFSRelativePath p1 = getPathRelative();
		assertEquals(0, p1.getNameCount());

		p1 = getPathRelative("aaaa", "bbb", "cccc");
		assertEquals(3, p1.getNameCount());

		p1 = getPathRelative("aaaa", "bbb");
		assertEquals(2, p1.getNameCount());

		p1 = getPathRelative("aaaa");
		assertEquals(1, p1.getNameCount());
	}

	@Test
	public void testResolvePathOK() {
		Path p1, p2, p3;

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p2 = getPathRelative("ddd");

		p3 = p1.resolve(p2);
		assertEquals(4, p3.getNameCount());
		assertPath(toList("aaa"), p3.getName(0));
		assertPath(toList("bbb"), p3.getName(1));
		assertPath(toList("ccc"), p3.getName(2));
		assertPath(toList("ddd"), p3.getName(3));
		assertPath(toList("aaa", "bbb", "ccc", "ddd"), p3);
		assertFalse(p3.isAbsolute());

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p2 = getPath("fff");

		p3 = p1.resolve(p2);
		assertTrue(p3.isAbsolute());
		assertPath(toList("fff"), p3);

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p2 = getPathRelative("");

		p3 = p1.resolve(p2);
		assertFalse(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p3);
	}

	@Test
	public void testResolvePathKO() {
		Path p1, p2, p3;

		p1 = getPathRelative("aaa", "bbb", "ccc");

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

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p3 = p1.resolve("ddd");
		assertEquals(4, p3.getNameCount());
		assertPath(toList("aaa"), p3.getName(0));
		assertPath(toList("bbb"), p3.getName(1));
		assertPath(toList("ccc"), p3.getName(2));
		assertPath(toList("ddd"), p3.getName(3));
		assertPath(toList("aaa", "bbb", "ccc", "ddd"), p3);
		assertFalse(p3.isAbsolute());

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p3 = p1.resolve("fff");
		assertFalse(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc", "fff"), p3);

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p3 = p1.resolve("fff/ggg");
		assertFalse(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc", "fff", "ggg"), p3);

		p1 = getPathRelative("aaa", "bbb", "ccc");

		p3 = p1.resolve("");
		assertFalse(p3.isAbsolute());
		assertPath(toList("aaa", "bbb", "ccc"), p3);
	}

	@Test
	public void testResolveStringKO() {
		Path p1, p3;

		p1 = getPathRelative("aaa", "bbb", "ccc");

		try {
			p3 = p1.resolve((String) null);
			fail("Error");
		} catch (IllegalArgumentException e) {
			assertEquals("param must not be null", e.getMessage());
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

	private TVFSRelativePath getPathRelative(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSRelativePath(virtualFS, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSRelativePath(virtualFS, liste);
		}
	}
}