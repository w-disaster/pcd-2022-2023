package pcd.ass01.part1;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * Monitor keeping track of the sources analysed and
 * related data.
 * 
 * @author aricci
 *
 */
public class SourceLocMap {

	private int[] bands;
	private LinkedList<LocEntry> maxLocSources;
	private int maxSourcesToTrack;
	private int maxLinesOfCode;
	
	public SourceLocMap(int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		maxLocSources = new LinkedList<>();
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.maxLinesOfCode = maxLinesOfCode;
		bands = new int[nBands];
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}		
	}
	
	public synchronized void clear() {
		maxLocSources.clear();
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}
	}
	
	public  synchronized void add(String src, int nLoc) {
		Iterator<LocEntry> it = maxLocSources.iterator();
		int pos = 0;
		boolean toBeInserted = false;
		while (it.hasNext() && pos < maxSourcesToTrack) {
			LocEntry el = it.next();
			if (nLoc > el.getNLoc()) {
				toBeInserted = true;
				break;
			} 	
			pos++;
		}

		if (pos < maxSourcesToTrack || toBeInserted) {
			maxLocSources.add(pos, new LocEntry(src, nLoc));
			if (maxLocSources.size() > maxSourcesToTrack) {	
				maxLocSources.removeLast();
			}
		}

		if (nLoc >= maxLinesOfCode) {
			bands[bands.length - 1]++;
		} else {
			int nLocPerBand = maxLinesOfCode/bands.length;
			int bandIndex = nLoc / nLocPerBand;
			bands[bandIndex]++;
		}
	}
	
	public synchronized void dumpSrcsWithMoreNLocs() {
		maxLocSources.forEach(el -> {
			System.out.println((el.getSrc() + " - "  + el.getNLoc()));
		}); 
	}

	public synchronized void dumpDistribution() {
		int nLocPerBand = maxLinesOfCode/bands.length;
		int a = 0;
		int b = nLocPerBand - 1;
		
		for (int i = 0; i < bands.length - 1; i++) {
			System.out.println("band " + (i+1) + " (" + a + " - " + b + "): " + bands[i]);
			a += nLocPerBand;
			b += nLocPerBand;
		}
		System.out.println("band " + bands.length + " ( > " + (a+1) + "): " + bands[bands.length - 1]);
	}
	
}
