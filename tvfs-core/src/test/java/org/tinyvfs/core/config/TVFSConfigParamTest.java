package org.tinyvfs.core.config;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSToolsTest;
import org.tinyvfs.core.path.TVFSRootName;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Alain on 19/02/2017.
 */
public class TVFSConfigParamTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(TVFSToolsTest.class);

	@Test
	public void testCreate() throws Exception {
		TVFSConfigParam tvfsConfigParam = new TVFSConfigParam(getName1(), getPath1(), false);
		LOGGER.info("config=" + tvfsConfigParam);
		assertEquals(getName1(), tvfsConfigParam.getName());
		assertEquals(getPath1(), tvfsConfigParam.getPath());
		assertEquals(false, tvfsConfigParam.isReadOnly());

		tvfsConfigParam = new TVFSConfigParam(getName2(), getPath2(), true);
		LOGGER.info("config=" + tvfsConfigParam);
		assertEquals(getName2(), tvfsConfigParam.getName());
		assertEquals(getPath2(), tvfsConfigParam.getPath());
		assertEquals(true, tvfsConfigParam.isReadOnly());

	}

	@Test
	public void testEqualsHashcode() throws Exception {
		TVFSConfigParam param1, param2;

		param1 = new TVFSConfigParam(getName1(), getPath1(), false);
		param2 = new TVFSConfigParam(getName1(), getPath1(), false);
		assertEquals(param1, param2);
		assertEquals(param1.hashCode(), param2.hashCode());

		param1 = new TVFSConfigParam(getName1(), getPath1(), false);
		param2 = new TVFSConfigParam(getName2(), getPath2(), true);
		assertNotEquals(param1, param2);
		assertNotEquals(param1.hashCode(), param2.hashCode());

		param1 = new TVFSConfigParam(getName1(), getPath1(), false);
		param2 = new TVFSConfigParam(getName2(), getPath1(), false);
		assertNotEquals(param1, param2);
		assertNotEquals(param1.hashCode(), param2.hashCode());

		param1 = new TVFSConfigParam(getName1(), getPath1(), false);
		param2 = new TVFSConfigParam(getName1(), getPath2(), false);
		assertNotEquals(param1, param2);
		assertNotEquals(param1.hashCode(), param2.hashCode());

		param1 = new TVFSConfigParam(getName1(), getPath1(), false);
		param2 = new TVFSConfigParam(getName1(), getPath1(), true);
		assertNotEquals(param1, param2);
		assertNotEquals(param1.hashCode(), param2.hashCode());
	}

	// tools method

	private Path getPath1() {
		return Paths.get("toto", "abc.txt");
	}

	private Path getPath2() {
		return Paths.get("toto", "tttt.txt");
	}

	private TVFSRootName getName1() {
		return new TVFSRootName("name1");
	}

	private TVFSRootName getName2() {
		return new TVFSRootName("name2");
	}
}