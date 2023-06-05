package pcd.ass02.event_based;

public class ReportSnapshot {
	
	private long nSrcProcessed;
	private ReportEntry[] entries;
	private int[] bands;
	private int maxLoC;
	
	public ReportSnapshot(long nSrcProcessed, ReportEntry[] entries, int[] bands, int maxLoC) {
		this.nSrcProcessed = nSrcProcessed;
		this.entries = entries;
		this.bands = bands;
		this.maxLoC = maxLoC;
	}
	
	public long getNumSrcProcessed() {
		return nSrcProcessed;
	}

	public ReportEntry[] getRank() {
		return entries;
	}

	public int[] getBands() {
		return bands;
	}
	
	public int getMaxLoc() {
		return maxLoC;
	}
}
