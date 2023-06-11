package pcd.ass02.rx_based;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import pcd.ass02.common.*;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SourceAnalyserImpl implements SourceAnalyser {

	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private Flag stopFlag;
	private String[] extensions;
	
	public SourceAnalyserImpl(int maxSourcesToTrack, int nBands, int maxLinesOfCode, Flag stopFlag) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.stopFlag = stopFlag;
		extensions = new String[] { ".java", ".c" };
	}

	@Override
	public Single<Report> getReport(String directory){
		Report report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);
		ReportSnapshot sn = report.getSnaptshot();
		Flowable<File> srcStream = this.createSrcStream(directory).subscribeOn(Schedulers.io());
		Flowable<ReportEntry> repEntryStream = this.createRepEntryStream(srcStream);
		Flowable<ReportSnapshot> mapStream = this.createMapStream(report, repEntryStream);
		return mapStream.last(sn).map(s -> {
			return report;
		});		
	}

	@Override
	public Flowable<ReportSnapshot>  analyzeSources(String directory){
		Report report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);
		Flowable<File> srcStream = this.createSrcStream(directory).subscribeOn(Schedulers.io());
		Flowable<ReportEntry> repEntryStream = this.createRepEntryStream(srcStream);
		return this.createMapStream(report, repEntryStream);
	}

	private Flowable<File> createSrcStream(String directory) {
		return Flowable.create(emitter -> {
			explore(new File(directory), emitter);
			emitter.onComplete();
		}, BackpressureStrategy.BUFFER);
	}

	private void explore(File dir, FlowableEmitter<File> emitter) {
		if (!stopFlag.isSet()) {
			for (File f : dir.listFiles()) {
				if (f.isDirectory()) {
					explore(f, emitter);
				} else if (checkExt(f)) {
					try {
						// logDebug("find a new src: " + f.getName());
						emitter.onNext(f);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private boolean checkExt(File file) {
		for (int i = 0; i < extensions.length; i++) {
			if (file.getAbsolutePath().endsWith(extensions[i])) {
				return true;
			}
		}
		return false;
	}

	private Flowable<ReportEntry> createRepEntryStream(Flowable<File> fs) {
		return fs.map(f -> {
			if (!stopFlag.isSet()) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				// long nLines = SourceAnalysisLib.countLoC_SimpleStrategy(br);
				long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(br);
				fr.close();
				return new ReportEntry(f.getName(), f.getAbsolutePath(), (int) nLines);
			} else {
				return null;
			}
		});
	}

	private Flowable<ReportSnapshot> createMapStream(Report report, Flowable<ReportEntry> s) {
		return s.observeOn(Schedulers.single()).map(e -> {
			if (!stopFlag.isSet()) {
				report.add(e);
				// log("entry: " + e.getSrcFileName() + " " + e.getNLoc() + " -- " + report.getNumSrcProcessed());
			} 
			return report.getSnaptshot();
		});
	}
	protected void log(String msg) {
		System.out.println("[SourceAnalyserImpl] " + msg);
	}

	protected void logDebug(String msg) {
		// System.out.println("[SourceAnalyserImpl] " + msg);
	}
	
}
