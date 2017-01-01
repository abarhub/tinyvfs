package org.tinyvfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alain on 11/12/2016.
 */
public class VirtualFSProvider extends FileSystemProvider {

	final static Logger LOGGER = LoggerFactory.getLogger(VirtualFSProvider.class);

	public static String SCHEME="tvfs";

	private TVFileSystem tvFileSystem;

	public VirtualFSProvider() {
		super();
	}

	public String getScheme() {
		return SCHEME;
	}

	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		TVFSTools.checkParamNotNull(uri,"uri null");
		TVFSTools.checkParam(tvFileSystem==null,"le FS existe déjà");
		return createFileSystem(uri);
	}

	public FileSystem getFileSystem(URI uri) {
		TVFSTools.checkParamNotNull(uri,"uri null");
		return tvFileSystem;
	}

	private TVFileSystem createFileSystem(URI uri){
		TVFSTools.checkParamNotNull(uri,"uri null");
		tvFileSystem=new TVFileSystem(this,null,FileSystems.getDefault());
		return tvFileSystem;
	}

	public Path getPath(URI uri) {
		unsupportedOperation();
		return null;
	}

	private void unsupportedOperation() {
		TVFSTools.unsupportedOperation();
	}

	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>[] attrs) throws IOException {
		checkVirtualPath(path);
		TVFSTools.checkParam(path instanceof TVFSPath,"le path n'est pas valide");
		TVFSTools.checkParamNotNull(path.getFileSystem(),"le FS est null");
		Path p2=getRealPath(path);
		FileSystem fs3 = getRealFileSystem(path);
		return fs3.provider().newByteChannel(p2,options,attrs);
	}

	private void checkVirtualPath(Path p){
		TVFSTools.checkParamNotNull(p,"le Path est null");
		TVFSTools.checkParam(p instanceof TVFSPath,"le path n'est pas valide");
		TVFSTools.checkParamNotNull(p.getFileSystem(),"le FS est null");
	}

	private Path getRealPath(Path path){
		checkVirtualPath(path);
		TVFSPath p2= (TVFSPath) path;
		Path p3=p2.getRealPath();
		LOGGER.info(path+ "->"+p3);
		return p3;
	}

	private TVFileSystem getFileSystem(Path path){
		checkVirtualPath(path);
		FileSystem fs = path.getFileSystem();
		TVFileSystem fs2= (TVFileSystem) fs;
		return fs2;
	}

	private FileSystem getRealFileSystem(Path path){
		checkVirtualPath(path);
		FileSystem fs = path.getFileSystem();
		TVFileSystem fs2= (TVFileSystem) fs;
		return fs2.getDefautFileSystem();
	}

	public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
		checkVirtualPath(dir);
		Path p2=getRealPath(dir);
		FileSystem fs3 = getRealFileSystem(dir);
		return fs3.provider().newDirectoryStream(p2,filter);
	}

	public void createDirectory(Path dir, FileAttribute<?>[] attrs) throws IOException {
		checkVirtualPath(dir);

		Path p2=getRealPath(dir);
		FileSystem fs3 = getRealFileSystem(dir);
		fs3.provider().createDirectory(p2,attrs);
	}

	public void delete(Path path) throws IOException {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
		FileSystem fs3 = getRealFileSystem(path);
		fs3.provider().delete(p2);
	}

	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);

		Path p2=getRealPath(source);
		Path p3=getRealPath(target);
		FileSystem fs3 = getRealFileSystem(source);
		fs3.provider().copy(p2,p3,options);
	}

	public void move(Path source, Path target, CopyOption... options) throws IOException {
		checkVirtualPath(source);
		checkVirtualPath(target);

		Path p2=getRealPath(source);
		Path p3=getRealPath(target);
		FileSystem fs3 = getRealFileSystem(source);
		fs3.provider().move(p2,p3,options);
	}

	public boolean isSameFile(Path path, Path path2) throws IOException {
		checkVirtualPath(path);
		checkVirtualPath(path2);

		Path p2=getRealPath(path);
		Path p3=getRealPath(path2);
		FileSystem fs3 = getRealFileSystem(path);
		return fs3.provider().isSameFile(p2,p3);
	}

	public boolean isHidden(Path path) throws IOException {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
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
		TVFSTools.checkParam(path instanceof TVFSPath,"le path n'est pas valide");
		TVFSTools.checkParamNotNull(path.getFileSystem(),"le FS est null");

		Path p2=getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		fs.provider().checkAccess(p2,modes);
	}

	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().getFileAttributeView(p2,type,options);
	}

	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().readAttributes(p2,type,options);
	}

	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		return fs.provider().readAttributes(p2,attributes,options);
	}

	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		checkVirtualPath(path);

		Path p2=getRealPath(path);
		FileSystem fs = getRealFileSystem(path);
		fs.provider().setAttribute(p2,attribute,value,options);
	}


}
