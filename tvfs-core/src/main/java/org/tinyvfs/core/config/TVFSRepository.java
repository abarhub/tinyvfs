package org.tinyvfs.core.config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSRepository {

	private static TVFSConfig instance = new TVFSConfig();
	private static AtomicBoolean isInit = new AtomicBoolean(false);

	public static TVFSConfig getInstance() {
		if (!isInit.get()) {
			synchronized (TVFSRepository.class) {
				instance.init();
				isInit.set(true);
			}
		}
		return instance;
	}

	public static void clearInstance() {
		instance = new TVFSConfig();
	}

}
