package pcd.ass02.event_based;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class SourceAnalyserImpl implements SourceAnalyser {

	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private Vertx vertx;
	private String discovererId, analyserId;
	
	
	public SourceAnalyserImpl(Vertx vertx, int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.vertx = vertx;
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
		Promise<Report> p = Promise.promise();		
		deployDiscoverer(dir);
		deployAnalyser(false);
		configureReportReception(p);
		return p.future();
	}

	@Override
	public Future<Report>  analyzeSources(String dir) {
		Promise<Report> p = Promise.promise();
		deployDiscoverer(dir);
		deployAnalyser(true);		
		configureReportReception(p);
		return p.future();
	}

	private void deployDiscoverer(String dir) {
		vertx.deployVerticle(new SrcDiscovererAgent(dir, new String[] {".java", ".c"}),
				res -> {
					if (res.succeeded()) {
						logDebug("discoverer deployed.");
				        this.discovererId = res.result();
					}
				});
	}

	private void deployAnalyser(boolean publishing) {
		vertx.deployVerticle(new SrcAnalyserAgent(maxSourcesToTrack, nBands, maxLinesOfCode, true),
				res -> {
					if (res.succeeded()) {
						logDebug("analyser deployed.");
				        this.analyserId = res.result();
					}
				});
	}
	
	private void configureReportReception(Promise<Report> p) {
		EventBus eb = vertx.eventBus();
		eb.consumer(ChannelProtocol.REPORT_CHANNEL, ev -> {
			logDebug("report ready");	
			Report report = (Report) ev.body();
			p.complete(report);
			
			vertx
			.undeploy(discovererId)
			.onSuccess(res -> {
				log("Discoverer undeployed.");
			});
			
			vertx.undeploy(analyserId)
			.onSuccess(res -> {
				log("Analyser undeployed.");
			});
		});
	}
	
	protected void log(String msg) {
		System.out.println("[SourceAnalyserImpl] " + msg);
	}

	protected void logDebug(String msg) {
		System.out.println("[SourceAnalyserImpl] " + msg);
	}
	
}
