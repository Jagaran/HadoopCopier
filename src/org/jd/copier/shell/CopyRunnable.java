
/*
 * A simple Copy Mechanism to Hadoop
 * Author: Jagaran Das
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.jd.copier.shell;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class CopyRunnable implements Runnable {
	  /** Check List of the files for the worker thread to work on. */
	private  AtomicReference<List<Path>> subList = new AtomicReference<List<Path>>();
	
	  /** The out directory for copying the files */
	
	private Path outDirectory;
	
	 /** The Configuration for HDFS*/
	private static Configuration configuration;
	
	 /** The FileSystem API class of HDFS*/
	
	
	private static FileSystem fileSystem;
	
	public void setSubFileList(List<Path> subFileList) {
		subList.set(subFileList);
	}

	 /** Constructor to set the output directory and the configuration*/
	
	
	public CopyRunnable(Path outDirectory){
		this.setOutDirectory(outDirectory);
		configuration = new Configuration();
		configuration.set("fs.default.name", CopyConstant.FS_DEFAULT_NAME);
	}

	/** Run Method copy the files*/
	
	public void run() {
		try {
			setFileSystem(FileSystem.newInstance(configuration));
			Path [] p =  new Path[subList.get().size()];
			Path [] path = subList.get().toArray(p);
			getFileSystem().copyFromLocalFile(false, true, path, getOutDirectory());

		}	 catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * @param outDirectory the outDirectory to set
	 */
	public void setOutDirectory(Path outDirectory) {
		this.outDirectory = outDirectory;
	}

	/**
	 * @return the outDirectory
	 */
	public Path getOutDirectory() {
		return outDirectory;
	}

	/**
	 * @param fileSystem to set
	 */
	public static void setFileSystem(FileSystem fileSystem) {
		CopyRunnable.fileSystem = fileSystem;
	}

	/**
	 * @return the fileSystem
	 */
	public static FileSystem getFileSystem() {
		return fileSystem;
	}

}
