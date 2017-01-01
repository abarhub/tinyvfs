package org.tinyvfs.path;

import org.tinyvfs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSAbsolutePath extends TVFSAbstractPath {

	public TVFSAbsolutePath(VirtualFS virtualFS, List<String> liste) {
		super(virtualFS, liste);
	}

	public boolean isAbsolute() {
		return true;
	}

	public Path getRoot() {
		return new TVFSAbsolutePath(virtualFS, new ArrayList<>());
	}

	public boolean startsWith(Path other) {
		unsupportedOperation();
		return false;
	}

	public boolean startsWith(String other) {
		unsupportedOperation();
		return false;
	}

	public boolean endsWith(Path other) {
		unsupportedOperation();
		return false;
	}

	public boolean endsWith(String other) {
		unsupportedOperation();
		return false;
	}

	public Path normalize() {
		unsupportedOperation();
		return null;
	}

	public Path resolve(Path other) {
		unsupportedOperation();
		return null;
	}

	public Path resolve(String other) {
		unsupportedOperation();
		return null;
	}

	public Path resolveSibling(Path other) {
		unsupportedOperation();
		return null;
	}

	public Path resolveSibling(String other) {
		unsupportedOperation();
		return null;
	}

	public Path relativize(Path other) {
		unsupportedOperation();
		return null;
	}

	public URI toUri() {
		unsupportedOperation();
		return null;
	}

	public Path toAbsolutePath() {
		return this;
	}

	public Path toRealPath(LinkOption... options) throws IOException {
		unsupportedOperation();
		return null;
	}

	public File toFile() {
		unsupportedOperation();
		return null;
	}

	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
		unsupportedOperation();
		return null;
	}

	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events) throws IOException {
		unsupportedOperation();
		return null;
	}

	public Iterator<Path> iterator() {
		unsupportedOperation();
		return null;
	}

	public int compareTo(Path other) {
		return 0;
	}

	public boolean equals(Object other) {
		return false;
	}

	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		String s = "";
		for (String s2 : path) {
			s += "/" + s2;
		}
		return s;
	}
}
