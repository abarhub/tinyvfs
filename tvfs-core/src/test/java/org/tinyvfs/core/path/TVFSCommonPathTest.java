package org.tinyvfs.core.path;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.fs.TVFileSystem;
import org.tinyvfs.core.fs.VirtualFS;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alain on 19/02/2017.
 */
public class TVFSCommonPathTest {

	protected static final String NAME = "root1";
	protected static final String SEPARATOR = "/";
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	protected VirtualFS virtualFS;
	protected Path rootPath;
	protected TVFileSystem tvFileSystem;
	protected VirtualFS virtualFSRelative;

	public void init() throws IOException {
		virtualFS = mock(VirtualFS.class);

		TVFSRootName name = new TVFSRootName(NAME);
		when(virtualFS.getName()).thenReturn(name);

		File f = folder.newFolder();
		rootPath = f.toPath();
		when(virtualFS.getRootPath()).thenReturn(rootPath);

		tvFileSystem = mock(TVFileSystem.class);
		when(tvFileSystem.getSeparator()).thenReturn(SEPARATOR);
		when(tvFileSystem.getDefautFileSystem()).thenReturn(FileSystems.getDefault());
		when(virtualFS.getTvFileSystem()).thenReturn(tvFileSystem);

		virtualFSRelative=VirtualFS.getRelativeVFS(tvFileSystem);
	}

	// tools method

	protected TVFSAbsolutePath getPath(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(virtualFS, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSAbsolutePath(virtualFS, liste);
		}
	}

	protected TVFSRelativePath getPathRelative(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSRelativePath(virtualFSRelative, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSRelativePath(virtualFSRelative, liste);
		}
	}
}
