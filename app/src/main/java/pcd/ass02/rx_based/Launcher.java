package pcd.ass02.rx_based;
import pcd.ass02.common.InputListener;

/**
 * 
 * Assignment #02 - Reactive Programming Solution
 *   
 * @author aricci
 *
 */
public class Launcher {
	public static void main(String[] args) {		
		try {

			int defaultNumBands = 21;
			int defaukltMaxLoc = 5000;

			String defaultStartDir = "data/linux-master";
			// String defaultStartDir = "data/jpf-core-master";
			
			int defaultMaxFileToRank = 15; 
			
			View view = new View(defaultStartDir, defaultMaxFileToRank, defaultNumBands, defaukltMaxLoc);
			InputListener controller = new Controller(view);
			view.addListener(controller);
			
			view.display();						
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
