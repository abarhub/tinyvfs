package org.tinyvfs.api.ast;

import java.util.List;

/**
 * Created by Alain on 28/05/2017.
 */
public class GlobalConfig {

	private final List<DirectoryConfig> directoryConfigs;

	public GlobalConfig(List<DirectoryConfig> directoryConfigs) {
		this.directoryConfigs = directoryConfigs;
	}

	public List<DirectoryConfig> getDirectoryConfigs() {
		return directoryConfigs;
	}

	@Override
	public String toString() {
		return "GlobalConfig{" +
				"directoryConfigs=" + directoryConfigs +
				'}';
	}
}
