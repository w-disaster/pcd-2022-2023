package pcd.ass02.event_based;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

public class Master extends AbstractVerticle {

	private String startDir;
	private View view;
	private Controller controller;
	private int maxSourcesToTrack;
	private int nBands;
	private int maxLinesOfCode;
	private long lastTimeDisplayed;


	public Master(Controller controller, int maxSourcesToTrack, int nBands, int maxLinesOfCode, String startDir, View view) {
		this.startDir = startDir;
		this.view = view;		
		this.maxLinesOfCode = maxLinesOfCode;
		this.nBands = nBands;
		this.maxSourcesToTrack = maxSourcesToTrack;
		this.controller = controller;
	}

	public void start(Promise<Void> startPromise) {
		log("started.");
		long t0 = System.currentTimeMillis();

		// SourceAnalyser lib = new SourceAnalyserImplSingleVerticle(this.getVertx(), maxSourcesToTrack, nBands, maxLinesOfCode);
		SourceAnalyser lib = new SourceAnalyserImpl(this.getVertx(), maxSourcesToTrack, nBands, maxLinesOfCode);
		Future<Report> frep = lib.analyzeSources(startDir);

		frep.onSuccess(rep -> {
			view.update(rep.getSnaptshot());
			view.done();					
			rep.dumpSrcsWithMoreNLocs();
			rep.dumpDistribution();
			long t1 = System.currentTimeMillis();
			log("Done in " + (t1 - t0));
			controller.done();
		});
		
		EventBus eb = vertx.eventBus();
		lastTimeDisplayed = 0;
		eb.consumer(ChannelProtocol.REPORT_SNAPSHOTS_CHANNEL, ev -> {
			ReportSnapshot repSnap = (ReportSnapshot) ev.body();
			long now = System.currentTimeMillis();
			if (now - lastTimeDisplayed > 10) {
				view.update(repSnap);
				lastTimeDisplayed = now;
			}
		});
		
		startPromise.complete();
	}

	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

	protected void log(String msg) {
		System.out.println("[Master] " + msg);
	}

}
