package org.tinyvfs.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFSProvider;
import org.tinyvfs.core.path.TVFSAbstractPath;
import org.tinyvfs.core.path.TVFSRelativePath;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Alain on 11/12/2016.
 */
public final class TVFSPaths {

	private final static Logger LOGGER = LoggerFactory.getLogger(TVFSPaths.class);

	private static TVFileSystem fs;

	public static Path getAbsolutePath(String name, String... paths) {
		TVFileSystem fs2;
		try {
			fs2 = getTvFileSystem(name);
		} catch (IOException | URISyntaxException e) {
			LOGGER.error("File System '" + VirtualFSProvider.SCHEME + "' not found", e);
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		if (fs2 == null) {
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		String first = null;
		String[] more = null;
		if (paths == null || paths.length == 0) {
			first = null;
			more = null;
		} else if (paths.length == 1) {
			first = paths[0];
			more = null;
		} else {
			first = paths[0];
			more = new String[paths.length - 1];
			System.arraycopy(paths, 1, more, 0, more.length);
		}
		return fs2.getPath(first, more);
	}

	public static Path getRelativePath(String... paths) {
		TVFileSystem fs2;
		//try {
		VirtualFSProvider virtualFSProvider = getFileSystemProvider();
		fs2 = virtualFSProvider.getDefaultFileSystem();
		//fs2 = getTvFileSystem();
//		} catch (IOException | URISyntaxException e) {
//			LOGGER.error("File System '" + VirtualFSProvider.SCHEME + "' not found", e);
//			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
//		}
		if (fs2 == null) {
			throw new FileSystemNotFoundException("File System '" + VirtualFSProvider.SCHEME + "' not found");
		}
		List<String> liste = new ArrayList<>();
		if (paths != null) {
			for (String p : paths) {
				liste.add(p);
			}
		}
		TVFSRelativePath relative = new TVFSRelativePath(fs2, liste);
		return relative;
	}

	public static VirtualFSProvider getFileSystemProvider() {
		VirtualFSProvider virtualFSProvider = null;
		List<FileSystemProvider> list = FileSystemProvider.installedProviders();
		if (list != null) {
			Optional<VirtualFSProvider> optFs = list.stream()
					.filter(x -> x instanceof VirtualFSProvider)
					.map(x -> (VirtualFSProvider) x)
					.findAny();
			if (optFs.isPresent()) {
				virtualFSProvider = optFs.get();
			}
		}
		return virtualFSProvider;
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

	private static TVFileSystem getTvFileSystem(String name) throws URISyntaxException, IOException {
		return (TVFileSystem) FileSystems.getFileSystem(TVFSTools.createURI(name));
	}

	public static void clear() {
		fs = null;
	}

	public boolean isTVFSPath(Path p) {
		return p != null && p instanceof TVFSAbstractPath;
	}

	public String getRootName(TVFSAbstractPath p) {
		if (p == null) {
			throw new NullPointerException("param null");
		}
		if (!p.isAbsolute()) {
			throw new IllegalArgumentException("Param is not absolute");
		}
		return ((TVFileSystem) p.getFileSystem()).getName().getName();
	}
}
