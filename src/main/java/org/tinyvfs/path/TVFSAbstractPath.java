package org.tinyvfs.path;

import org.tinyvfs.TVFSTools;
import org.tinyvfs.VirtualFS;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Alain on 01/01/2017.
 */
public abstract class TVFSAbstractPath implements Path {

	protected final List<String> path;
	protected final VirtualFS virtualFS;

	public TVFSAbstractPath(VirtualFS virtualFS, List<String> liste) {
		TVFSTools.checkParamNotNull(virtualFS, "Param null");
		this.virtualFS = virtualFS;
		this.path = Collections.unmodifiableList(splitPath(liste));
	}

	private List<String> splitPath(List<String> list) {
		List<String> res = new ArrayList<>();

		if (list != null && !list.isEmpty()) {

			for (String s : list) {
				if (s == null) {
					throw new NullPointerException("Path null");
				} else if (s.isEmpty()) {
					// do nothing
				} else {
					if (s.contains("/")) {
						String[] tab = s.split("/");
						if (tab != null) {
							for (String s2 : tab) {
								if (s2 != null && !s2.isEmpty()) {
									res.add(s2);
								}
							}
						}
					} else if (s.contains("\\")) {
						String[] tab = s.split("\\\\");
						if (tab != null) {
							for (String s2 : tab) {
								if (s2 != null && !s2.isEmpty()) {
									res.add(s2);
								}
							}
						}
					} else {
						res.add(s);
					}
				}
			}
		}

		return res;
	}

	@Override
	public FileSystem getFileSystem() {
		return virtualFS.getTvFileSystem();
	}

	public Path getRealPath() {
		return getRealPath2();
	}

	protected Path getRealPath2() {
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

	public int compareTo(Path other) {
		if (other == null)
			throw new NullPointerException("other must not be null");
		if (other.getClass() != getClass())
			throw new ClassCastException();

		TVFSAbsolutePath p = (TVFSAbsolutePath) other;

		if (!p.virtualFS.getName().equals(this.virtualFS.getName()))
			throw new IllegalArgumentException("Path with other root");

		Optional<String> p1 = path.stream().reduce((x, y) -> x + y);
		Optional<String> p2 = p.path.stream().reduce((x, y) -> x + y);
		String s1, s2;

		if (p1.isPresent())
			s1 = p1.get();
		else
			s1 = "";
		if (p2.isPresent())
			s2 = p2.get();
		else
			s2 = "";

		return s1.compareTo(s2);
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof TVFSAbsolutePath))
			return false;

		TVFSAbsolutePath p = (TVFSAbsolutePath) other;

		if (!p.virtualFS.getName().equals(this.virtualFS.getName()))
			return false;

		return getRealPath2().equals(p.getRealPath2());
	}

	public int hashCode() {
		return getRealPath2().hashCode();
	}

	@Override
	public Path resolve(Path other) {
		if (other == null) {
			throw new IllegalArgumentException("param must not be null");
		}
		if (!(other instanceof TVFSAbstractPath)) {
			throw new IllegalArgumentException("param is invalid");
		}

		if (other instanceof TVFSAbsolutePath) {
			return other;
		} else {
			List<String> path = new ArrayList<>();
			path.addAll(this.path);
			path.addAll(((TVFSAbstractPath) other).path);
			if (isAbsolute()) {
				return new TVFSAbsolutePath(virtualFS, path);
			} else {
				return new TVFSRelativePath(virtualFS, path);
			}
		}
	}

	@Override
	public Path resolve(String other) {
		if (other == null) {
			throw new IllegalArgumentException("param must not be null");
		}
		if (other.isEmpty()) {
			return this;
		}

		List<String> path = new ArrayList<>();
		path.addAll(this.path);
		path.add(other);
		if (isAbsolute()) {
			return new TVFSAbsolutePath(virtualFS, path);
		} else {
			return new TVFSRelativePath(virtualFS, path);
		}
	}

}
