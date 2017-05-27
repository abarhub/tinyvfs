package org.tinyvfs.core.config;

import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFS;
import org.tinyvfs.core.path.TVFSRootName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alain on 01/01/2017.
 */
public class TVFSConfig {

	private final Map<TVFSRootName, TVFSConfigParam> map;
	private final Map<TVFSRootName, VirtualFS> mapFS;

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
	}

	public void add(TVFSRootName rootName, TVFSConfigParam configParam, TVFileSystem fileSystem) {
		TVFSTools.checkParamNotNull(rootName, "root is Null");
		TVFSTools.checkParamNotNull(configParam, "config is Null");
		TVFSTools.checkParamNotNull(fileSystem, "root is Null");
		TVFSTools.checkParam(!map.containsKey(rootName), "Name '" + rootName.getName() + "' existe déjà !");
		TVFSTools.checkParam(configParam.getName().equals(rootName), "Name '" + rootName.getName() + "' invalid !");
		map.put(rootName, configParam);
		mapFS.put(rootName, new VirtualFS(fileSystem, rootName, configParam.getPath()));
	}

	public TVFSConfigParam get(TVFSRootName rootName) {
		return map.get(rootName);
	}

	public VirtualFS getFS(TVFSRootName name, TVFileSystem fileSystem) {
		TVFSTools.checkParamNotNull(name, "Param is Null");
		TVFSTools.checkParamNotNull(fileSystem, "Param is Null");
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
}
