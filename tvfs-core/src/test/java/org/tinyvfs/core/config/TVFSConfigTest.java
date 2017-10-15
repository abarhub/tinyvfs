package org.tinyvfs.core.config;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TVFSConfigTest {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfigTest.class);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private TVFSConfig tvfsConfig;

	private int nbInit;

	private Path path;

	@Before
	public void init() throws IOException {
		LOGGER.info("init");
		tvfsConfig = new TVFSConfig() {
			@Override
			public synchronized void init() {
				LOGGER.info("init appele");
				nbInit++;
			}
		};

		path = folder.newFolder().toPath();
	}

	@Test
	public void testAddOK() throws Exception {
		LOGGER.info("testAddOK");
		final TVFSRootName rootName = createTVFSRootName("nom1");
		final TVFSConfigParam configParam = createTVFSConfigParam(rootName);

		// methode testée
		tvfsConfig.add(rootName, configParam);

		// vérifications
		assertTrue(tvfsConfig.contains(rootName));
		TVFSConfigParam configParam2 = tvfsConfig.get(rootName);
		assertNotNull(configParam2);
		assertTrue(configParam == configParam2);
	}

	// methodes utilitaires


	private TVFSRootName createTVFSRootName(String name) {
		return new TVFSRootName(name);
	}

	private TVFSConfigParam createTVFSConfigParam(TVFSRootName rootName) {
		return new TVFSConfigParam(rootName, path, false);
	}

}