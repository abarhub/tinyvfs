package org.tinyvfs.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.exception.TVFSInitializeException;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFS;
import org.tinyvfs.core.path.TVFSRootName;
import org.tinyvfs.core.spi.TVFSConfigurator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSConfig {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfig.class);

	private final Map<TVFSRootName, TVFSConfigParam> map;
	private final Map<TVFSRootName, VirtualFS> mapFS;
	private final AtomicBoolean isInit = new AtomicBoolean(false);

	public TVFSConfig() {
		map = new HashMap<>();
		mapFS = new HashMap<>();
	}

	public void add(TVFSRootName rootName, TVFSConfigParam configParam) {
		TVFSTools.checkParamNotNull(rootName, "root is Null");
		TVFSTools.checkParamNotNull(configParam, "config is Null");
		TVFSTools.checkParam(!map.containsKey(rootName), "Name '" + rootName.getName() + "' existe déjà !");
		TVFSTools.checkParam(configParam.getName().equals(rootName), "Name '" + rootName.getName() + "' invalid !");
		map.put(rootName, configParam);
		LOGGER.info("add {}", rootName);
	}

	public void add(TVFSRootName rootName, TVFSConfigParam configParam, TVFileSystem fileSystem) {
		TVFSTools.checkParamNotNull(rootName, "root is Null");
		TVFSTools.checkParamNotNull(configParam, "config is Null");
		TVFSTools.checkParamNotNull(fileSystem, "root is Null");
		TVFSTools.checkParam(!map.containsKey(rootName), "Name '" + rootName.getName() + "' existe déjà !");
		TVFSTools.checkParam(configParam.getName().equals(rootName), "Name '" + rootName.getName() + "' invalid !");
		map.put(rootName, configParam);
		mapFS.put(rootName, new VirtualFS(fileSystem, rootName, configParam.getPath()));
		LOGGER.info("add {}", rootName);
	}

	public TVFSConfigParam get(TVFSRootName rootName) {
		return map.get(rootName);
	}

	public VirtualFS getFS(TVFSRootName name, TVFileSystem fileSystem) {
		TVFSTools.checkParamNotNull(name, "Param is Null");
		TVFSTools.checkParamNotNull(fileSystem, "Param is Null");
		init();
		if (mapFS.containsKey(name)) {
			return mapFS.get(name);
		} else if (map.containsKey(name)) {
			TVFSConfigParam conf = map.get(name);
			mapFS.put(name, new VirtualFS(fileSystem, name, conf.getPath()));
			return mapFS.get(name);
		} else {
			return null;
		}
	}

	public void init() {
		if (isInit.compareAndSet(false, true)) {
			LOGGER.debug("start init");
			ServiceLoader<TVFSConfigurator> configuratorServiceLoader = ServiceLoader.load(TVFSConfigurator.class);

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
			LOGGER.debug("end init");
		}
	}

}
