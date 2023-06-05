package pcd.ass02.event_based;

import java.io.File;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import pcd.ass02.common.*;

/**
 * Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private View view;
	private Vertx vertx;
	private String masterId;
	
	public Controller(View view){
		this.view = view;
		vertx = Vertx.vertx();
	}
	
	public void started(File dir, int nMaxFilesToRank, int nBands, int maxLoc){
		vertx.deployVerticle(new Master(this, nMaxFilesToRank, nBands, maxLoc, dir.getAbsolutePath(), view),
				res -> {
				    masterId = res.result();
				});
	}

	public void stopped() {
		vertx.eventBus().publish(ChannelProtocol.STOPPED_CHANNEL, "stop");
	}
	
	public void done() {
		vertx.undeploy(masterId)
		.onFailure(e -> {
			e.printStackTrace();
		})
		.onSuccess(res -> {
			log("Master undeployed.");
		});
	}

	protected void log(String msg) {
		System.out.println("[Controller] " + msg);
	}
	
}
