package org.tinyvfs.api.ast;

/**
 * Created by Alain on 28/05/2017.
 */
public class DirectoryConfig {

	private String name;
	private String path;
	private boolean readOnly;

	public DirectoryConfig() {
	}

	public DirectoryConfig(String name, String path, boolean readOnly) {
		this.name = name;
		this.path = path;
		this.readOnly = readOnly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DirectoryConfig)) return false;

		DirectoryConfig that = (DirectoryConfig) o;

		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "DirectoryConfig{" +
				"name='" + name + '\'' +
				", path='" + path + '\'' +
				", readOnly=" + readOnly +
				'}';
	}
}
