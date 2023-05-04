package pcd.ass01.part1;

public class LocEntry {
	
	private String src;
	private int nLoc;
	
	LocEntry(String src, int nLoc){
		this.src = src;
		this.nLoc = nLoc;
	}
	
	public String getSrc() {
		return src;
	}
	
	public int getNLoc() {
		return nLoc;
	}
}
