package pcd.ass02.vt_based;

/**
 * 
 * Assignment #02 - Virtual Threads-based Solution
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
			int defaultMaxFileToRank = 15; 
			
			View view = new View(defaultStartDir, defaultMaxFileToRank, defaultNumBands, defaukltMaxLoc);
			Controller controller = new Controller(view);
			view.addListener(controller);
			
			view.display();						
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
