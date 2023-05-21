package pcd.ass03.example;

public class PixelGrid {

	private int nRows;
	private int nColumns;
	private int[][] grid;
	
	public PixelGrid(int nRows, int nColumns) {
		this.nRows = nRows;
		this.nColumns = nColumns;
		grid = new int[nRows][nColumns];
	}

	public void clear() {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				grid[i][j] = 0;
			}
		}
	}
	
	public void set(int x, int y, int c) {
		grid[y][x] = c;
	}
	
	public int get(int x, int y) {
		return grid[y][x];
	}
	
	public int getNumRows() {
		return this.nRows;
	}
	

	public int getNumColumns() {
		return this.nColumns;
	}
	
}
