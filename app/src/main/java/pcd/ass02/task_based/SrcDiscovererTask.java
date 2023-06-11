package pcd.ass02.task_based;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import pcd.ass02.common.Flag;

public class SrcDiscovererTask extends RecursiveTask<Report> {

	private File dir;
	private String[] extensions;
	private Flag stopFlag;
	private Report report;
	
	public SrcDiscovererTask(File dir, Report report, String[] extensions, Flag stopFlag) {
		this.dir = dir;
		this.extensions = extensions;
		this.stopFlag = stopFlag;
		this.report = report;
	}

	protected Report compute() {
		logDebug("started - dir: " + dir);
		List<SrcDiscovererTask> discTasks = new LinkedList<SrcDiscovererTask>();
		List<SrcAnalyserTask> analyserTasks = new LinkedList<SrcAnalyserTask>();

		for (File f : dir.listFiles()) {
			if (stopFlag.isSet()) {
				break;
			}
			if (f.isDirectory()) {
				SrcDiscovererTask task = new SrcDiscovererTask(f, report, extensions, stopFlag);
				task.fork();
				discTasks.add(task);
			} else if (checkExt(f)) {
				SrcAnalyserTask task = new SrcAnalyserTask(f, stopFlag);
				task.fork();
				analyserTasks.add(task);
			}
		}
		
		for (var t: analyserTasks) {
			report.add(t.join());
		}
		
		for (var t: discTasks) {
			t.join();
		}
		
		return report;
	}

	private boolean checkExt(File f) {
		for (int i = 0; i < extensions.length; i++) {
			if (f.getName().endsWith(extensions[i])) {
				return true;
			}
		}
		return false;
	}

	protected void log(String msg) {
		System.out.println("[SrcDiscovererTask] " + msg);
	}

	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

}
