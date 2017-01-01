package org.tinyvfs;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by Alain on 11/12/2016.
 */
public class VirtualFS {

	private final TVFileSystem tvFileSystem;
	private final TVFSRoot name;
	private final Path rootPath;

	public VirtualFS(TVFileSystem tvFileSystem, TVFSRoot name, Path rootPath) {
		TVFSTools.checkParamNotNull(tvFileSystem, "Param null");
		TVFSTools.checkParamNotNull(name, "Name invalide");
		TVFSTools.checkParamNotNull(rootPath, "Param null");
		TVFSTools.checkParam(rootPath.isAbsolute(), "Param null");
		this.tvFileSystem = tvFileSystem;
		this.name = name;
		this.rootPath = rootPath;
	}

	public Path get(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSPath(this, null);
		} else if (paths.length == 1) {
			return new TVFSPath(this, Arrays.asList(paths));
		} else {
			//Path p = Paths.get(paths);
			return new TVFSPath(this, Arrays.asList(paths));
		}
		//return null;
	}

	public TVFileSystem getTvFileSystem() {
		return tvFileSystem;
	}

	public TVFSRoot getName() {
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
