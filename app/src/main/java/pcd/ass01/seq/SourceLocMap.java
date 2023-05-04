package pcd.ass01.seq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

class LocEntry {
	
	private String src;
	private int nLoc;
	
	LocEntry(String src, int nLoc){
		this.src = src;
		this.nLoc = nLoc;
	}
	
	public String getSrc() {
		return src;
	}
	
	public int getNLoc() {
		return nLoc;
	}
}

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
	
	public void clear() {
		maxLocSources.clear();
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}
	}
	
	public  void add(String src, int nLoc) {
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
	
	public void dumpSrcsWithMoreNLocs() {
		maxLocSources.forEach(el -> {
			System.out.println((el.getSrc() + " - "  + el.getNLoc()));
		}); 
	}

	public void dumpDistribution() {
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
