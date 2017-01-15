package org.tinyvfs.path;

import org.tinyvfs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
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
	public boolean startsWith(Path other) {
		return false;
	}

	@Override
	public boolean startsWith(String other) {
		return false;
	}

	@Override
	public boolean endsWith(Path other) {
		return false;
	}

	@Override
	public boolean endsWith(String other) {
		return false;
	}

	@Override
	public Path normalize() {
		return null;
	}

	@Override
	public Path resolve(Path other) {
		return null;
	}

	@Override
	public Path resolve(String other) {
		return null;
	}

	@Override
	public Path resolveSibling(Path other) {
		return null;
	}

	@Override
	public Path resolveSibling(String other) {
		return null;
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

	@Override
	public Iterator<Path> iterator() {
		unsupportedOperation();
		return null;
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
