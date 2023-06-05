package pcd.ass02.vt_based;

import java.io.File;
import pcd.ass02.common.*;

public class Master extends BasicAgent {

	private File startDir;
	private Flag stopFlag;
	private View view;
	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;

	public Master(int maxSourcesToTrack, int nBands, int maxLinesOfCode, File startDir, Flag stopFlag, View view) {
		super("master");
		this.startDir = startDir;
		this.stopFlag = stopFlag;
		this.view = view;
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
	}
	
	public void run() {
		logDebug("Started");
		try {			
			
			long t0 = System.currentTimeMillis();
			
			SourceAnalyser lib = new SourceAnalyserImpl(maxSourcesToTrack, nBands, maxLinesOfCode, stopFlag);
			Report report = lib.getReport(startDir);

			Flag done = new Flag();
			ViewerAgent viewer = new ViewerAgent(report, view, done);
			viewer.start();
			
			report.waitToBeReady();
			done.set();
			view.done();

			report.dumpSrcsWithMoreNLocs();
			report.dumpDistribution();
			
			long t1 = System.currentTimeMillis();
			logDebug("Done in " + (t1 - t0));
						

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
		
}
