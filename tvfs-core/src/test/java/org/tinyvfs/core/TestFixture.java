package org.tinyvfs.core;

import org.tinyvfs.core.fs.VirtualFSProvider;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TestFixture {


	public static URI createUri(String name) {
		return TVFSTools.createURI(name, null);
	}

	public static URI createUri(String name, String path) {
		return TVFSTools.createURI(name, path);
	}

	public static FileSystem createFileSystem(VirtualFSProvider virtualFSProvider, String name, String path,
	                                          Map<String, String> env) throws IOException {
		return createFileSystem(virtualFSProvider, createUri(name, path), env);
	}

	public static FileSystem createFileSystem(VirtualFSProvider virtualFSProvider,
	                                          URI uri, Map<String, String> env) throws IOException {
		return virtualFSProvider.newFileSystem(uri, env);
	}

	public static Map<String, String> createEnv(Path rootPath) {
		Map<String, String> map = new HashMap<>();
		map.put(VirtualFSProvider.ROOT_PATH, rootPath.toString());
		map.put(VirtualFSProvider.READ_ONLY, "false");
		return map;
	}
}
