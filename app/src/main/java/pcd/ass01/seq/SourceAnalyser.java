package pcd.ass01.seq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pcd.ass01.common.SourceAnalysisLib;


public class SourceAnalyser {

	private SourceLocMap map;
	private long nSources;
	
	public SourceAnalyser(int maxSourcesToTrack, int nBands, int maxLinesOfCode) {
		map = new SourceLocMap(maxSourcesToTrack, nBands, maxLinesOfCode);
	}

	public void analyse(File sourceDir) {
		try {
			log("started - analysing " + sourceDir);
			
			long t0 = System.currentTimeMillis();

			nSources = 0;
			
			exploreAndElab(sourceDir);
			
			log("Number of sources analyzed: " + nSources);
			map.dumpSrcsWithMoreNLocs();
			map.dumpDistribution();			
			
			long t1 = System.currentTimeMillis();
			log("Done in " + (t1 - t0));
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	 	
	private void exploreAndElab(File dir) {
		for (File f: dir.listFiles()) {
			if (f.isDirectory()) {
				exploreAndElab(f);
			} else if (f.getName().endsWith(".java") || f.getName().endsWith(".c")) {
				logDebug("find a new source: " + f.getName());
				try {
					loadAndElabSource(f);
					nSources++;
				} catch (Exception ex) {
					ex.printStackTrace();
					log("error in processing the doc.");
				}
			}
		}
	}

	private void loadAndElabSource(File src) throws Exception {

		try {
			/* comments still to be removed */ 
			
			FileReader fr = new FileReader(src);
			BufferedReader br = new BufferedReader(fr);
					
			long nLines = SourceAnalysisLib.countLoC_MoreRefinedStrategy(br);

			fr.close();
			
			map.add(src.getCanonicalPath(), (int) nLines);
						
		} catch (Exception ex) {
			ex.printStackTrace();
		}
  	}
	
	protected void log(String msg) {
		System.out.println("[analyzer] " + msg);
	}

	protected void logDebug(String msg) {
		// System.out.println("[analyzer] " + msg);
	}

}
