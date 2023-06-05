package pcd.ass02.task_based;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.RecursiveTask;

import pcd.ass02.common.Flag;
import pcd.ass02.common.SourceAnalysisLib;

public class SrcAnalyserTask extends RecursiveTask<ReportEntry> {

	private Flag stopFlag;
	private File src;

	public SrcAnalyserTask(File src, Flag stopFlag) {
		this.stopFlag = stopFlag;
		this.src = src;
	}

	public ReportEntry compute() {
		logDebug("working on " + src.getName());
		try {
			if (!stopFlag.isSet()) {
				FileReader fr = new FileReader(src);
				BufferedReader br = new BufferedReader(fr);
				// long nLines = SourceAnalysisLib.countLoC_SimpleStrategy(br);
				long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(br);
				fr.close();			
				ReportEntry reportEntry = new ReportEntry(src.getName(), src.getAbsolutePath(), (int) nLines); 
				return reportEntry;		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected void log(String msg) {
		System.out.println("[SrcAnalyserTask] " + msg);
	}

	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

}
