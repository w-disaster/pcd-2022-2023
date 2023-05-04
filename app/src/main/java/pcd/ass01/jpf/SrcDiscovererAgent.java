package pcd.ass01.jpf;

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

	private BoundedBuffer<File> srcFiles;
	private int nDocsFound;
	private Flag stopFlag;
	
	public SrcDiscovererAgent(BoundedBuffer<File> srcFiles, Flag stopFlag) {
		super("src-discoverer");
		this.srcFiles = srcFiles;
		this.stopFlag = stopFlag;
	}
	
	public void run() {
		nDocsFound = 0;
		simulateDocFound("src-01", 100);
		simulateDocFound("src-02", 50);
		simulateDocFound("src-03", 200);
	}
	
	private void simulateDocFound(String name, int nLoC) {
		try {
			if (!stopFlag.isSet()) {
				srcFiles.put(new File(name, nLoC));
				nDocsFound++;
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
}
