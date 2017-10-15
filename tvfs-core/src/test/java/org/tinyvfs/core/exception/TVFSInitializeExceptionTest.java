package org.tinyvfs.core.exception;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TVFSInitializeExceptionTest {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSInitializeExceptionTest.class);

	@Test
	public void test1() throws Exception {
		LOGGER.info("test1");
		final String message = "Message 1";

		try {
			throw new TVFSInitializeException(message);
		} catch (TVFSInitializeException e) {
			assertEquals(message, e.getMessage());
			assertNull(e.getCause());
		}
	}

	@Test
	public void test2() throws Exception {
		LOGGER.info("test2");
		final String message = "Message 2";
		final Throwable throwable = new Throwable();

		try {
			throw new TVFSInitializeException(message, throwable);
		} catch (TVFSInitializeException e) {
			assertEquals(message, e.getMessage());
			assertTrue(throwable == e.getCause());
		}
	}
}