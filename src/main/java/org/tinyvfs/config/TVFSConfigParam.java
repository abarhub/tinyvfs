package org.tinyvfs.config;

import org.tinyvfs.TVFSTools;
import org.tinyvfs.path.TVFSRootName;

import java.nio.file.Path;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSConfigParam {

	private final TVFSRootName name;
	private final Path path;
	private final boolean readOnly;

	public TVFSConfigParam(TVFSRootName name, Path path, boolean readOnly) {
		TVFSTools.checkParamNotNull(name, "Name is null");
		TVFSTools.checkParam(TVFSTools.isNameValide(name.getName()), "Name is invalid");
		TVFSTools.checkParamNotNull(path, "Path is null");
		this.name = name;
		this.path = path;
		this.readOnly = readOnly;
	}

	public TVFSRootName getName() {
		return name;
	}

	public Path getPath() {
		return path;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TVFSConfigParam that = (TVFSConfigParam) o;

		if (readOnly != that.readOnly) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return path != null ? path.equals(that.path) : that.path == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (path != null ? path.hashCode() : 0);
		result = 31 * result + (readOnly ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "TVFSConfigParam{" +
				"name=" + name +
				", path=" + path +
				", readOnly=" + readOnly +
				'}';
	}
}
