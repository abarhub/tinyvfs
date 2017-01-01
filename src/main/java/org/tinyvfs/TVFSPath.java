package org.tinyvfs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alain on 11/12/2016.
 */
public class TVFSPath implements Path {

	private final VirtualFS virtualFS;
	private final List<String> path;

	protected TVFSPath(VirtualFS virtualFS,List<String> path){
		TVFSTools.checkParamNotNull(virtualFS,"Param null");
		this.virtualFS=virtualFS;
		if(path==null||path.size()==0){
			this.path= Collections.unmodifiableList(new ArrayList<>());
		} else {
			List<String> liste=new ArrayList<String>(path);
			this.path = Collections.unmodifiableList(liste);
		}
	}

	public Path getRealPath(){
		Path root=virtualFS.getRootPath();
		String s="";
		for(String s2:path){
			if(s.length()>0)
				s+=getFileSystem().getSeparator();
			s+=s2;
		}
		Path p=root.resolve(s);
		return p;
	}

	public FileSystem getFileSystem() {
		return virtualFS.getTvFileSystem();
	}

	public boolean isAbsolute() {
		return false;
	}

	public Path getRoot() {
		return null;
	}

	public Path getFileName() {
		return null;
	}

	public Path getParent() {
		return null;
	}

	public int getNameCount() {
		return 0;
	}

	public Path getName(int index) {
		return null;
	}

	public Path subpath(int beginIndex, int endIndex) {
		return null;
	}

	public boolean startsWith(Path other) {
		return false;
	}

	public boolean startsWith(String other) {
		return false;
	}

	public boolean endsWith(Path other) {
		return false;
	}

	public boolean endsWith(String other) {
		return false;
	}

	public Path normalize() {
		return null;
	}

	public Path resolve(Path other) {
		return null;
	}

	public Path resolve(String other) {
		return null;
	}

	public Path resolveSibling(Path other) {
		return null;
	}

	public Path resolveSibling(String other) {
		return null;
	}

	public Path relativize(Path other) {
		return null;
	}

	public URI toUri() {
		return null;
	}

	public Path toAbsolutePath() {
		return null;
	}

	public Path toRealPath(LinkOption... options) throws IOException {
		return null;
	}

	public File toFile() {
		return null;
	}

	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
		return null;
	}

	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events) throws IOException {
		return null;
	}

	public Iterator<Path> iterator() {
		return null;
	}

	public int compareTo(Path other) {
		return 0;
	}

	public boolean equals(Object other) {
		return false;
	}

	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "TVFSPath{" +
				"virtualFS=" + virtualFS +
				", path=" + path +
				'}';
	}
}
