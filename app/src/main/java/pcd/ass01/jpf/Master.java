package pcd.ass01.jpf;

public class Master extends BasicAgent {

	private File startDir;
	private SourceLocMap map;
	private View view;
	private Flag stopFlag;
	
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
		try {			
			Flag done = new Flag();
			ViewerAgent viewer = new ViewerAgent(map,view,done);
			viewer.start();

			BoundedBuffer<File> srcFiles = new BoundedBuffer<File>(srcFilesBufferSize);
			SrcDiscovererAgent docDiscoverer = new SrcDiscovererAgent(srcFiles, stopFlag);
			docDiscoverer.start();

			Latch allSrcAnalyzed = new Latch(nSrcAnalyzerAgents);
			for (int i = 0; i < nSrcAnalyzerAgents; i++) {
				new SrcAnalyserAgent("" + i, srcFiles, map, allSrcAnalyzed, stopFlag).start();
			}

			allSrcAnalyzed.await();
			
			done.set();
			view.done();
			
			map.dumpSrcsWithMoreNLocs();
			map.dumpDistribution();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
		
}
