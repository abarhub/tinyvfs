package org.tinyvfs.core.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSPaths;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFileSystem extends FileSystem {

	public final static Logger LOGGER = LoggerFactory.getLogger(TVFSConfig.class);

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
		List<Path> list=new ArrayList<>();
		List<TVFSRootName> list2=tvfsConfig.getRootsName();
		if(list2!=null){
			for(TVFSRootName root:list2){
				list.add(TVFSPaths.getAbsolutePath(root.getName()));
			}
		}
		return list;
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		//unsupportedOperation();
		List<FileStore> list=new ArrayList<>();
		List<TVFSRootName> list2=tvfsConfig.getRootsName();
		if(list2!=null){
			for(TVFSRootName root:list2){
				try {
					list.add(provider().getFileStore(TVFSPaths.getAbsolutePath(root.getName())));
				}catch(IOException e){
					LOGGER.debug("Error : {}",e.getMessage(),e);
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
		TVFSTools.checkIsNotEmpty(first, "Param null");
		//TVFSTools.checkParam(first.startsWith("$"), "Root invalide (must start with $)");
		TVFSTools.checkParam(TVFSTools.isNameValide(first), "Root invalide (name invalid)");

		TVFSRootName tvfsRoot = new TVFSRootName(first);
		VirtualFS fs = getFS(tvfsRoot);

		return fs.get(more);
	}

	private VirtualFS getFS(TVFSRootName name) {
		TVFSTools.checkParamNotNull(name, "Param is Null");
		VirtualFS fs = tvfsConfig.getFS(name, this);
		if (fs == null) {
			throw new InvalidParameterException("Erreur: no FS for '" + name.getName() + "'");
		}
		return fs;
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
			Path p=virtualFSProvider.getRealPath(x);
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

	public void add(TVFSConfigParam tvfsConfigParam) {
		TVFSTools.checkParam(isOpen(), "FS closed");
		//tvfsConfig.add(tvfsConfigParam);
		tvfsConfig.add(tvfsConfigParam.getName(), tvfsConfigParam);
	}

	public VirtualFS getRelativeFS() {
		return relativeFS;
	}
}
