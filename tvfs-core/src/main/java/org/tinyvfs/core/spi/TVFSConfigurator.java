package org.tinyvfs.core.spi;

import org.tinyvfs.core.config.TVFSConfig;

/**
 * Created by Alain on 27/05/2017.
 */
public abstract class TVFSConfigurator {

	public abstract void configure(TVFSConfig tvfsConfig);
}
