package pcd.ass02.event_based;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import pcd.ass02.common.SourceAnalysisLib;

public class SourceAnalyserImplSingleVerticle implements SourceAnalyser {

	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private Vertx vertx;
	
	private List<Future> srcAnalysedListF;
	private boolean publishing;
	private int nSrcFound;
	private String[] extensions;
	private boolean stopped;
	private Report report;
	
	
	public SourceAnalyserImplSingleVerticle(Vertx vertx, int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.vertx = vertx;
		this.extensions = new String[] {".java", ".c"};
		
		try {
			vertx.eventBus().registerDefaultCodec(ReportSnapshot.class,
				      new GenericCodec<ReportSnapshot>(ReportSnapshot.class));
			vertx.eventBus().registerDefaultCodec(Report.class,
				      new GenericCodec<Report>(Report.class));
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	@Override
	public Future<Report> getReport(String dir) {
		return this.genReport(dir, false);
	}
	
	public Future<Report>  analyzeSources(String dir) {
		return this.genReport(dir, true);
	}

	private Future<Report> genReport(String dir, boolean publishing) {
		Promise<Report> p = Promise.promise();

		this.publishing = publishing;
		report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);		

		srcAnalysedListF = new ArrayList<Future>();
		nSrcFound = 0;
		stopped = false;
		
		traverse(dir)
		.onSuccess(v -> {
			CompositeFuture.all(srcAnalysedListF).onSuccess(arg -> {
				p.complete(report);
			});
		});
		
		EventBus eb = vertx.eventBus();
		eb.consumer(ChannelProtocol.STOPPED_CHANNEL, ev -> {
			stopped = true;
		});		

		return p.future();
	}
	

	private Future<Void> traverse(String basePath) {
		Promise<Void> promise = Promise.promise();
		try {
			vertx.fileSystem().readDir(basePath)
			.onSuccess(files -> {
				var listF = new ArrayList<Future>();
				if (!stopped) {
					files.forEach(file -> {
						Promise<Void> p = Promise.promise();
						listF.add(p.future());						
						vertx.fileSystem().props(file)
						.onSuccess(props -> {
							if (!stopped) {
								if (props.isDirectory()) {
									traverse(file)
									.onSuccess( arg -> {
										p.complete();
									});
								
								} else {
									if (props.isRegularFile() && checkExt(file)) {
										nSrcFound++;

										var p1 = Promise.promise();
										this.srcAnalysedListF.add(p1.future());
										Future<Buffer> fbuf = vertx.fileSystem().readFile(file);
										fbuf.onSuccess(buf -> {
											if (!stopped) {
												vertx.executeBlocking(future -> {
													long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(buf.toString());
													ReportEntry reportEntry = new ReportEntry(new File(file).getName(), file, (int) nLines); 
													// log("new entry: " + reportEntry.getSrcFileName() + " " + reportEntry.getNLoc());
													report.add(reportEntry);
													if (publishing) {
														ReportSnapshot sn = report.getSnaptshot();
														vertx.eventBus().publish(ChannelProtocol.REPORT_SNAPSHOTS_CHANNEL, sn);
													}
													p1.complete();
												});
											} else {
												p1.complete();
											}
										});
									
									}
									p.complete();
								}
							}
						});
					});
				}	
				CompositeFuture
				.all(listF)
				.onSuccess( arg -> {
					promise.complete();
				});
			});

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return promise.future();
	}

	private boolean checkExt(String fileName) {
		for (int i = 0; i < extensions.length; i++) {
			if (fileName.endsWith(extensions[i])) {
				return true;
			}
		}
		return false;
	}	
	protected void log(String msg) {
		System.out.println("[SourceAnalyserImpl] " + msg);
	}

	protected void logDebug(String msg) {
		System.out.println("[SourceAnalyserImpl] " + msg);
	}
	
}
