package pcd.ass02.event_based;

import io.vertx.core.Vertx;
import pcd.ass02.common.*;

/**
 * 
 * Assignment #02 - Asynchronous/event-based Solution
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
