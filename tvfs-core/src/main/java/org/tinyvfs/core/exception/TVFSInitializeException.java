package org.tinyvfs.core.exception;

/**
 * Created by Alain on 27/05/2017.
 */
public class TVFSInitializeException extends RuntimeException {

	public TVFSInitializeException(String message) {
		super(message);
	}

	public TVFSInitializeException(String message, Throwable cause) {
		super(message, cause);
	}
}
