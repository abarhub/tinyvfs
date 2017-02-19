package org.tinyvfs;

import org.tinyvfs.path.TVFSRelativePath;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alain on 11/12/2016.
 */
public final class TVFSPaths {

	private static Logger LOGGER = Logger.getLogger(TVFSPaths.class.getName());

	private static TVFileSystem fs;

	/*public static Path getAbsolutePath(VirtualFS virtualFS, String... paths) {
		return virtualFS.get(paths);
	}*/

	public static Path getAbsolutePath(String name, String... paths) {
		TVFileSystem fs2;
		try {
			fs2 = getTvFileSystem();
		} catch (IOException | URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "File System '" + VirtualFSProvider.SCHEME + "' not found", e);
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		if (fs2 == null) {
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		return fs2.getPath(name, paths);
	}

	public static Path getRelativePath(String... paths) {
		TVFileSystem fs2;
		try {
			fs2 = getTvFileSystem();
		} catch (IOException | URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "File System '" + VirtualFSProvider.SCHEME + "' not found", e);
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		if (fs2 == null) {
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		List<String> liste = new ArrayList<>();
		if (paths != null) {
			for (String p : paths) {
				liste.add(p);
			}
		}
		TVFSRelativePath relative = new TVFSRelativePath(fs2.getRelativeFS(), liste);
		return relative;
	}

	private static TVFileSystem getTvFileSystem() throws URISyntaxException, IOException {
		if (fs == null) {
			URI uri;
			uri = URI.create(VirtualFSProvider.SCHEME + "://localhost/");
			FileSystem fs2 = FileSystems.newFileSystem(uri, null);
			if (fs2 == null) {
				throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
			}
			if (fs2 instanceof TVFileSystem) {
				fs = (TVFileSystem) fs2;
			} else {
				throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' invalid");
			}
		}
		return fs;
	}

	protected static void clear(){
		fs=null;
	}
}
