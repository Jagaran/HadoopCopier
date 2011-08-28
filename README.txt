This is simple utility to copy local files from your system to HDFS

Take out the src file.
Change the  CopyConstant.FS_DEFAULT_NAME in CopyConstant file to the default URI of your cluster
Build the jar using Ant

To invoke just call hadoop jar org.jd.copier.CopyClient <MR/SHELL> <src-list> <o/p dir> <Chunk Count> <ThreadPoolCount>

The Src lsit file should contain the files in uri scheme as below

file:///Users/jagarandas/Work-Assignment/Analytics/analytics-poc/sample-data/tmp/test.ksh
file:///Users/jagarandas/Work-Assignment/Analytics/analytics-poc/sample-data/tmp/sample.txt

<Chunk Count> - This determines how many files each thread would handle. 

<ThreadPoolCount> - This determines how many threads you want to keep in the pool.

DISTCP is used to copy the files.
It is a utility provided by Hadoop distribution.