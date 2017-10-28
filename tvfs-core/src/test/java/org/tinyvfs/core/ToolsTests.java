package org.tinyvfs.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Alain on 15/01/2017.
 */
public final class ToolsTests {

	private static final AtomicInteger compteur = new AtomicInteger(0);


	private ToolsTests() {
	}

	public static void assertPath(List<String> liste, Path p) {
		assertNotNull(liste);
		assertNotNull(p);
		List<String> liste2 = new ArrayList<>();
		for (int i = 0; i < p.getNameCount(); i++) {
			Path p2 = p.getName(i);
			String s = p2.toString();
			if (s.startsWith("/"))
				s = s.substring(1);
			liste2.add(s);
		}
		assertEquals(liste, liste2);
	}

	public static int getCompteur() {
		return compteur.getAndIncrement();
	}
}
