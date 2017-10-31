package org.tinyvfs.core.fs;

import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.path.TVFSAbsolutePath;
import org.tinyvfs.core.path.TVFSRootName;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by Alain on 11/12/2016.
 */
public class VirtualFS {

	private final TVFileSystem tvFileSystem;
	private final TVFSRootName name;
	private final Path rootPath;

	private VirtualFS(TVFileSystem tvFileSystem, TVFSRootName name, Path rootPath) {
		TVFSTools.checkParamNotNull(tvFileSystem, "Param null");
		TVFSTools.checkParamNotNull(name, "Name invalide");
		TVFSTools.checkParamNotNull(rootPath, "Param null");
		TVFSTools.checkParam(rootPath.isAbsolute(), "path is not absolute : " + rootPath);

		this.tvFileSystem = tvFileSystem;
		this.name = name;
		this.rootPath = rootPath;
	}

	private VirtualFS(TVFileSystem tvFileSystem) {
		TVFSTools.checkParamNotNull(tvFileSystem, "Param null");
		this.tvFileSystem = tvFileSystem;
		name = null;
		rootPath = null;
	}

	static public VirtualFS getRelativeVFS(TVFileSystem tvFileSystem) {
		return new VirtualFS(tvFileSystem);
	}

	public Path get(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(this, null);
		} else if (paths.length == 1) {
			return new TVFSAbsolutePath(this, Arrays.asList(paths));
		} else {
			return new TVFSAbsolutePath(this, Arrays.asList(paths));
		}
	}

	public TVFileSystem getTvFileSystem() {
		return tvFileSystem;
	}

	public TVFSRootName getName() {
		return name;
	}

	public Path getRootPath() {
		return rootPath;
	}

	@Override
	public String toString() {
		return "VirtualFS{" +
				"tvFileSystem=" + tvFileSystem +
				", name='" + name + '\'' +
				", rootPath=" + rootPath +
				'}';
	}
}
