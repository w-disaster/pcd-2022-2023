package pcd.ass03.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class PixelArtMain {
	public static int randomColor() {
		Random rand = new Random();
		return rand.nextInt(256 * 256 * 256);
	}

	public static void main(String[] args) {
		var brushManager = new BrushManager();
		var localBrush = new BrushManager.Brush(0, 0, randomColor());
		brushManager.addBrush(localBrush);
		PixelGrid grid = new PixelGrid(40,40);

		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
		}

		PixelGridView view = new PixelGridView(grid, brushManager, 800, 800);
		view.addMouseMovedListener((x, y) -> {
			localBrush.updatePosition(x, y);
			view.refresh();
		});
		view.addPixelGridEventListener((x, y) -> {
			System.out.println("=> " + x + " " + y);
			grid.set(x, y, localBrush.color);
			view.refresh();
		});
		view.display();
	}

}
