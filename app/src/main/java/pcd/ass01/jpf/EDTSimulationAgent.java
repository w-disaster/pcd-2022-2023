package pcd.ass01.jpf;

public class EDTSimulationAgent extends BasicAgent {

	private View view;
	
	protected EDTSimulationAgent(View view) {
		super("edt");
		this.view = view;
	}

	public void run() {
		view.simulateNotifyStarted();
		double r = Math.random();
		if (r > 0.5) {
			view.simulateNotifyStopped();
		}
	}
}
