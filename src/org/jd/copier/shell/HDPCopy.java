

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.fs.Path;
import org.jd.copier.util.PathUtils;

public class HDPCopy implements IHDPCopy {

	/**
	 * This method is used as Copy to HDFS 
	 */
	
	public void copyToHdfs(String fileListPath, Path outPutPath, int chunkCount, int threadPoolCount) {
		List<List<Path>>  subLists = getFileWorkerLists(chunkCount,fileListPath);
		CopyRunnable copyRunbleObj;
		ExecutorService executor = Executors.newFixedThreadPool(threadPoolCount);
	
		try {

			for (int threadRunCount=0; threadRunCount < subLists.size(); threadRunCount++){
				copyRunbleObj = new CopyRunnable(outPutPath);
				copyRunbleObj.setSubFileList(subLists.get(threadRunCount));
				executor.execute(copyRunbleObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
	
	/**
	 * This method is used to give the file list to all the worker threads to work upon 
	 */
	
	public List<List<Path>> getFileWorkerLists(int splitListCount,String srcFilePath){
		List<List<Path>> subLists = null;
		try{
			File fileList = new File(srcFilePath);
			BufferedReader br = new BufferedReader(new FileReader(fileList));
			String line;
			List<Path >allPaths = new ArrayList<Path>();
			while ((line = br.readLine()) != null) {
				URI uri = new URI(line);
				allPaths.add(new Path(uri.getPath()));
				uri = null;
			}
			br.close();
			subLists = PathUtils.split(allPaths,splitListCount);
		}catch(Exception exec){
			exec.printStackTrace();
		}
		return subLists;
	}
	/**
	 * The main method to test this class independently
	 */
	public static void main(String args[]){
		String path = "/Users/jagarandas/Work-Assignment/Analytics/analytics-poc/sample-data/srclist";
		Path pathOutDir = new Path("/home/hadoop/data");
		int chunkCount = 1;
		int threadPoolCount = 5;
		new HDPCopy().copyToHdfs(path,pathOutDir,chunkCount,threadPoolCount);
	}
}
