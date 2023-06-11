package pcd.ass02.task_based;

import java.io.File;
import java.util.concurrent.Future;

import pcd.ass02.common.Flag;

public interface SourceAnalyser {

	Future<Report> getReport(File directory);
	
	void analyzeSources(File directory, Report report);

	
}
