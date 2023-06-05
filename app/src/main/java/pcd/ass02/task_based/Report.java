package pcd.ass02.task_based;

import java.util.Iterator;
import java.util.LinkedList;

public class Report {

	private int[] bands;
	private LinkedList<ReportEntry> maxLocSources;
	private int maxSourcesToTrack;
	private int maxLinesOfCode;
	private long nSrcProcessed;
	
	public Report(int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		maxLocSources = new LinkedList<>();
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.maxLinesOfCode = maxLinesOfCode;
		bands = new int[nBands];
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}
		nSrcProcessed = 0;
	}
	
	public synchronized void clear() {
		maxLocSources.clear();
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}
		nSrcProcessed = 0;
	}
	
	public synchronized long getNumSrcProcessed() {
		return this.nSrcProcessed;
	}

	public synchronized void add(ReportEntry entry) {
		nSrcProcessed++;
		Iterator<ReportEntry> it = maxLocSources.iterator();
		int pos = 0;
		boolean toBeInserted = false;
		while (it.hasNext() && pos < maxSourcesToTrack) {
			ReportEntry el = it.next();
			if (entry.getNLoc() > el.getNLoc()) {
				toBeInserted = true;
				break;
			} 	
			pos++;
		}

		if (pos < maxSourcesToTrack || toBeInserted) {
			maxLocSources.add(pos, entry);
			if (maxLocSources.size() > maxSourcesToTrack) {	
				maxLocSources.removeLast();
			}
		}

		if (entry.getNLoc() > maxLinesOfCode) {
			bands[bands.length - 1]++;
		} else {
			int nLocPerBand = maxLinesOfCode/(bands.length - 1);
			int bandIndex = entry.getNLoc() / nLocPerBand;
			bands[bandIndex]++;
		}
	}
	
	public synchronized void dumpSrcsWithMoreNLocs() {
		maxLocSources.forEach(el -> {
			System.out.println((el.getSrcFullPath() + " - "  + el.getNLoc()));
		}); 
	}

	
	public synchronized ReportSnapshot getSnaptshot() {
		ReportEntry[] list = new ReportEntry[maxLocSources.size()];
		maxLocSources.toArray(list);
		return new ReportSnapshot(nSrcProcessed, list, bands.clone(),maxLinesOfCode);
	}
		
	public synchronized void dumpDistribution() {
		int nLocPerBand = maxLinesOfCode/(bands.length - 1);
		int a = 0;
		int b = nLocPerBand;
		
		for (int i = 0; i < bands.length - 1; i++) {
			System.out.println("band " + (i+1) + " (" + a + " - " + b + "): " + bands[i]);
			a = b + 1;
			b += nLocPerBand;
		}
		System.out.println("band " + bands.length + " ( >= " + a + "): " + bands[bands.length - 1]);
	}
	
}
