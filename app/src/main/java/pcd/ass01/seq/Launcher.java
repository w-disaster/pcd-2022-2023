package pcd.ass01.seq;

import java.io.File;

/**
 * 
 * Assignment #01 - Part 1 - Sequential version
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
			
			// File dir = new File("data/spring-framework-main");
			File dir = new File("data/linux-master");
			// File dir = new File("data/jdk-master");
			
			var an = new SourceAnalyser(maxSourcesToTrack, nBands, maxLoc);
			an.analyse(dir);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
}
