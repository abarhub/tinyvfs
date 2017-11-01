package org.tinyvfs.core.path;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.tinyvfs.core.fs.VirtualFSProvider.SCHEME;

public class TVFSURITest {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSURITest.class);

	@Test
	public void test1() throws Exception {
		LOGGER.info("test1");
		final URI uri = URI.create(SCHEME + ":nom1:/test");
		assertNotNull(uri);
		TVFSURI tvfsuri = new TVFSURI(uri);

		assertEquals("nom1", tvfsuri.getName().getName());
		assertEquals("/test", tvfsuri.getPath());
	}

	@Test
	public void test2() throws Exception {
		LOGGER.info("test2");
		final URI uri = URI.create(SCHEME + ":nom2:/test2");
		assertNotNull(uri);
		TVFSURI tvfsuri = new TVFSURI(uri);

		assertEquals("nom2", tvfsuri.getName().getName());
		assertEquals("/test2", tvfsuri.getPath());
	}
}