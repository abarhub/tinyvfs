package org.tinyvfs.config;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRepository {

	private static TVFSConfig2 instance = new TVFSConfig2();

	public static TVFSConfig2 getInstance() {
		return instance;
	}

	public static void clearInstance() {
		instance = new TVFSConfig2();
	}
}
