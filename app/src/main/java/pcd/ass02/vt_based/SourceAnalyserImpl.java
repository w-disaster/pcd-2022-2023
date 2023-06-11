package pcd.ass02.vt_based;

import java.io.File;
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
			
	@Override
	public Report getReport(File startDir) {
		Report report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);		
		SrcDiscovererAgent docDiscoverer = new SrcDiscovererAgent(startDir, report, new String[] {".java", ".c"}, stopFlag);
		docDiscoverer.start();
		return report;
	}

}
