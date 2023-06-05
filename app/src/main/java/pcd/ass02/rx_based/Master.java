package pcd.ass02.rx_based;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pcd.ass02.common.Flag;

public class Master extends BasicAgent {

	private File startDir;
	private View view;
	private Flag stopFlag;
	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;

	public Master(int maxSourcesToTrack, int nBands, int maxLinesOfCode, File startDir, Flag stopFlag, View view) {
		super("master");
		this.startDir = startDir;
		this.stopFlag = stopFlag;
		this.view = view;
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
	}

	public void run() {
		logDebug("Started.");
		long t0 = System.currentTimeMillis();
		SourceAnalyser lib = new SourceAnalyserImpl(maxSourcesToTrack, nBands, maxLinesOfCode, stopFlag);
		Flowable<ReportSnapshot> repStream = lib.analyzeSources(startDir.getAbsolutePath());		
		repStream.subscribe(rep -> {
			view.update(rep);
		}, e -> {
			logDebug("stopped.");
		}, () -> {
			view.done();
			long t1 = System.currentTimeMillis();
			logDebug("Done in " + (t1 - t0));
		});
	}

	protected void logDebug(String msg) {
		System.out.println("[ " + getName() +"] " + msg);
	}

	protected void log(String msg) {
		System.out.println("[ " + getName() + "] " + msg);
	}

}
