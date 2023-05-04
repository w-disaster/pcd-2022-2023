package pcd.ass01.part2;

import java.io.File;

public interface InputListener {

	void started(File dir, int maxFiles, int nBands, int maxLoc);
	
	void stopped();
}
