package org.tinyvfs.core.path;

import org.tinyvfs.core.fs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSAbsolutePath extends TVFSAbstractPath {

	public TVFSAbsolutePath(VirtualFS virtualFS, List<String> liste) {
		super(virtualFS, liste);
	}

	@Override
	public boolean isAbsolute() {
		return true;
	}

	@Override
	public Path getRoot() {
		return new TVFSAbsolutePath(virtualFS, new ArrayList<>());
	}


	@Override
	public Path normalize() {
		List<String> list = normalizePath();
		return new TVFSAbsolutePath(virtualFS, list);
	}


	@Override
	public URI toUri() {
		unsupportedOperation();
		return null;
	}

	@Override
	public Path toAbsolutePath() {
		return this;
	}


	@Override
	public File toFile() {
		unsupportedOperation();
		return null;
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
		unsupportedOperation();
		return null;
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events) throws IOException {
		unsupportedOperation();
		return null;
	}

	protected Path getRealPath2() {
		Path root = virtualFS.getRootPath();
		StringBuilder s = new StringBuilder();
		for (String s2 : path) {
			if (s.length() > 0)
				s.append(getFileSystem().getSeparator());
			s.append(s2);
		}
		Path p = root.resolve(s.toString());
		return p;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(virtualFS.getName().getName() + ":");
		for (String s2 : path) {
			s.append("/").append(s2);
		}
		return s.toString();
	}
}
