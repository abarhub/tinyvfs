package org.tinyvfs.api.exception;

/**
 * Created by Alain on 28/05/2017.
 */
public class TVFSParseException extends RuntimeException {

	public TVFSParseException(String message) {
		super(message);
	}

	public TVFSParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
