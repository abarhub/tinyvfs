package org.tinyvfs.path;

import org.tinyvfs.TVFSTools;
import org.tinyvfs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSPath implements Path {

	private final VirtualFS virtualFS;
	private final List<String> path;

	protected TVFSPath(VirtualFS virtualFS, List<String> path) {
		TVFSTools.checkParamNotNull(virtualFS, "Param null");
		this.virtualFS = virtualFS;
		if (path == null || path.size() == 0) {
			this.path = Collections.unmodifiableList(new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<String>(path);
			this.path = Collections.unmodifiableList(liste);
		}
	}

	public Path getRealPath() {
		Path root = virtualFS.getRootPath();
		String s = "";
		for (String s2 : path) {
			if (s.length() > 0)
				s += getFileSystem().getSeparator();
			s += s2;
		}
		Path p = root.resolve(s);
		return p;
	}

	public FileSystem getFileSystem() {
		return virtualFS.getTvFileSystem();
	}

	public boolean isAbsolute() {
		return true;
	}

	public Path getRoot() {
		return new TVFSPath(virtualFS, new ArrayList<>());
	}

	public Path getFileName() {
		return null;
	}

	public Path getParent() {
		return null;
	}

	public int getNameCount() {
		return path.size();
	}

	public Path getName(int index) {
		unsupportedOperation();
		return null;
	}

	public Path subpath(int beginIndex, int endIndex) {
		unsupportedOperation();
		return null;
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

	private void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	private void checkVirtualPath(Path p) {
		TVFSTools.checkParamNotNull(p, "le Path est null");
		TVFSTools.checkParam(p instanceof TVFSPath, "le path n'est pas valide");
		TVFSTools.checkParamNotNull(p.getFileSystem(), "le FS est null");
		TVFSTools.checkParam(p.getFileSystem() == this.getFileSystem(), "le FS est invalide");
		TVFSTools.checkParam(p.getFileSystem().provider() == this.getFileSystem().provider(), "le FS est invalide");
	}

	@Override
	public String toString() {
		return "TVFSPath{" +
				"virtualFS=" + virtualFS +
				", path=" + path +
				'}';
	}
}
