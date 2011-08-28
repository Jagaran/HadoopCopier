
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



package org.jd.copier;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jd.copier.mapred.DistCp;
import org.jd.copier.shell.HDPCopy;

public class CopyClient {

	public static void main(String args[]) throws URISyntaxException{
		if(args.length < 0){
			throw new IllegalArgumentException("Please Provide Some Argument To Go Ahead");
		}else{
			if(args[0].equals("MR")){
				runMRCopy(args);
			}else if(args[0].equals("SHELL")){
				runNormalCopy(args);
			}else{
				throw new IllegalArgumentException("Please Provide Copy Mode: MR or NORMAL");
			}
		}
	}
	
	/** Map Reduce Copy Using  Distcp capabilities as Exposed by Hadoop API tools*/
	
	
	
	public static void runMRCopy(String args[]) throws URISyntaxException{
		if(!args[1].equals(null)){
			File fileListCopy = new File(new URI(args[1]).getPath());
			if(!fileListCopy.isFile()){
				throw new IllegalArgumentException("Please Provide File Path For the File List To Copy");
			}else{
				try {
						DistCp distcp = new DistCp(new Configuration());
						String values [] = new String[3];
						values[0]="-f";
						values[1]=args[1];
						values[2]=args[2];
						distcp.copyViaMR(values);
				} catch (IOException e) {
					e.printStackTrace();
				}catch(URISyntaxException urlExec){
					urlExec.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** Normal copy using the shell copy command */
	
	
	public static void runNormalCopy(String args[]){
		if(!args[1].equals(null)){
			try {
				File fileListCopy = new File(new URI(args[1]).getPath());
				if(!fileListCopy.isFile()){
					throw new IllegalArgumentException("Please Provide File Path For the File List To Copy");
				}else{
					FileStatus status;
					status = FileSystem.newInstance(new Configuration()).getFileStatus(new Path(args[2]));
					if(!status.isDir()){
						throw new IllegalArgumentException("Please Provide a Valid HDFS Directory to Copy");	
					}else{
						if(args[3].equals(null)){
							throw new IllegalArgumentException("Please Provide the Chunk Count");
						}else{
							if(args[4].equals(null)){
								throw new IllegalArgumentException("Please Provide Number Of  Threads");
							}else{
								String fileListPath = fileListCopy.getAbsolutePath();
								Path pathOutDir = new Path(args[2]);
								int chunkCount = Integer.parseInt(args[3]);
								int threadCount = Integer.parseInt(args[4]);
								new HDPCopy().copyToHdfs(fileListPath,pathOutDir,chunkCount,threadCount);

							}
						}
					}
				}

			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
