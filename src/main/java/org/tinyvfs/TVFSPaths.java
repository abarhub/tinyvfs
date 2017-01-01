package org.tinyvfs;

import java.nio.file.Path;

/**
 * Created by Alain on 11/12/2016.
 */
public final class TVFSPaths {

	/*public static Path get(String first, String... more) {
		//FileSystems.getFileSystem()

		FSParam fsParam = new FSParam();
		TVFileSystem tvFileSystem = new TVFileSystem(null, fsParam, null);
		return tvFileSystem.getPath(first, more);
	}

	public static Path getPath(String name, String... paths) {
		FSParam fsParam = new FSParam();
		TVFileSystem tvFileSystem = new TVFileSystem(null, fsParam, null);
		return tvFileSystem.getPath(name, paths);
	}*/

	public static Path getAbsolutePath(VirtualFS virtualFS, String... paths) {
		return virtualFS.get(paths);
	}
}
