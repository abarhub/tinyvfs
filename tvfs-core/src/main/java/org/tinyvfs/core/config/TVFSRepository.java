package org.tinyvfs.core.config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRepository {

	private static TVFSConfig instance = new TVFSConfig();
	private static AtomicBoolean isInit = new AtomicBoolean(false);

	public static TVFSConfig getInstance() {
		if (isInit.compareAndSet(false, true)) {
			instance.init();
		}
		return instance;
	}

	public static void clearInstance() {
		instance = new TVFSConfig();
		isInit.set(false);
	}

}
