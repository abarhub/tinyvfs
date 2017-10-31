package org.tinyvfs.core.path;

import org.tinyvfs.core.fs.TVFileSystem;

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

	public TVFSAbsolutePath(TVFileSystem fileSystem, List<String> liste) {
		super(fileSystem, liste);
	}

	@Override
	public boolean isAbsolute() {
		return true;
	}

	@Override
	public Path getRoot() {
		return new TVFSAbsolutePath(fileSystem, new ArrayList<>());
	}


	@Override
	public Path normalize() {
		List<String> list = normalizePath();
		return new TVFSAbsolutePath(fileSystem, list);
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
		Path root = fileSystem.getRootPath();
		StringBuilder s = new StringBuilder();
		for (String s2 : path) {
			if (s.length() > 0)
				s.append(getFileSystem().getSeparator());
			s.append(s2);
		}
		return root.resolve(s.toString());
	}

	@Override
	public Path getParent() {
		if (path.size() < 2) {
			return getRoot();
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(path.get(path.size() - 2));
			return new TVFSAbsolutePath(fileSystem, liste2);
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(fileSystem.getName().getName() + ":");
		for (String s2 : path) {
			s.append("/").append(s2);
		}
		return s.toString();
	}
}
