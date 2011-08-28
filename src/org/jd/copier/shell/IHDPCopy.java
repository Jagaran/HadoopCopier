
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

import org.apache.hadoop.fs.Path;

public interface IHDPCopy {
	
	/** Copy to Hadoop */
	
	public void copyToHdfs(String fileListPath, Path outPutPath, int chuckCount, int threadPoolCount);
}
