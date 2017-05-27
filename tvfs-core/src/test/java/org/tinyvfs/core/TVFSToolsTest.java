package org.tinyvfs.core;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Created by Alain on 19/02/2017.
 */
public class TVFSToolsTest {

	final static Logger LOGGER = LoggerFactory.getLogger(TVFSToolsTest.class);

	@Test
	public void isNameValide() throws Exception {

		LOGGER.info("test isNameValide");

		assertTrue(TVFSTools.isNameValide("abc"));
		assertTrue(TVFSTools.isNameValide("a"));
		assertTrue(TVFSTools.isNameValide("avfvbfdbfdvcbazertyuiopqsdfghjklmwxcvbn"));
		assertTrue(TVFSTools.isNameValide("a.b_c"));
		assertTrue(TVFSTools.isNameValide("AZERTYUIOPQSDFGHJKLMWXCVBN"));
		assertFalse(TVFSTools.isNameValide(".abc"));
		assertFalse(TVFSTools.isNameValide("_abc"));
		assertTrue(TVFSTools.isNameValide("Abc"));
		assertTrue(TVFSTools.isNameValide("zzabc"));
		assertTrue(TVFSTools.isNameValide("ZZabc"));
		assertTrue(TVFSTools.isNameValide("abc0123456789"));
		assertFalse(TVFSTools.isNameValide("0abc0123456789"));

	}

	@Test
	public void isNameValide2() throws Exception {

		LOGGER.info("test isNameValide2");

		Set<Character> listFirstChar, listSecondChar;
		listFirstChar = new TreeSet<>();
		listSecondChar = new TreeSet<>();
		for (char c = 'a'; c <= 'z'; c++) {
			listFirstChar.add(c);
			listSecondChar.add(c);
			listFirstChar.add(Character.toUpperCase(c));
			listSecondChar.add(Character.toUpperCase(c));
		}
		for (char c = '0'; c <= '9'; c++) {
			listSecondChar.add(c);
		}
		listSecondChar.add('_');
		listSecondChar.add('.');

		LOGGER.info("listFirstChar=" + listFirstChar);
		LOGGER.info("listSecondChar=" + listSecondChar);

		int limit = 255;
		// check for all chars
		for (int c1 = 0; c1 < limit; c1++) {
			for (int c2 = 0; c2 < limit; c2++) {
				StringBuilder sb = new StringBuilder();
				char c01 = (char) c1;
				sb.append(c01);
				char c02 = (char) c2;
				sb.append(c02);
				String s = sb.toString();
				//LOGGER.info("s(" + c1 + "," + c2 + ")=" + s);
				if (listFirstChar.contains(c01) && listSecondChar.contains(c02)) {
					assertTrue("s(" + c1 + "," + c2 + ")=" + s, TVFSTools.isNameValide(s));
				} else {
					assertFalse("s(" + c1 + "," + c2 + ")=" + s, TVFSTools.isNameValide(s));
				}
			}
		}
	}


	@Test
	public void checkParam() throws Exception {
		LOGGER.info("test checkParam");
		TVFSTools.checkParam(true, "message");
		try {
			TVFSTools.checkParam(false, "message2");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("message2", e.getMessage());
		}
		try {
			TVFSTools.checkParam(false, "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParam(false, null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParam(true, null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParam(true, "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void checkParamNotNull() throws Exception {
		LOGGER.info("test checkParamNotNull");
		TVFSTools.checkParamNotNull(new Object(), "message");
		try {
			TVFSTools.checkParamNotNull(null, "message2");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("message2", e.getMessage());
		}
		try {
			TVFSTools.checkParamNotNull(null, "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParamNotNull(null, null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParamNotNull(new Object(), null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkParamNotNull(new Object(), "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void checkIsNotEmpty() throws Exception {
		LOGGER.info("test checkIsNotEmpty");
		TVFSTools.checkIsNotEmpty("abcdef", "message");
		try {
			TVFSTools.checkIsNotEmpty("", "message2");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("message2", e.getMessage());
		}
		try {
			TVFSTools.checkIsNotEmpty(null, "message2");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("message2", e.getMessage());
		}
		try {
			TVFSTools.checkIsNotEmpty(null, "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkIsNotEmpty(null, null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkIsNotEmpty("abcdef", null);
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
		try {
			TVFSTools.checkIsNotEmpty("abcdef", "");
			fail("Error fail");
		} catch (InvalidParameterException e) {
			assertEquals("Error", e.getMessage());
		}
	}

	@Test
	public void unsupportedOperation() throws Exception {
		LOGGER.info("test unsupportedOperation");
		try {
			TVFSTools.unsupportedOperation();
			fail("Error");
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void toList() throws Exception {
		LOGGER.info("test toList");
		List<String> liste = TVFSTools.toList("aaa", "bbb", "ccc");

		assertNotNull(liste);
		assertEquals(3, liste.size());
		assertEquals("aaa", liste.get(0));
		assertEquals("bbb", liste.get(1));
		assertEquals("ccc", liste.get(2));

		liste = TVFSTools.toList("ttt", "vvv");

		assertNotNull(liste);
		assertEquals(2, liste.size());
		assertEquals("ttt", liste.get(0));
		assertEquals("vvv", liste.get(1));

		liste = TVFSTools.toList();

		assertNotNull(liste);
		assertEquals(0, liste.size());

		liste = TVFSTools.toList("DDD");

		assertNotNull(liste);
		assertEquals(1, liste.size());
		assertEquals("DDD", liste.get(0));

		List<Integer> liste2 = TVFSTools.toList(7, 10, 15);

		assertNotNull(liste2);
		assertEquals(3, liste2.size());
		assertEquals(7, (int) liste2.get(0));
		assertEquals(10, (int) liste2.get(1));
		assertEquals(15, (int) liste2.get(2));
	}
}