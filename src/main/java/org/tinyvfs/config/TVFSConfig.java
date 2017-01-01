package org.tinyvfs.config;

import org.tinyvfs.TVFSRoot;
import org.tinyvfs.TVFSTools;
import org.tinyvfs.TVFileSystem;
import org.tinyvfs.VirtualFS;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSConfig {

	private final Map<TVFSRoot, TVFSConfigParam> map;
	private final Map<TVFSRoot, VirtualFS> mapFS;
	private final TVFileSystem tvFileSystem;

	public TVFSConfig(TVFileSystem tvFileSystem) {
		map = new HashMap<>();
		mapFS = new HashMap<>();
		this.tvFileSystem = tvFileSystem;
	}

	public void add(TVFSConfigParam tvfsConfigParam) {
		TVFSTools.checkParamNotNull(tvfsConfigParam, "Param is Null");
		TVFSTools.checkParam(!map.containsKey(tvfsConfigParam.getName()), "Name '" + tvfsConfigParam.getName() + "' existe déjà !");
		map.put(tvfsConfigParam.getName(), tvfsConfigParam);
		mapFS.put(tvfsConfigParam.getName(), new VirtualFS(tvFileSystem, tvfsConfigParam.getName(), tvfsConfigParam.getPath()));
	}

	public VirtualFS getFS(TVFSRoot name) {
		TVFSTools.checkParamNotNull(name, "Param is Null");
		if (mapFS.containsKey(name)) {
			return mapFS.get(name);
		} else {
			return null;
		}
	}
}
