package org.tinyvfs.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.exception.TVFSInitializeException;
import org.tinyvfs.core.path.TVFSRootName;
import org.tinyvfs.core.spi.TVFSConfigurator;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSConfig {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfig.class);

	private final Map<TVFSRootName, TVFSConfigParam> map;
	private final AtomicBoolean isInit = new AtomicBoolean(false);

	public TVFSConfig() {
		map = new HashMap<>();
		//mapFS = new HashMap<>();
	}

	public synchronized void add(TVFSRootName rootName, TVFSConfigParam configParam) {
		TVFSTools.checkParamNotNull(rootName, "root is Null");
		TVFSTools.checkParamNotNull(configParam, "config is Null");
		TVFSTools.checkParam(!map.containsKey(rootName), "Name '" + rootName.getName() + "' existe déjà !");
		TVFSTools.checkParam(configParam.getName().equals(rootName), "Name '" + rootName.getName() + "' invalid !");
		//TVFSTools.checkParam(!mapFS.containsKey(rootName), "Name '" + rootName.getName() + "' existe déjà !");
		map.put(rootName, configParam);
		LOGGER.debug("add {}", rootName);
	}

	public TVFSConfigParam get(TVFSRootName rootName) {
		return map.get(rootName);
	}

	public synchronized void init() {
		if (isInit.compareAndSet(false, true)) {
			LOGGER.debug("start init");
			ServiceLoader<TVFSConfigurator> configuratorServiceLoader = ServiceLoader.load(TVFSConfigurator.class);

			if (configuratorServiceLoader != null) {
				Iterator<TVFSConfigurator> iter = configuratorServiceLoader.iterator();
				while (iter.hasNext()) {
					TVFSConfigurator tvfsConfigurator = iter.next();
					try {
						tvfsConfigurator.configure(this);
					} catch (Exception e) {
						LOGGER.error("Error : {}", e.getMessage(), e);
						throw new TVFSInitializeException("Error in TVFS initialisation : " + e.getMessage(), e);
					}
				}
			}
			LOGGER.debug("end init");
		}
	}

	public boolean contains(TVFSRootName rootName) {
		return map.containsKey(rootName);
	}

	public List<TVFSRootName> getRootsName() {
		Set<TVFSRootName> set = map.keySet();
		List<TVFSRootName> list = new ArrayList<>();
		if (set != null) {
			list.addAll(set);
		}
		return list;
	}

}
