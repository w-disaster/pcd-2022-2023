package pcd.ass01.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	
	public SrcAnalyserAgent(String id, BoundedBuffer<File> srcFiles, SourceLocMap sourceMap, Latch analyzerLatch) throws Exception  {
		super("doc-loader-" + id);
		this.srcFiles = srcFiles;
		this.sourceMap = sourceMap;
        this.analyzerLatch = analyzerLatch;
	}
	
	public void run() {
		log("started");
		int nJobs = 0;
		boolean noMoreSrcs = false;
		while (!noMoreSrcs) {
			try {
				File src = srcFiles.get();
				nJobs++;
				logDebug("got a src to load: " + src.getName() + " - job: " + nJobs);
				try {
					FileReader fr = new FileReader(src);
					BufferedReader br = new BufferedReader(fr);

					// long nLines = SourceAnalysisLib.countLoC_SimpleStrategy(br);					
					long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(br);
					
					fr.close();
					
					sourceMap.add(src.getCanonicalPath(), (int) nLines);
								
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				/*
				try {
					loadDoc(doc);
					logDebug("job " + nJobs + " done.");
				} catch (Exception ex) {
					log("error in processing the " + nJobs + " doc.");
				}*/
			} catch (ClosedException ex) {
				log("no more srcs.");
				noMoreSrcs = true;
			}
		}
		analyzerLatch.countDown();
		log("done.");
	}
}
