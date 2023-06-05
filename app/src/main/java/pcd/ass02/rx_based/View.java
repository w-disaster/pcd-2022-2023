package pcd.ass02.rx_based;

import pcd.ass02.common.InputListener;

/**
 * Class representing the view part of the application.
 * 
 * @author aricci
 *
 */
public class View {

	private ViewFrame frame;
	private ViewDistributionFrame distribFrame;
	
	public View(String defStartDir, int defMaxFileToRank, int defaultNumBands, int defaultMaxLoc){
		frame = new ViewFrame(defStartDir, defMaxFileToRank, defaultNumBands, defaultMaxLoc);
		distribFrame = new ViewDistributionFrame(800,600);
	}
	
	public void addListener(InputListener l){
		frame.addListener(l);
	}

	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	frame.setVisible(true);
        	distribFrame.setVisible(true);
        });
    }
	
	public void update(ReportSnapshot report) {
		frame.update(report.getNumSrcProcessed(), report.getRank());
		distribFrame.updateDistribution(report);
	}
	
	public void done() {
		frame.done();
	}

}
	
