package pcd.ass01.part2;

import java.io.File;

public class Master extends BasicAgent {

	private File startDir;
	private SourceLocMap map;
	private long nSources;
	private View view;
	private Flag stopFlag;
	
	/* performance tuning params */
	
	private int nSrcAnalyzerAgents;
	private static final int srcFilesBufferSize = 1000;

	public Master(int maxSourcesToTrack, int nBands, int maxLinesOfCode, File startDir, Flag stopFlag, View view) {
		super("master");
		this.startDir = startDir;
		this.stopFlag = stopFlag;
		this.view = view;		
		map = new SourceLocMap(maxSourcesToTrack, nBands, maxLinesOfCode);		
		nSrcAnalyzerAgents = Runtime.getRuntime().availableProcessors();
	}
	
	public void run() {
		log("started.");
		try {			
			
			long t0 = System.currentTimeMillis();

			Flag done = new Flag();
			ViewerAgent viewer = new ViewerAgent(map,view,done);
			viewer.start();

			/* spawn discoverer */
			
			BoundedBuffer<File> srcFiles = new BoundedBuffer<File>(srcFilesBufferSize);
			SrcDiscovererAgent docDiscoverer = new SrcDiscovererAgent(startDir, srcFiles, new String[] {".java", ".c"}, stopFlag);
			docDiscoverer.start();

			/* spawn src analyzers */
			
			Latch allSrcAnalyzed = new Latch(nSrcAnalyzerAgents);
			for (int i = 0; i < nSrcAnalyzerAgents; i++) {
				// 	public SrcAnalyzer(String id, BoundedBuffer<File> srcFiles, SourceLocMap sourceMap, Latch analyzerLatch) throws Exception  {
				new SrcAnalyserAgent("" + i, srcFiles, map, allSrcAnalyzed, stopFlag).start();
			}
			/* wait for loaders to complete */
			allSrcAnalyzed.await();
			log("analyzers done.");
			
			done.set();
			view.done();
			
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
