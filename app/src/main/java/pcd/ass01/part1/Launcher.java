package pcd.ass01.part1;

import java.io.File;

/**
 * 
 * Assignment #01 - Part 1 - Multithreaded without GUI
 * 
 * @author aricci
 *
 */
public class Launcher {
	public static void main(String[] args) {		
		try {
			int maxSourcesToTrack = 20;
			int nBands = 20;
			int maxLoc = 5000;
			
			int nSrcAnalysers = Runtime.getRuntime().availableProcessors(); 
			int srcFilesBufferSize = 1000;
			
			// File dir = new File("data/test");
			// File dir = new File("data/spring-framework-main");
			File dir = new File("data/linux-master");
			// File dir = new File("data/jdk-master");
			
			var master = new Master(maxSourcesToTrack, nBands, maxLoc, dir, nSrcAnalysers, srcFilesBufferSize);
			master.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
