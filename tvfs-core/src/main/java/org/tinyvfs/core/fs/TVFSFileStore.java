package org.tinyvfs.core.fs;

import org.tinyvfs.core.TVFSTools;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

/**
 * Created by tulip_000 on 24/07/2017.
 */
public class TVFSFileStore extends FileStore {

	final FileStore fileStore;
	final boolean readOnly;

	public TVFSFileStore(FileStore fileStore, boolean readOnly) {
		TVFSTools.checkParamNotNull(fileStore, "Param null");
		this.fileStore = fileStore;
		this.readOnly = readOnly;
	}

	@Override
	public String name() {
		return fileStore.name();
	}

	@Override
	public String type() {
		return fileStore.type();
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public long getTotalSpace() throws IOException {
		return fileStore.getTotalSpace();
	}

	@Override
	public long getUsableSpace() throws IOException {
		return fileStore.getUsableSpace();
	}

	@Override
	public long getUnallocatedSpace() throws IOException {
		return fileStore.getUnallocatedSpace();
	}

	@Override
	public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
		return fileStore.supportsFileAttributeView(type);
	}

	@Override
	public boolean supportsFileAttributeView(String name) {
		return fileStore.supportsFileAttributeView(name);
	}

	@Override
	public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
		return fileStore.getFileStoreAttributeView(type);
	}

	@Override
	public Object getAttribute(String attribute) throws IOException {
		return fileStore.getAttribute(attribute);
	}
}
