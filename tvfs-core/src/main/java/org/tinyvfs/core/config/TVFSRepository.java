package org.tinyvfs.core.config;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRepository {

	private static TVFSConfig instance = new TVFSConfig();

	public static TVFSConfig getInstance() {
		return instance;
	}

	public static void clearInstance() {
		instance = new TVFSConfig();
	}
}
