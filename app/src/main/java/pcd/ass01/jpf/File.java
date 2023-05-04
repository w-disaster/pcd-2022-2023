package pcd.ass01.jpf;

public class File {

	private String name;
	private int nLoC;
	
	public File(String name, int nLoc) {
		this.name = name;
		this.nLoC = nLoc;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNLoC() {
		return nLoC;
	}
}
