package pcd.ass03.example;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;


public class PixelGridView extends JFrame implements MouseListener {
    private final VisualiserPanel panel;
    private final PixelGrid grid;
    private final int w, h;
    private final List<PixelGridEventListener> listeners;
    
    public PixelGridView(PixelGrid grid, int w, int h){
		this.grid = grid;
		this.w = w;
		this.h = h;
        setTitle(".:: PixelArt ::.");
		setResizable(false);
        panel = new VisualiserPanel(grid, w, h);
        panel.addMouseListener(this);
        getContentPane().add(panel);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        listeners = new ArrayList<>();
    }
    
    public void refresh(){
        panel.repaint();
    }
        
    public void display() {
		SwingUtilities.invokeLater(() -> {
			this.getContentPane().setPreferredSize(new Dimension(w, h));
			this.setVisible(true);
			this.pack();
		});
    }
    
    public void addPixelGridEventListener(PixelGridEventListener l) {
		listeners.add(l);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		int dx = w / grid.getNumColumns();
		int dy = h / grid.getNumRows();
		int col = e.getX() / dx;
		int row = e.getY() / dy;
		listeners.forEach(l -> l.selectedCell(col, row));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
