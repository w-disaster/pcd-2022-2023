package pcd.ass03;

import java.util.Random;

public class PixelArtMain {

	public static void main(String[] args) {

		PixelGrid grid = new PixelGrid(40,40);

		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			grid.set(rand.nextInt(40), rand.nextInt(40), 1);
		}

		PixelGridView view = new PixelGridView(grid, 800, 800);
		
		view.addPixelGridEventListener((x, y) -> {
			System.out.println("=> " + x + " " + y);
			grid.set(x, y, 1);
			view.refresh();
		});
		
		view.display();
	}

}
