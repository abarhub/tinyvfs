package org.tinyvfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyvfs.config.TVFSConfig2;
import org.tinyvfs.config.TVFSRepository;
import org.tinyvfs.path.TVFSAbsolutePath;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

/**
 * Created by Alain on 11/12/2016.
 */
public class VirtualFSProvider extends FileSystemProvider {

	final static Logger LOGGER = LoggerFactory.getLogger(VirtualFSProvider.class);

	public static String SCHEME = "tvfs";

	protected FileSystem defautFileSystem;
	private TVFileSystem tvFileSystem;
	private TVFSConfig2 tvfsConfig;

	public VirtualFSProvider() {
		this(FileSystems.getDefault(), null);
	}

	public VirtualFSProvider(FileSystem defautFileSystem) {
		this(defautFileSystem, null);
	}

	public VirtualFSProvider(FileSystem defautFileSystem, TVFSConfig2 tvfsConfig) {
		super();
		this.defautFileSystem = defautFileSystem;
		if (tvfsConfig != null) {
			this.tvfsConfig = tvfsConfig;
		} else {
			this.tvfsConfig = getConfig();
		}
		LOGGER.info("VFS démarré");
	}

	public String getScheme() {
		return SCHEME;
	}

	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		TVFSTools.checkParamNotNull(uri, "uri null");
		if (tvFileSystem != null) {
			throw new FileSystemAlreadyExistsException("Le FS existe déjà");
		}
		return createFileSystem(uri);
	}

	public FileSystem getFileSystem(URI uri) {
		TVFSTools.checkParamNotNull(uri, "uri null");
		if (tvFileSystem == null) {
			throw new FileSystemNotFoundException("Le FS n'existe pas");
		}
		return tvFileSystem;
	}

	private TVFileSystem createFileSystem(URI uri) {
		TVFSTools.checkParamNotNull(uri, "uri null");
		tvFileSystem = new TVFileSystem(this, tvfsConfig, defautFileSystem);
		return tvFileSystem;
	}

	protected TVFSConfig2 getConfig() {
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
		if (root.isEmpty() || !root.startsWith("$")) {
			throw new IllegalArgumentException("Root path invalide");
		}
		String[] tab = null;
		if (liste.size() > 0) {
			//liste = liste.subList(1, liste.size() - 1);
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

		Path p2 = getRealPath(dir);
		FileSystem fs3 = getRealFileSystem(dir);
		fs3.provider().createDirectory(p2, attrs);
	}

	public void delete(Path path) throws IOException {
		checkVirtualPath(path);

		Path p2 = getRealPath(path);
		FileSystem fs3 = getRealFileSystem(path);
		fs3.provider().delete(p2);
	}

	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);

		Path p2 = getRealPath(source);
		Path p3 = getRealPath(target);
		FileSystem fs3 = getRealFileSystem(source);
		fs3.provider().copy(p2, p3, options);
	}

	public void move(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);

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

		Path p2 = getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		fs.provider().setAttribute(p2, attribute, value, options);
	}


}
