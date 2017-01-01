package org.tinyvfs;

import java.security.InvalidParameterException;

/**
 * Created by Alain on 11/12/2016.
 */
public final class TVFSTools {

	private TVFSTools() {
	}

	public static boolean isNameValide(String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isDigit(c) || (c >= 'a' && c < 'z') || (c >= 'A' && c < 'Z') || c == '.' || c == '_') {
				// character valide
			} else {
				return false;
			}
		}

		char c = name.charAt(0);
		if ((c >= 'a' && c < 'z') || (c >= 'A' && c < 'Z')) {
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
}
