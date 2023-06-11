package pcd.ass02.task_based;

/**
 * 
 * Assignment #02 - Task-based Solution
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
