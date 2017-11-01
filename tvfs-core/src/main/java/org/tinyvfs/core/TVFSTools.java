package org.tinyvfs.core;

import java.net.URI;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.tinyvfs.core.fs.VirtualFSProvider.SCHEME;

/**
 * Created by Alain on 11/12/2016.
 */
public final class TVFSTools {

	private TVFSTools() {
	}

	// root name valide ?
	public static boolean isNameValide(String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isDigit(c) || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.' || c == '_') {
				// character valide
			} else {
				return false;
			}
		}

		char c = name.charAt(0);
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
			// premier caractère valide
		} else {
			return false;
		}

		return true;
	}

	public static void checkParam(boolean b, String message) {
		if (message == null || message.length() == 0) {
			throw new InvalidParameterException("Error");
		}
		if (!b) {
			throw new InvalidParameterException(message);
		}
	}

	public static void checkParamNotNull(Object o, String message) {
		if (message == null || message.length() == 0) {
			throw new InvalidParameterException("Error");
		}
		if (o == null) {
			throw new InvalidParameterException(message);
		}
	}

	public static void checkIsNotEmpty(String s, String message) {
		if (message == null || message.length() == 0) {
			throw new InvalidParameterException("Error");
		}
		if (s == null || s.length() == 0) {
			throw new InvalidParameterException(message);
		}
	}

	public static void unsupportedOperation() {
		throw new UnsupportedOperationException();
	}

	public static <T> List<T> toList(T... values) {
		if (values == null || values.length == 0) {
			return new ArrayList<T>();
		} else {
			List<T> list = new ArrayList<T>();
			for (T v : values) {
				list.add(v);
			}
			return list;
		}
	}

	public static String[] toArray(List<String> list) {
		if (list == null) {
			return new String[0];
		} else {
			return list.toArray(new String[0]);
		}
	}

	public static String[] toArray(Path p) {
		if (p == null) {
			return new String[0];
		} else {
			String tab[] = new String[p.getNameCount()];
			for (int i = 0; i < p.getNameCount(); i++) {
				tab[i] = p.getName(i).toString();
			}
			return tab;
		}
	}

	public static URI createURI(String name, String path) {
		if (path == null || path.length() == 0) {
			return URI.create(SCHEME + ":" + name + ":");
		} else {
			return URI.create(SCHEME + ":" + name + ":" + path);
		}
	}
}
