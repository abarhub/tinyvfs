package org.tinyvfs.path;

import org.junit.Before;
import org.junit.Test;
import org.tinyvfs.VirtualFS;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	}

	@Test
	public void testToStringOK() throws Exception {
		TVFSAbsolutePath p1 = getPath();
		assertEquals(NAME + ":", p1.toString());

		p1 = getPath("aaa", "bbb", "ccc");
		assertEquals(NAME + ":/aaa/bbb/ccc", p1.toString());
	}

	@Test
	public void testGetRootOK() throws Exception {
		TVFSAbsolutePath p1 = getPath();
		Path p = p1.getRoot();
		assertTrue(p instanceof TVFSAbsolutePath);
		assertEquals(NAME + ":", p.toString());

		p1 = getPath("aaa", "bbbb", "cccc");
		p = p1.getRoot();
		assertTrue(p instanceof TVFSAbsolutePath);
		assertEquals(NAME + ":", p.toString());
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