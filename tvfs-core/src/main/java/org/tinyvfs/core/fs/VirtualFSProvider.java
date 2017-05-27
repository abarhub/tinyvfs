package org.tinyvfs.core.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.core.TVFSTools;
import org.tinyvfs.core.config.TVFSConfig;
import org.tinyvfs.core.config.TVFSConfigParam;
import org.tinyvfs.core.config.TVFSRepository;
import org.tinyvfs.core.path.TVFSAbsolutePath;
import org.tinyvfs.core.path.TVFSAbstractPath;
import org.tinyvfs.core.path.TVFSRootName;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by Alain on 11/12/2016.
 */
public class VirtualFSProvider extends FileSystemProvider {

	private final static Logger LOGGER = LoggerFactory.getLogger(VirtualFSProvider.class);

	public static String SCHEME = "tvfs";

	protected FileSystem defautFileSystem;
	private TVFileSystem tvFileSystem;
	private TVFSConfig tvfsConfig;

	public VirtualFSProvider() {
		this(FileSystems.getDefault(), null);
	}

	public VirtualFSProvider(FileSystem defautFileSystem) {
		this(defautFileSystem, null);
	}

	public VirtualFSProvider(FileSystem defautFileSystem, TVFSConfig tvfsConfig) {
		super();
		this.defautFileSystem = defautFileSystem;
		if (tvfsConfig != null) {
			this.tvfsConfig = tvfsConfig;
		} else {
			this.tvfsConfig = getConfig();
		}
		LOGGER.info("VFS démarré");
	}

	public static void clearFs() {
		for (FileSystemProvider fs : installedProviders()) {
			if (fs instanceof VirtualFSProvider) {
				VirtualFSProvider fs2 = (VirtualFSProvider) fs;
				fs2.clear();
			}
		}
	}

	private void clear() {
		LOGGER.info("VFS clear");
		tvFileSystem = null;
	}

	public String getScheme() {
		return SCHEME;
	}

	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		checkUri(uri);
		if (tvFileSystem != null) {
			throw new FileSystemAlreadyExistsException("Le FS existe déjà");
		}
		return createFileSystem(uri);
	}

	public FileSystem getFileSystem(URI uri) {
		checkUri(uri);
		if (tvFileSystem == null) {
			throw new FileSystemNotFoundException("Le FS n'existe pas");
		}
		return tvFileSystem;
	}

	private TVFileSystem createFileSystem(URI uri) {
		checkUri(uri);
		LOGGER.info("new VFS");
		tvFileSystem = new TVFileSystem(this, tvfsConfig, defautFileSystem);
		return tvFileSystem;
	}

	private void checkUri(URI uri) {
		TVFSTools.checkParamNotNull(uri, "uri null");
		TVFSTools.checkParam(uri.getScheme().equals(SCHEME), "uri scheme invalide : " + uri.getScheme());
	}

	protected TVFSConfig getConfig() {
		return TVFSRepository.getInstance();
	}

	public Path getPath(URI uri) {
		TVFSTools.checkParamNotNull(uri, "uri null");
		if (tvFileSystem == null) {
			LOGGER.info("creation du FS");
			createFileSystem(uri);
		}
		LOGGER.info("uri=" + uri);
		if (uri.getScheme() == null || !uri.getScheme().equals(SCHEME)) {
			throw new IllegalArgumentException("scheme invalide");
		}
		String path = uri.getPath();
		if (path == null || path.length() == 0) {
			throw new IllegalArgumentException("path empty");
		}
		LOGGER.info("path=" + path);
		char separator = '/';
		List<String> liste = new ArrayList<>();
		StringBuilder buf = new StringBuilder();
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			if (c == separator) {
				liste.add(buf.toString());
				buf.setLength(0);
				while (i + 1 < path.length() && path.charAt(i + 1) == separator) {
					i++;
				}
			} else {
				buf.append(c);
			}
		}
		if (buf.length() > 0) {
			liste.add(buf.toString());
		}
		LOGGER.info("liste=" + liste);
		/*if (liste.isEmpty()) {
			throw new IllegalArgumentException("no Root path");
		}*/
		String root = uri.getAuthority();
		LOGGER.info("root=" + root);
		if (root.isEmpty() || !TVFSTools.isNameValide(root)) {
			throw new IllegalArgumentException("Root path invalide");
		}
		String[] tab = null;
		if (liste.size() > 0) {
			//liste = liste.subList(1, liste.size() - 1);
			liste.add(0, "" + separator);
			tab = liste.toArray(new String[liste.size()]);
			LOGGER.info("tab=" + Arrays.toString(tab));
		}
		return tvFileSystem.getPath(root, tab);
	}

	private void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>[] attrs) throws IOException {
		checkVirtualPath(path);
		TVFSTools.checkParam(path instanceof TVFSAbsolutePath, "le path n'est pas valide");
		TVFSTools.checkParamNotNull(path.getFileSystem(), "le FS est null");
		//checkPathReadOnly ??? TODO: tester si c'est une création
		Path p = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().newByteChannel(p, options, attrs);
	}

	private void checkVirtualPath(Path p) {
		TVFSTools.checkParamNotNull(p, "le Path est null");
		TVFSTools.checkParam(p instanceof TVFSAbsolutePath, "le path n'est pas valide");
		TVFSTools.checkParamNotNull(p.getFileSystem(), "le FS est null");
		TVFSTools.checkParam(p.getFileSystem().provider() == this, "le FS est invalide");
	}

	private Path getRealPath(Path path) {
		checkVirtualPath(path);
		TVFSAbsolutePath p2 = (TVFSAbsolutePath) path;
		Path p3 = p2.getRealPath();
		LOGGER.info(path + "->" + p3);
		return p3;
	}

	private TVFileSystem getFileSystem(Path path) {
		checkVirtualPath(path);
		FileSystem fs = path.getFileSystem();
		TVFileSystem fs2 = (TVFileSystem) fs;
		return fs2;
	}

	private FileSystem getRealFileSystem(Path path) {
		checkVirtualPath(path);
		FileSystem fs = path.getFileSystem();
		TVFileSystem fs2 = (TVFileSystem) fs;
		return fs2.getDefautFileSystem();
	}

	public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
		checkVirtualPath(dir);
		Path p2 = getRealPath(dir);
		FileSystem fs3 = getRealFileSystem(dir);
		return fs3.provider().newDirectoryStream(p2, filter);
	}

	public void createDirectory(Path dir, FileAttribute<?>[] attrs) throws IOException {
		checkVirtualPath(dir);
		checkPathReadOnly(dir);

		Path p2 = getRealPath(dir);
		FileSystem fs3 = getRealFileSystem(dir);
		fs3.provider().createDirectory(p2, attrs);
	}

	public void delete(Path path) throws IOException {
		checkVirtualPath(path);
		checkPathReadOnly(path);

		Path p2 = getRealPath(path);
		FileSystem fs3 = getRealFileSystem(path);
		fs3.provider().delete(p2);
	}

	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);
		checkPathReadOnly(target);

		Path p2 = getRealPath(source);
		Path p3 = getRealPath(target);
		FileSystem fs3 = getRealFileSystem(source);
		fs3.provider().copy(p2, p3, options);
	}

	public void move(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);
		checkPathReadOnly(source);
		checkPathReadOnly(target);

		Path p2 = getRealPath(source);
		Path p3 = getRealPath(target);
		FileSystem fs3 = getRealFileSystem(source);
		fs3.provider().move(p2, p3, options);
	}

	public boolean isSameFile(Path path, Path path2) throws IOException {
		checkVirtualPath(path);
		checkVirtualPath(path2);

		Path p2 = getRealPath(path);
		Path p3 = getRealPath(path2);
		FileSystem fs3 = getRealFileSystem(path);
		return fs3.provider().isSameFile(p2, p3);
	}

	public boolean isHidden(Path path) throws IOException {
		checkVirtualPath(path);

		Path p2 = getRealPath(path);
		FileSystem fs3 = getRealFileSystem(path);
		return fs3.provider().isHidden(p2);
	}

	public FileStore getFileStore(Path path) throws IOException {
		unsupportedOperation();
		checkVirtualPath(path);
		return null;
	}

	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		checkVirtualPath(path);
		TVFSTools.checkParam(path instanceof TVFSAbsolutePath, "le path n'est pas valide");
		TVFSTools.checkParamNotNull(path.getFileSystem(), "le FS est null");

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		fs.provider().checkAccess(p2, modes);
	}

	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		checkVirtualPath(path);

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().getFileAttributeView(p2, type, options);
	}

	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		checkVirtualPath(path);

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().readAttributes(p2, type, options);
	}

	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		checkVirtualPath(path);

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().readAttributes(p2, attributes, options);
	}

	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		checkVirtualPath(path);
		checkPathReadOnly(path);

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		fs.provider().setAttribute(p2, attribute, value, options);
	}

	public Path readSymbolicLink(Path link) throws IOException {
		checkVirtualPath(link);

		Path p2 = getRealPath(link);
		FileSystem fs = getRealFileSystem(link);
		return fs.provider().readSymbolicLink(p2);
	}

	public void createLink(Path link, Path existing) throws IOException {
		checkVirtualPath(link);
		checkVirtualPath(existing);
		checkPathReadOnly(link);

		Path p2 = getRealPath(link);
		Path p3 = getRealPath(existing);
		FileSystem fs = getRealFileSystem(link);
		fs.provider().createLink(p2, p3);
	}

	public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs)
			throws IOException {
		checkVirtualPath(link);
		checkVirtualPath(target);
		checkPathReadOnly(link);

		Path p2 = getRealPath(link);
		Path p3 = getRealPath(target);
		FileSystem fs = getRealFileSystem(link);
		fs.provider().createSymbolicLink(p2, p3, attrs);
	}

	public AsynchronousFileChannel newAsynchronousFileChannel(Path path,
	                                                          Set<? extends OpenOption> options,
	                                                          ExecutorService executor,
	                                                          FileAttribute<?>... attrs)
			throws IOException {
		checkVirtualPath(path);
		// checkPathReadOnly ??? TODO: tester si c'est une création

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().newAsynchronousFileChannel(p2, options, executor, attrs);
	}

	public FileChannel newFileChannel(Path path,
	                                  Set<? extends OpenOption> options,
	                                  FileAttribute<?>... attrs)
			throws IOException {
		checkVirtualPath(path);
		// checkPathReadOnly TODO: tester si c'est une création

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().newFileChannel(p2, options, attrs);
	}

	private boolean isPathReadOnly(Path path) {
		checkVirtualPath(path);
		TVFSAbstractPath p = (TVFSAbstractPath) path;
		TVFSRootName name = p.getVirtualFS().getName();
		TVFSConfigParam conf = tvfsConfig.get(name);
		TVFSTools.checkParamNotNull(conf, "conf null");
		return conf.isReadOnly();
	}

	private void checkPathReadOnly(Path path) {
		checkVirtualPath(path);
		if (isPathReadOnly(path)) {
			throw new ReadOnlyFileSystemException();
		}
	}
}
