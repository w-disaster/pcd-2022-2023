package pcd.ass01.jpf;

public class View {
	
	private InputListener listener;
	private int defMaxFileToRank;
	private int defaultNumBands;
	private int defaultMaxLoc;
	private File startDir;
	
	public View(String startDir, int defMaxFileToRank, int defaultNumBands, int defaultMaxLoc){
		this.defaultMaxLoc = defMaxFileToRank;
		this.defaultNumBands = defaultNumBands;
		this.defaultMaxLoc = defaultMaxLoc;
		this.defMaxFileToRank = defMaxFileToRank;
	}
	
	public void addListener(InputListener l){
		listener = l;
	}

	public void display() {
		new EDTSimulationAgent(this).start();
	}
	
	public void update(SourceLocMapSnapshot snapshot) {
	}
	
	public void done() {
	}

	public void simulateNotifyStarted() {
		listener.started(startDir, defaultMaxLoc, defaultNumBands, defMaxFileToRank);
	}
	
	public void simulateNotifyStopped() {
		listener.stopped();
	}
}
	
