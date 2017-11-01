package org.tinyvfs.core.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSPaths;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.path.TVFSAbsolutePath;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFileSystem extends FileSystem {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfig.class);

	private final VirtualFSProvider virtualFSProvider;
	private final FileSystem defautFileSystem;
	private final TVFSConfigParam configParam;
	private boolean open;

	public TVFileSystem(VirtualFSProvider virtualFSProvider, TVFSConfigParam configParam) {
		super();
		TVFSTools.checkParamNotNull(virtualFSProvider, "Param virtualFSProvider null");
		TVFSTools.checkParamNotNull(configParam, "Param configParam null");
		this.virtualFSProvider = virtualFSProvider;
		open = true;
		this.defautFileSystem = configParam.getPath().getFileSystem();
		this.configParam = configParam;
	}

	public static void deconnect() {
		TVFSRepository.clearInstance();
		TVFSPaths.clear();
		VirtualFSProvider.clearFs();
	}

	@Override
	public FileSystemProvider provider() {
		return virtualFSProvider;
	}

	@Override
	public void close() throws IOException {
		TVFSTools.checkParam(isOpen(), "FS closed");
		open = false;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public boolean isReadOnly() {
		return defautFileSystem.isReadOnly();
	}

	@Override
	public String getSeparator() {
		return defautFileSystem.getSeparator();
	}

	@Override
	public Iterable<Path> getRootDirectories() {
		//unsupportedOperation();
		List<Path> list = new ArrayList<>();
		List<TVFSRootName> list2 = virtualFSProvider.getConfig().getRootsName();
		if (list2 != null) {
			for (TVFSRootName root : list2) {
				list.add(TVFSPaths.getAbsolutePath(root.getName()));
			}
		}
		return list;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		//unsupportedOperation();
		List<FileStore> list = new ArrayList<>();
		List<TVFSRootName> list2 = virtualFSProvider.getConfig().getRootsName();
		if (list2 != null) {
			for (TVFSRootName root : list2) {
				try {
					list.add(provider().getFileStore(TVFSPaths.getAbsolutePath(root.getName())));
				} catch (IOException e) {
					LOGGER.debug("Error : {}", e.getMessage(), e);
				}
			}
		}
		return list;
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return defautFileSystem.supportedFileAttributeViews();
	}

	@Override
	public Path getPath(String first, String... more) {
		TVFSTools.checkParam(isOpen(), "FS closed");
		//TVFSTools.checkIsNotEmpty(first, "Param null");
		//TVFSTools.checkParam(first.startsWith("$"), "Root invalide (must start with $)");
		//TVFSTools.checkParam(TVFSTools.isNameValide(first), "Root invalide (name invalid)");

		String[] paths;

		if ((first == null || first.length() == 0) && (more == null || more.length == 0)) {
			paths = null;
		} else if (more == null || more.length == 0) {
			paths = new String[]{first};
		} else {
			paths = new String[more.length + 1];
			paths[0] = first;
			System.arraycopy(more, 0, paths, 1, more.length);
		}

		return get(paths);
	}

	public Path get(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(this, null);
		} else if (paths.length == 1) {
			return new TVFSAbsolutePath(this, Arrays.asList(paths));
		} else {
			return new TVFSAbsolutePath(this, Arrays.asList(paths));
		}
	}

	public FileSystem getDefautFileSystem() {
		return defautFileSystem;
	}

	private void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		//unsupportedOperation();
		return (x) -> {
			Path p = virtualFSProvider.getRealPath(x);
			return defautFileSystem.getPathMatcher(syntaxAndPattern).matches(p);
		};
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		//unsupportedOperation();
		return defautFileSystem.getUserPrincipalLookupService();
	}

	@Override
	public WatchService newWatchService() throws IOException {
		unsupportedOperation();
		return null;
	}

	public TVFSRootName getName() {
		return configParam.getName();
	}

	public Path getRootPath() {
		return configParam.getPath();
	}
}
