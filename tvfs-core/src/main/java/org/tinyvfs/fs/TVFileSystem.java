package org.tinyvfs.fs;

import org.tinyvfs.TVFSPaths;
import org.tinyvfs.TVFSTools;
import org.tinyvfs.config.TVFSConfig;
import org.tinyvfs.config.TVFSConfigParam;
import org.tinyvfs.config.TVFSRepository;
import org.tinyvfs.path.TVFSRootName;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.security.InvalidParameterException;
import java.util.Set;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFileSystem extends FileSystem {

	private final VirtualFSProvider virtualFSProvider;
	private final FileSystem defautFileSystem;
	private final TVFSConfig tvfsConfig;
	private final VirtualFS relativeFS;
	private boolean open;

	public TVFileSystem(VirtualFSProvider virtualFSProvider, TVFSConfig tvfsConfig, FileSystem defautFileSystem) {
		super();
		TVFSTools.checkParamNotNull(virtualFSProvider, "Param null");
		TVFSTools.checkParamNotNull(tvfsConfig, "Param null");
		TVFSTools.checkParamNotNull(defautFileSystem, "Param null");
		this.virtualFSProvider = virtualFSProvider;
		open = true;
		this.tvfsConfig = tvfsConfig;
		this.defautFileSystem = defautFileSystem;
		relativeFS = VirtualFS.getRelativeVFS(this);
	}

	public static void deconnect() {
		TVFSRepository.clearInstance();
		TVFSPaths.clear();
		VirtualFSProvider.clearFs();
	}

	public FileSystemProvider provider() {
		return virtualFSProvider;
	}

	public void close() throws IOException {
		TVFSTools.checkParam(isOpen(), "FS closed");
		open = false;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isReadOnly() {
		return defautFileSystem.isReadOnly();
	}

	public String getSeparator() {
		return defautFileSystem.getSeparator();
	}

	public Iterable<Path> getRootDirectories() {
		unsupportedOperation();
		return null;
	}

	public Iterable<FileStore> getFileStores() {
		unsupportedOperation();
		return null;
	}

	public Set<String> supportedFileAttributeViews() {
		return defautFileSystem.supportedFileAttributeViews();
	}

	public Path getPath(String first, String... more) {
		TVFSTools.checkParam(isOpen(), "FS closed");
		TVFSTools.checkIsNotEmpty(first, "Param null");
		//TVFSTools.checkParam(first.startsWith("$"), "Root invalide (must start with $)");
		TVFSTools.checkParam(TVFSTools.isNameValide(first), "Root invalide (name invalid)");

		TVFSRootName tvfsRoot = new TVFSRootName(first);
		VirtualFS fs = getFS(tvfsRoot);

		return fs.get(more);
	}

	private VirtualFS getFS(TVFSRootName name) {
		VirtualFS fs = tvfsConfig.getFS(name, this);
		if (fs == null) {
			throw new InvalidParameterException("Erreur");
		}
		return fs;
	}

	public FileSystem getDefautFileSystem() {
		return defautFileSystem;
	}

	private void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		unsupportedOperation();
		return null;
	}

	public UserPrincipalLookupService getUserPrincipalLookupService() {
		unsupportedOperation();
		return null;
	}

	public WatchService newWatchService() throws IOException {
		unsupportedOperation();
		return null;
	}

	public void add(TVFSConfigParam tvfsConfigParam) {
		TVFSTools.checkParam(isOpen(), "FS closed");
		//tvfsConfig.add(tvfsConfigParam);
		tvfsConfig.add(tvfsConfigParam.getName(), tvfsConfigParam);
	}

	public VirtualFS getRelativeFS() {
		return relativeFS;
	}
}
