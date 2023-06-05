package pcd.ass02.task_based;

import java.io.File;
import pcd.ass02.common.Flag;

public class Master extends BasicAgent {

	private File startDir;
	private View view;
	private Flag stopFlag;
	private Report report;
	

	public Master(int maxSourcesToTrack, int nBands, int maxLinesOfCode, File startDir, Flag stopFlag, View view) {
		super("master");
		this.startDir = startDir;
		this.stopFlag = stopFlag;
		this.view = view;		
		report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);		
	}
	
	public void run() {
		log("started.");
		try {						
			long t0 = System.currentTimeMillis();

			Flag done = new Flag();
			ViewerAgent viewer = new ViewerAgent(report,view,done);
			viewer.start();

			SourceAnalyser lib = new SourceAnalyserImpl(stopFlag);
			lib.analyzeSources(startDir, report);

			/*
			Future<Report> frep = lib.getReport(startDir);			
			Report rep = frep.get();
			*/
			
			done.set();
			view.done();
			
			log("Number of sources analyzed: " + report.getNumSrcProcessed());
			report.dumpSrcsWithMoreNLocs();
			report.dumpDistribution();
			
			long t1 = System.currentTimeMillis();
			log("Done in " + (t1 - t0));
						

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

	protected void log(String msg) {
		System.out.println("[ " + getName() +"] " + msg);
	}
	
}
