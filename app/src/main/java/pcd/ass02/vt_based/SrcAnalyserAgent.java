package pcd.ass02.vt_based;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import pcd.ass02.common.Flag;
import pcd.ass02.common.SourceAnalysisLib;

public class SrcAnalyserAgent implements Runnable {

	private Report report;
	private Flag stopFlag;
	private File src;

	protected SrcAnalyserAgent(File src, Report report, Flag stopFlag) {
		this.report = report;
		this.stopFlag = stopFlag;
		this.src = src;
	}

	public static Thread make(int id, File src, Report report, Flag stopFlag) {
		Thread t = 
				Thread
				.ofVirtual()
				.name("SrcAnalyserTask-"+id)
				.unstarted(new SrcAnalyserAgent(src, report, stopFlag));
		return t;
	}
	
	public void run() {
		logDebug("working on " + src.getName());
		try {
			if (!stopFlag.isSet()) {
				FileReader fr = new FileReader(src);
				BufferedReader br = new BufferedReader(fr);
				// long nLines = SourceAnalysisLib.countLoC_SimpleStrategy(br);
				long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(br);
				fr.close();
				ReportEntry e = new ReportEntry(src.getName(), src.getAbsolutePath(),  (int) nLines);
				report.add(e);
				// log("new entry: " + e.getSrcFileName() + " " + e.getNLoc() + " " + report.getNumSrcProcessed());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			report.notifyAnalyserWorkingDone();
		}
	}

	protected void log(String msg) {
		System.out.println("[SrcAnalyserTask] " + msg);
	}

	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

}
