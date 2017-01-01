package org.tinyvfs.config;

import org.tinyvfs.TVFSTools;
import org.tinyvfs.path.TVFSRootName;

import java.nio.file.Path;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSConfigParam {

	private final TVFSRootName name;
	private final Path path;
	private final boolean readOnly;

	public TVFSConfigParam(TVFSRootName name, Path path, boolean readOnly) {
		TVFSTools.checkParamNotNull(name, "Name is null");
		TVFSTools.checkParam(TVFSTools.isNameValide(name.getName()), "Name is invalid");
		TVFSTools.checkParamNotNull(path, "Path is null");
		this.name = name;
		this.path = path;
		this.readOnly = readOnly;
	}

	public TVFSRootName getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
}
