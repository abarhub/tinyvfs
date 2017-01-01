package org.tinyvfs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.config.TVFSConfigParam;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alain on 11/12/2016.
 */
public class Test1 {

	final static Logger LOGGER = LoggerFactory.getLogger(Test1.class);

	@Rule
	public TemporaryFolder folder= new TemporaryFolder();

	@Test
	public void test1() throws IOException {
		TVFileSystem tvFileSystem;

		LOGGER.info("Test 1");

		tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());

		tvFileSystem.add(new TVFSConfigParam(new TVFSRoot("test1"), newTemp(),false));

		Path p=tvFileSystem.getPath("$test1","toto.txt");

		LOGGER.info("p="+p);
	}

	@Test
	public void test2() throws IOException {
		TVFileSystem tvFileSystem;
		VirtualFSProvider virtualFSProvider;

		LOGGER.info("Test 2");

		virtualFSProvider=new VirtualFSProvider();

		//tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());
		tvFileSystem= (TVFileSystem) virtualFSProvider.newFileSystem(URI.create("vfs://test"),null);

		tvFileSystem.add(new TVFSConfigParam(new TVFSRoot("test1"), newTemp(),false));

		Path p=tvFileSystem.getPath("$test1","toto2.txt");

		LOGGER.info("p="+p);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());
		assertTrue(p.getFileSystem().provider()==virtualFSProvider);

		//Files.createFile(p);
		Files.createFile(p);

		Set<OpenOption> set=new HashSet<>();
		//set.add(StandardOpenOption.READ);
		set.add(StandardOpenOption.CREATE);
		set.add(StandardOpenOption.WRITE);
		SeekableByteChannel s=virtualFSProvider.newByteChannel(p, set,new FileAttribute[0]);
		assertNotNull(s);
	}

	@Test
	public void test3() throws IOException {
		TVFileSystem tvFileSystem;
		VirtualFSProvider virtualFSProvider;

		LOGGER.info("Test 2");

		virtualFSProvider=new VirtualFSProvider();

		//tvFileSystem=new TVFileSystem(null,null, FileSystems.getDefault());
		tvFileSystem= (TVFileSystem) virtualFSProvider.newFileSystem(URI.create("vfs://test"),null);

		tvFileSystem.add(new TVFSConfigParam(new TVFSRoot("test1"), newTemp(),false));

		Path p=tvFileSystem.getPath("$test1","toto2.txt");

		LOGGER.info("p="+p);


		Path p2=tvFileSystem.getPath("$test1","toto3.txt");

		LOGGER.info("p2="+p2);

		assertNotNull(p);
		assertNotNull(p.getFileSystem());
		assertNotNull(p.getFileSystem().provider());
		assertTrue(p.getFileSystem().provider()==virtualFSProvider);

		//Files.createFile(p);
		Files.createFile(p);

		String buf0="test 5";

		LOGGER.info("write="+buf0);

		Files.write(p,buf0.getBytes("UTF-8"),StandardOpenOption.APPEND);

		Files.copy(p,p2);

		byte[] buf=Files.readAllBytes(p2);

		String s=new String(buf,"UTF-8");

		LOGGER.info("read="+s);

	}

	private Path newTemp() throws IOException {
		File f=folder.newFolder();
		return f.toPath();
	}
}
