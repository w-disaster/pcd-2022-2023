package pcd.ass01.part2;

import java.io.File;

/**
 * 
 * This agent explores recursively a folder looking for docs (sources)
 * to be analysed. 
 * 
 * It works as a producer in a producers/consumers architecture,   
 * sharing a bounded buffer to store docs found.
 * 
 * @author aricci
 *
 */
public class SrcDiscovererAgent extends BasicAgent {

	private File startDir;
	private BoundedBuffer<File> srcFiles;
	private int nDocsFound;
	private String[] extensions;
	private Flag stopFlag;
	
	public SrcDiscovererAgent(File dir, BoundedBuffer<File> srcFiles, String[] extensions, Flag stopFlag) {
		super("src-discoverer");
		this.startDir = dir;	
		this.srcFiles = srcFiles;
		this.extensions = extensions;
		this.stopFlag = stopFlag;
	}
	
	public void run() {
		log("started - dir: " + startDir);
		nDocsFound = 0;
		if (startDir.isDirectory()) {
			explore(startDir);
			srcFiles.close();
			log("job done - " + nDocsFound + " docs found.");
		}
	}
	
	private void explore(File dir) {
		if (!stopFlag.isSet()) {
			for (File f: dir.listFiles()) {
				if (stopFlag.isSet()) {
					break;
				}
				if (f.isDirectory()) {
					explore(f);
				} else if (checkExt(f)) {
					try {
						logDebug("find a new doc: " + f.getName());
						srcFiles.put(f);
						nDocsFound++;
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
