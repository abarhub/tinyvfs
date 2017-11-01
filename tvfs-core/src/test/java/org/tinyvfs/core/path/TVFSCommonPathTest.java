package org.tinyvfs.core.path;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.tinyvfs.core.fs.TVFileSystem;

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
	protected Path rootPath;
	protected TVFileSystem tvFileSystem;

	public void init() throws IOException {

		TVFSRootName name = new TVFSRootName(NAME);

		File f = folder.newFolder();
		rootPath = f.toPath();

		tvFileSystem = mock(TVFileSystem.class);
		when(tvFileSystem.getSeparator()).thenReturn(SEPARATOR);
		when(tvFileSystem.getDefautFileSystem()).thenReturn(FileSystems.getDefault());
		when(tvFileSystem.getName()).thenReturn(name);
		when(tvFileSystem.getRootPath()).thenReturn(rootPath);
	}

	// tools method

	protected TVFSAbsolutePath getPath(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSAbsolutePath(tvFileSystem, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSAbsolutePath(tvFileSystem, liste);
		}
	}

	protected TVFSRelativePath getPathRelative(String... paths) {
		if (paths == null || paths.length == 0) {
			return new TVFSRelativePath(tvFileSystem, new ArrayList<>());
		} else {
			List<String> liste = new ArrayList<>();
			for (String p : paths) {
				liste.add(p);
			}
			return new TVFSRelativePath(tvFileSystem, liste);
		}
	}
}
