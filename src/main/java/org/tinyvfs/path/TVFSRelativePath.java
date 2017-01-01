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
 * Created by Alain on 01/01/2017.
 */
public class TVFSRelativePath implements Path {

	private final List<String> liste;
	private final VirtualFS virtualFS;

	public TVFSRelativePath(VirtualFS virtualFS, List<String> liste) {
		TVFSTools.checkParamNotNull(virtualFS, "Param null");
		this.virtualFS = virtualFS;
		if (liste == null || liste.isEmpty()) {
			this.liste = Collections.unmodifiableList(new ArrayList<>());
		} else {
			this.liste = Collections.unmodifiableList(liste);
		}
	}

	@Override
	public FileSystem getFileSystem() {
		return virtualFS.getTvFileSystem();
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
	public Path getFileName() {
		if (liste.isEmpty()) {
			return null;
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(liste.get(liste.size() - 1));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public Path getParent() {
		if (liste.size() < 2) {
			return null;
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(liste.get(liste.size() - 2));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public int getNameCount() {
		return liste.size();
	}

	@Override
	public Path getName(int index) {
		if (index < 0 || index >= liste.size()) {
			throw new IllegalArgumentException("Index " + index + " invalide");
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(liste.get(index));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
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

}
