package org.tinyvfs.core.path;

import org.tinyvfs.core.fs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRelativePath extends TVFSAbstractPath {

	public TVFSRelativePath(VirtualFS virtualFS, List<String> liste) {
		super(virtualFS, liste);
	}

	@Override
	public boolean isAbsolute() {
		return false;
	}

	@Override
	public Path getRoot() {
		return null;
	}

	@Override
	public Path normalize() {

		List<String> list = normalizePath();
		return new TVFSRelativePath(virtualFS,list);
	}

	@Override
	public Path relativize(Path other) {
		return null;
	}

	@Override
	public URI toUri() {
		unsupportedOperation();
		return null;
	}

	@Override
	public Path toAbsolutePath() {
		unsupportedOperation();
		return null;
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		unsupportedOperation();
		return null;
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
		FileSystem fs = virtualFS.getTvFileSystem().getDefautFileSystem();
		Path p;
		if (path == null || path.isEmpty()) {
			p = fs.getPath("");
		} else {
			String path2[] = new String[path.size() - 1];
			String first;
			first = path.get(0);
			for (int i = 1; i < path.size(); i++) {
				path2[i - 1] = path.get(i);
			}
			p = fs.getPath(first, path2);
		}
		return p;
	}

	@Override
	public int compareTo(Path other) {
		return 0;
	}

	@Override
	public String toString() {
		String s = "";
		for (String s2 : path) {
			if (s.length() > 0)
				s += "/" + s2;
			else
				s += s2;
		}
		return s;
	}
}
