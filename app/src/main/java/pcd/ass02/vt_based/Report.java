package pcd.ass02.vt_based;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Report {

	private int[] bands;
	private LinkedList<ReportEntry> maxLocSources;
	private int maxSourcesToTrack;
	private int maxLinesOfCode;
	private long nSrcProcessed;

	private boolean noMoreSrcToDiscover;
	private int numAnalysersWorking;
	private boolean isReady;
	private ReentrantLock lock;
	private Condition ready;
	
	public Report(int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		maxLocSources = new LinkedList<>();
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.maxLinesOfCode = maxLinesOfCode;
		bands = new int[nBands];
		for (int i = 0; i < bands.length; i++) {
			bands[i] = 0;
		}
		nSrcProcessed = 0;
		lock = new ReentrantLock();
		ready = lock.newCondition();
		isReady = false;
		noMoreSrcToDiscover = false;
		numAnalysersWorking = 0;
	}
		
	public long getNumSrcProcessed() throws InterruptedException {
		try {
			lock.lock();
			while (!isReady) {
				ready.await();
			}
			return this.nSrcProcessed;
		} finally {
			lock.unlock();
			
		}
	}
	
	public void add(ReportEntry entry) {
		try {
			lock.lock();
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
		} finally {
			lock.unlock();
		}
	}
		
	public ReportSnapshot getSnaptshot() {
		try {
			lock.lock();
			ReportEntry[] list = new ReportEntry[maxLocSources.size()];
			maxLocSources.toArray(list);
			return new ReportSnapshot(nSrcProcessed, list, bands.clone(),maxLinesOfCode);
		} finally {
			lock.unlock();
			
		}
	}


	private void log(String msg) {
		System.out.println("[REPORT] " + msg);
	}
	
	public void notifyNewAnalyserWorking() {
		try {
			lock.lock();
			numAnalysersWorking++;
		} finally {
			lock.unlock();
		}
	}

	public void notifyAnalyserWorkingDone() {
		try {
			lock.lock();
			numAnalysersWorking--;
			if (numAnalysersWorking == 0 && noMoreSrcToDiscover) {
				isReady = true;
				ready.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

	public void notifyNoMoreSrcToDiscover() {
		try {
			lock.lock();
			noMoreSrcToDiscover = true;
			if (numAnalysersWorking == 0 && noMoreSrcToDiscover) {
				isReady = true;
				ready.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void waitToBeReady() throws InterruptedException {
		try {
			lock.lock();
			while (!isReady) {
				ready.await();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void dumpSrcsWithMoreNLocs() {
		try {
			lock.lock();
			maxLocSources.forEach(el -> {
				System.out.println((el.getSrcFullPath() + " - "  + el.getNLoc()));
			}); 
		} finally {
			lock.unlock();
			
		}
	}

	public void dumpDistribution() {
		try {
			lock.lock();
			int nLocPerBand = maxLinesOfCode/(bands.length - 1);
			int a = 0;
			int b = nLocPerBand;
			
			for (int i = 0; i < bands.length - 1; i++) {
				System.out.println("band " + (i+1) + " (" + a + " - " + b + "): " + bands[i]);
				a = b + 1;
				b += nLocPerBand;
			}
			System.out.println("band " + bands.length + " ( >= " + a + "): " + bands[bands.length - 1]);
		} finally {
			lock.unlock();
		}	
	}	
}
