package pcd.ass01.jpf;

import pcd.ass01.common.SourceAnalysisLib;

/**
 * 
 * This agent analyses documents which are meant to be sources,
 * loading them and counting the LoC. 
 * 
 * It works as a consumer in a producers/consumers architecture,   
 * getting the path of the document from a bounded buffer shared with the consumers.
 * 
 * @author aricci
 *
 */
public class SrcAnalyserAgent extends BasicAgent {

	private BoundedBuffer<File> srcFiles;
	private SourceLocMap sourceMap;
	private Latch analyzerLatch;
	private Flag stopFlag;
	
	public SrcAnalyserAgent(String id, BoundedBuffer<File> srcFiles, SourceLocMap sourceMap, Latch analyzerLatch, Flag stopFlag) throws Exception  {
		super("doc-loader-" + id);
		this.srcFiles = srcFiles;
		this.sourceMap = sourceMap;
        this.analyzerLatch = analyzerLatch;
        this.stopFlag = stopFlag;
	}
	
	public void run() {
		int nJobs = 0;
		boolean noMoreSrcs = false;
		while (!noMoreSrcs && !stopFlag.isSet()) {
			try {
				File src = srcFiles.get();
				nJobs++;
				sourceMap.add(src, src.getNLoC());
			} catch (ClosedException ex) {
				noMoreSrcs = true;
			}
		}
		analyzerLatch.countDown();
	}	
}
