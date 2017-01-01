package org.tinyvfs;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRoot {

	private final String name;

	public TVFSRoot(String name) {
		TVFSTools.checkIsNotEmpty(name, "Param is Null");
		TVFSTools.checkParam(TVFSTools.isNameValide(name), "Name is invalid");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TVFSRoot tvfsRoot = (TVFSRoot) o;

		return name != null ? name.equals(tvfsRoot.name) : tvfsRoot.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
