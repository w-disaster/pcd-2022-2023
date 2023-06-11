package pcd.ass02.vt_based;

import java.io.File;
import pcd.ass02.common.Flag;

public class SrcDiscovererAgent extends BasicAgent {

	private File startDir;
	private int nDocsFound;
	private String[] extensions;
	private Flag stopFlag;
	private Report report;
	
	public SrcDiscovererAgent(File dir, Report report, String[] extensions, Flag stopFlag) {
		super("src-discoverer");
		this.startDir = dir;	
		this.extensions = extensions;
		this.stopFlag = stopFlag;
		this.report = report;
	}
	
	public void run() {
		log("started.");
		nDocsFound = 0;
		if (startDir.isDirectory()) {
			explore(startDir);
			report.notifyNoMoreSrcToDiscover();
			log("job done - " + nDocsFound + " docs found.");
		}
	}
	
	private void explore(File dir) {
		if (!stopFlag.isSet()) {
			for (File f: dir.listFiles()) {
				if (f.isDirectory()) {
					explore(f);
				} else if (checkExt(f)) {
					try {
						if (!stopFlag.isSet()) {
							nDocsFound++;
							logDebug("find a new doc: " + f.getName() + " " + nDocsFound);
							Thread t = SrcAnalyserAgent.make(nDocsFound, f, report, stopFlag);
							report.notifyNewAnalyserWorking();
							t.start();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	private boolean checkExt(File f) {
		for (int i = 0; i < extensions.length; i++) {
			if (f.getName().endsWith(extensions[i])){
				return true;
			}
		}
		return false;
	}
}
