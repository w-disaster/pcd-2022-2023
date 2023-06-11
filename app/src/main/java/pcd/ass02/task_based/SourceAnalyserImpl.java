package pcd.ass02.task_based;

import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import pcd.ass02.common.Flag;

public class SourceAnalyserImpl implements SourceAnalyser {

	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private Flag stopFlag;
	
	public SourceAnalyserImpl(int maxSourcesToTrack, int nBands, int maxLinesOfCode, Flag stopFlag) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.stopFlag = stopFlag;
	}

	public SourceAnalyserImpl(Flag stopFlag) {
		this.stopFlag = stopFlag;
	}
	
	@Override
	public Future<Report> getReport(File dir) {
		Report report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);		
	    ForkJoinPool srcDiscovererTaskPool = new ForkJoinPool();
		SrcDiscovererTask task = new SrcDiscovererTask(dir, report, new String[] {".java", ".c"}, stopFlag);
		return srcDiscovererTaskPool.submit(task);		
	}
	
	@Override
	public void analyzeSources(File dir, Report report) {
    	ForkJoinPool srcDiscovererTaskPool = new ForkJoinPool();
    	SrcDiscovererTask task = new SrcDiscovererTask(dir, report, new String[] {".java", ".c"}, stopFlag);
    	srcDiscovererTaskPool.submit(task);		
    	srcDiscovererTaskPool.close(); /* blocking */
	}

}
