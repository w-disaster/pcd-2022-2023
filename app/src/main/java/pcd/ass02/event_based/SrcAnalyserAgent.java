package pcd.ass02.event_based;

import java.io.File;
import java.util.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import pcd.ass02.common.*;

public class SrcAnalyserAgent extends AbstractVerticle {

	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private List<Future> srcAnalysedListF;
	private boolean stopped;
	private boolean publishing;
	
	public SrcAnalyserAgent(int maxSourcesToTrack, int nBands, int maxLinesOfCode, boolean publishing) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.publishing = publishing;
	}

	public SrcAnalyserAgent(int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		publishing = false;
	}

	public void start(Promise<Void> startPromise) {
		log("started");
		srcAnalysedListF = new ArrayList<Future>();

		stopped = false;
		Report report = new Report(maxSourcesToTrack, nBands, maxLinesOfCode);		

		EventBus eb = vertx.eventBus();
		
		eb.consumer(ChannelProtocol.SRC_CHANNEL, fileMsg -> {
			if (!stopped) {
				var p = Promise.promise();
				this.srcAnalysedListF.add(p.future());

				String filePath = fileMsg.body().toString();
				Future<Buffer> fbuf = vertx.fileSystem().readFile(filePath);
				fbuf.onSuccess(buf -> {
					if (!stopped) {
						vertx.executeBlocking(future -> {
							long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(buf.toString());
							ReportEntry reportEntry = new ReportEntry(new File(filePath).getName(), filePath, (int) nLines); 
							// log("new entry: " + reportEntry.getSrcFileName() + " " + reportEntry.getNLoc());
							report.add(reportEntry);
							if (publishing) {
								ReportSnapshot sn = report.getSnaptshot();
								vertx.eventBus().publish(ChannelProtocol.REPORT_SNAPSHOTS_CHANNEL, sn);
							}
							p.complete();
						});
					} else {
						p.complete();
					}
				});
			}
		});

		eb.consumer(ChannelProtocol.DISCOVERER_DONE_CHANNEL, ev -> {
			CompositeFuture.all(srcAnalysedListF).onSuccess(arg -> {
				vertx.eventBus().publish(ChannelProtocol.REPORT_CHANNEL, report);
			});
		});

		eb.consumer(ChannelProtocol.STOPPED_CHANNEL, ev -> {
			stopped = true;
		});
		
		startPromise.complete();
	}

	protected void log(String msg) {
		System.out.println("[SrcAnalyserAgent] " + msg);
	}

}
