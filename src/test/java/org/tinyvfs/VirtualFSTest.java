package org.tinyvfs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Alain on 01/01/2017.
 */
public class VirtualFSTest {

	final static Logger LOGGER = LoggerFactory.getLogger(Test1.class);

	@Rule
	public TemporaryFolder folder= new TemporaryFolder();

	@Before
	public void init(){

	}

	@Test
	public void testGet() throws Exception {

		LOGGER.info("test testGet");

		TVFileSystem tvFileSystem=mock(TVFileSystem.class);

		TVFSRoot name=new TVFSRoot("test1");

		Path rootPath= folder.newFolder().toPath();

		VirtualFS virtualFS=new VirtualFS(tvFileSystem,name,rootPath);

		Path p=virtualFS.get("a");

		assertTrue(p instanceof TVFSPath);

		TVFSPath p2= (TVFSPath) p;

		LOGGER.info("ref:"+rootPath.resolve("a"));
		LOGGER.info("realPath:"+p2.getRealPath());

		assertEquals(rootPath.resolve("a"),p2.getRealPath());
	}

}