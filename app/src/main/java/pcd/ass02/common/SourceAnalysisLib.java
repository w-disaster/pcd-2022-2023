package pcd.ass02.common;

import java.io.BufferedReader;

public class SourceAnalysisLib {

	public static long countLoC_SimpleStrategy(BufferedReader br) {
		return br.lines().count();
	}

	public static long countLoC_MoreRefinedStrategy(BufferedReader br) {

		long nLines = 0;
		try {
			boolean parsingMultipleLinesBlockComment = false;
			while (br.ready()) {
				String line = br.readLine().trim();

				if (!line.equals("")) {

					if (!parsingMultipleLinesBlockComment) {

						// check for block comments
						int indexStartComment = line.indexOf("/*");

						if (indexStartComment == -1) {
							// no block comments

							// check for EoL comments
							indexStartComment = line.indexOf("//");
							if (indexStartComment == -1) {
								// no comments
								nLines++;
							} else {
								// is there code before the comment?
								String before = line.substring(0, indexStartComment).trim();
								if (!before.equals("")) {
									// line with start comment and code
									nLines++;
								}
							}
						} else {
							// a block comment started

							// is there code before the comment?
							String before = line.substring(0, indexStartComment).trim();
							if (!before.equals("")) {
								// line with start comment and code
								nLines++;
							}

							// check if the block comment ends with within the line
							int indexEndComment = line.indexOf("*/");
							if (indexEndComment == -1) {

								// block comment started, multiple lines

								parsingMultipleLinesBlockComment = true;
							}

						}
					} else {
						// skipping all lines

						if (line.indexOf("*/") != -1) {
							parsingMultipleLinesBlockComment = false;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return nLines;
	}

	public static long countLoC_MoreRefinedStrategy(String src) {
		
		long nLines = 0;
		boolean parsingMultipleLinesBlockComment = false;
			
		var it = src.lines().iterator();
		while (it.hasNext()) {
			String line = it.next().trim();				
			if (!line.equals("")) {
				if (!parsingMultipleLinesBlockComment) {
					// check for block comments
					int indexStartComment = line.indexOf("/*");
						
					if (indexStartComment == -1 ) {
						// no block comments
							
						// check for EoL comments
						indexStartComment = line.indexOf("//");
						if (indexStartComment == -1) {
								// no comments
							nLines++;
						} else {
							// is there code before the comment?
							String before = line.substring(0, indexStartComment).trim();
							if (!before.equals("")) {
								// line with start comment and code
								nLines++;
							}							
						}
					} else {
						// a block comment started
							
						// is there code before the comment?
						String before = line.substring(0, indexStartComment).trim();
						if (!before.equals("")) {
							// line with start comment and code
							nLines++;
						}
							
						// check if the block comment ends with within the line
						int indexEndComment = line.indexOf("*/");
						if (indexEndComment == -1) {
								
							// block comment started, multiple lines
								
							parsingMultipleLinesBlockComment = true;
						}
	 
					}
				} else {
						// skipping all lines
						
					if (line.indexOf("*/") != -1) {
						parsingMultipleLinesBlockComment = false;
					}
				}
			}
		}
		
		return nLines;		
	}	
	
}
