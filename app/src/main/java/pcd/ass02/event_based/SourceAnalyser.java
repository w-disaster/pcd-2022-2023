package pcd.ass02.event_based;

import io.vertx.core.Future;

public interface SourceAnalyser {

	Future<Report> getReport(String directory);

	Future<Report>  analyzeSources(String directory);

}
