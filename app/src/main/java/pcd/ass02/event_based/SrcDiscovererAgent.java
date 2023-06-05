package pcd.ass02.event_based;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

import java.util.*;

public class SrcDiscovererAgent extends AbstractVerticle {

	private String startDir;
	private int nSrcFound;
	private String[] extensions;
	private boolean stopped;

	public SrcDiscovererAgent(String dir, String[] extensions) {
		this.startDir = dir;
		this.extensions = extensions;
	}

	public void start(Promise<Void> startPromise) {
		log("started");
		startPromise.complete();
		nSrcFound = 0;
		stopped = false;
		traverse(startDir)
		.onSuccess(v -> {
 			vertx.eventBus().publish(ChannelProtocol.DISCOVERER_DONE_CHANNEL, "discoverer-done");
		});
		
		EventBus eb = vertx.eventBus();
		eb.consumer(ChannelProtocol.STOPPED_CHANNEL, ev -> {
			stopped = true;
		});

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
										vertx.eventBus().send(ChannelProtocol.SRC_CHANNEL, file);
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
		System.out.println("[SrcDiscoverAgent] " + msg);
	}

}
