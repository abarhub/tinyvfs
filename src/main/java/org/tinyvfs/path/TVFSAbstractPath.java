package org.tinyvfs.path;

import org.tinyvfs.TVFSTools;
import org.tinyvfs.VirtualFS;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alain on 01/01/2017.
 */
public abstract class TVFSAbstractPath implements Path {

	protected final List<String> path;
	protected final VirtualFS virtualFS;

	public TVFSAbstractPath(VirtualFS virtualFS, List<String> liste) {
		TVFSTools.checkParamNotNull(virtualFS, "Param null");
		this.virtualFS = virtualFS;
		if (liste == null || liste.isEmpty()) {
			this.path = Collections.unmodifiableList(new ArrayList<>());
		} else {
			this.path = Collections.unmodifiableList(liste);
		}
	}

	@Override
	public FileSystem getFileSystem() {
		return virtualFS.getTvFileSystem();
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

	@Override
	public Path getFileName() {
		if (path.isEmpty()) {
			return null;
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(path.get(path.size() - 1));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public Path getParent() {
		if (path.size() < 2) {
			return null;
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(path.get(path.size() - 2));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public int getNameCount() {
		return path.size();
	}

	@Override
	public Path getName(int index) {
		if (index < 0 || index >= path.size()) {
			throw new IllegalArgumentException("Index " + index + " invalide");
		} else {
			List<String> liste2 = new ArrayList<>();
			liste2.add(path.get(index));
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		if (beginIndex < 0 || beginIndex >= path.size()) {
			throw new IllegalArgumentException("Index " + beginIndex + " invalide");
		} else if (endIndex < 0 || endIndex >= path.size()) {
			throw new IllegalArgumentException("Index " + endIndex + " invalide");
		} else if (endIndex < beginIndex) {
			throw new IllegalArgumentException("Index " + endIndex + " < " + beginIndex + " invalide");
		} else {
			List<String> liste2 = new ArrayList<>();
			for (int i = beginIndex; i <= endIndex; i++) {
				liste2.add(path.get(i));
			}
			return new TVFSRelativePath(virtualFS, liste2);
		}
	}

	protected void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	protected void checkVirtualPath(Path p) {
		TVFSTools.checkParamNotNull(p, "le Path est null");
		TVFSTools.checkParam(p instanceof TVFSAbsolutePath, "le path n'est pas valide");
		TVFSTools.checkParamNotNull(p.getFileSystem(), "le FS est null");
		TVFSTools.checkParam(p.getFileSystem() == this.getFileSystem(), "le FS est invalide");
		TVFSTools.checkParam(p.getFileSystem().provider() == this.getFileSystem().provider(), "le FS est invalide");
	}

}
