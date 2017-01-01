package org.tinyvfs.config;

import org.tinyvfs.TVFSRoot;
import org.tinyvfs.TVFSTools;

import java.nio.file.Path;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSConfigParam {

	private final TVFSRoot name;
	private final Path path;
	private final boolean readOnly;

	public TVFSConfigParam(TVFSRoot name, Path path, boolean readOnly) {
		TVFSTools.checkParamNotNull(name, "Name is null");
		TVFSTools.checkParam(TVFSTools.isNameValide(name.getName()), "Name is invalid");
		TVFSTools.checkParamNotNull(path, "Path is null");
		this.name = name;
		this.path = path;
		this.readOnly = readOnly;
	}

	public TVFSRoot getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
}
