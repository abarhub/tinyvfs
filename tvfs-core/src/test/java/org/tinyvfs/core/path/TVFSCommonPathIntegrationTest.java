package org.tinyvfs.core.path;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.TVFSTestTools;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFSProvider;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TVFSCommonPathIntegrationTest {

	protected static final String NAME = "root1";
	protected static final String SEPARATOR = "/";
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	protected Path rootPath;
	protected TVFileSystem tvFileSystem;
	protected VirtualFSProvider virtualFSProvider;

	public void setUp() throws Exception {
		TVFSRootName name = new TVFSRootName(NAME);

		File f = folder.newFolder();
		rootPath = f.toPath();

		TVFSConfigParam configParam = new TVFSConfigParam(name, rootPath, false);

		TVFSRepository.getInstance().add(name, configParam);


		virtualFSProvider = new VirtualFSProvider();

		tvFileSystem = (TVFileSystem) virtualFSProvider.getFileSystem(URI.create(VirtualFSProvider.SCHEME + "://localhost"));
	}


	@Before
	public void init() throws NoSuchFieldException, IllegalAccessException {
		TVFSTestTools.reinitConfig();
	}

	@After
	public void tearDown() throws Exception {
		TVFSTestTools.reinitConfig();
	}

	protected TVFSAbsolutePath getAbsoluePath(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(tvFileSystem, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSAbsolutePath(tvFileSystem, liste);
		}
	}

}
