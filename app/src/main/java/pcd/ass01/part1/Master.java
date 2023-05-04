package pcd.ass01.part1;

import java.io.File;

/**
 * Master agent setting up the work to do.
 * 
 * @author aricci
 *
 */
public class Master extends BasicAgent {

	private File startDir;
	private SourceLocMap map;
	private long nSources;
		
	private int nSrcAnalyzerAgents;
	private int srcFilesBufferSize;

	public Master(int maxSourcesToTrack, int nBands, int maxLinesOfCode, File startDir, int nWorkers, int srcFilesBufferSize) {
		super("master");
		this.startDir = startDir;
		map = new SourceLocMap(maxSourcesToTrack, nBands, maxLinesOfCode);		
		nSrcAnalyzerAgents = nWorkers;
		this.srcFilesBufferSize = srcFilesBufferSize;
	}
	
	public void run() {
		log("Started - number of source analysers adopted: " + nSrcAnalyzerAgents);
		try {			
			
			long t0 = System.currentTimeMillis();
			
			/* spawn discoverer */
			
			BoundedBuffer<File> srcFiles = new BoundedBuffer<File>(srcFilesBufferSize);
			SrcDiscovererAgent docDiscoverer = new SrcDiscovererAgent(startDir, srcFiles, new String[] {".java", ".c"});
			docDiscoverer.start();

			/* spawn src analyzers */
			
			Latch allSrcAnalyzed = new Latch(nSrcAnalyzerAgents);
			for (int i = 0; i < nSrcAnalyzerAgents; i++) {
				new SrcAnalyserAgent("" + i, srcFiles, map, allSrcAnalyzed).start();
			}

			/* wait for loaders to complete */
			allSrcAnalyzed.await();
			log("analyzers done.");
			
			log("Number of sources analyzed: " + nSources);
			map.dumpSrcsWithMoreNLocs();
			map.dumpDistribution();
			
			
			long t1 = System.currentTimeMillis();
			log("Done in " + (t1 - t0));
						

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
		
}
