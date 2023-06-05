package pcd.ass02.vt_based;

import pcd.ass02.common.Flag;

public class ViewerAgent extends BasicAgent {

	private Report report;
	private View view;
	private Flag done;
	
	protected ViewerAgent(Report report, View view, Flag done) {
		super("viewer");
		this.report = report;
		this.view = view;
		this.done = done;
	}

	public void run() {
		while (!done.isSet()) {
			try {
				view.update(report.getSnaptshot());
				Thread.sleep(10);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		view.update(report.getSnaptshot());
	}
}
