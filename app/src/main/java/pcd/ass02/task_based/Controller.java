package pcd.ass02.task_based;

import java.io.File;

import pcd.ass02.common.Flag;
import pcd.ass02.common.InputListener;

/**
 * Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private Flag stopFlag;
	private View view;
	
	public Controller(View view){
		this.stopFlag = new Flag();
		this.view = view;
	}
	
	public synchronized void started(File dir, int nMaxFilesToRank, int nBands, int maxLoc){
		stopFlag.reset();
		
		var master = new Master(nMaxFilesToRank, nBands, maxLoc, dir, stopFlag, view);
		master.start();
	}

	public synchronized void stopped() {
		stopFlag.set();
	}

}
