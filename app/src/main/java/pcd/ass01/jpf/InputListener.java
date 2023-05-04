package pcd.ass01.jpf;

public interface InputListener {

	void started(File dir, int maxFiles, int nBands, int maxLoc);
	
	void stopped();
}
