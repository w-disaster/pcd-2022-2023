package pcd.ass01.part1;

/**
 * A typical example of class with comments following the rules
 * @author aricci
 *
 */
public class TestClassForParsingComments {

	int a; /* single line */
	
	int b; // single line
	
	/* 
	 * multiple 
	 * line 
	 * comments
	 * 
	 */
	
	/**
	 *  comments for a method
	 */
	void m() {
		if (a > 0) { // end of line comment
			
			a = 0; 
			
		} else {
			
			a = 1; /* single line */
			
		}
		
	}
	
}
