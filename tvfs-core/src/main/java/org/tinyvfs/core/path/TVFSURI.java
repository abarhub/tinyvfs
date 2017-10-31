package org.tinyvfs.core.path;

import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.exception.TVFSInvalideURIException;

import java.net.URI;
import java.security.InvalidParameterException;

import static org.tinyvfs.core.fs.VirtualFSProvider.SCHEME;

public class TVFSURI {

	private String name;
	private String path;

	public TVFSURI(URI uri) throws TVFSInvalideURIException {
		parse(uri);
	}

	private void parse(URI uri) throws TVFSInvalideURIException {
		try {
			TVFSTools.checkParamNotNull(uri, "uri null");
			TVFSTools.checkParam(uri.getScheme().equals(SCHEME), "uri scheme invalide : " + uri.getScheme());
			String s = uri.toString();
			TVFSTools.checkParam(s.startsWith(SCHEME + ":"), "URI scheme invalide");
			s = s.substring((SCHEME + ":").length());
			int pos = s.indexOf(":");
			TVFSTools.checkParam(pos > 0, "URI invalide");
			String name = s.substring(0, pos);
			TVFSTools.checkParam(TVFSTools.isNameValide(name), "URI name invalide");
			this.name = name;
			String path = "";
			if (s.length() > pos) {
				path = s.substring(pos + 1);
			}
			this.path = path;
		} catch (InvalidParameterException e) {
			throw new TVFSInvalideURIException("uri invalide", e);
		}
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
}
