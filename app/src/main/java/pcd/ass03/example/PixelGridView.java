package pcd.ass03.example;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.*;


public class PixelGridView extends JFrame  implements MouseListener {
    
    private VisualiserPanel panel;
    private PixelGrid grid;
    private int w, h;
    private List<PixelGridEventListener> listeners;
    
    public PixelGridView(PixelGrid grid, int w, int h){
    	this.grid = grid;
    	this.w = w;
    	this.h = h;
        setTitle(".:: PixelArt ::.");
        setSize(w + 20, h + 20);
        setResizable(false);
        panel = new VisualiserPanel(grid, w + 20, h + 20);
        panel.addMouseListener(this);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});
        listeners = new ArrayList<PixelGridEventListener>();
    }
    
    public void refresh(){
        panel.repaint();
    }
        
    public void display() {
    	SwingUtilities.invokeLater(() -> {
    		this.setVisible(true);
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
		for (var l: listeners) {
			l.selectedCell(col, row);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}


    
    public static class VisualiserPanel extends JPanel {
        private PixelGrid grid;
        private int w,h;
        
        public VisualiserPanel(PixelGrid grid, int w, int h){
            setSize(w,h);
            this.grid = grid;
            this.w = w;
            this.h = h;
        }

        public void paint(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    		          RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
    		          RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
	            if (grid!=null){
	            	
	            	int dx = w / grid.getNumColumns();
	            	int dy = h / grid.getNumRows();
	            			
	            	for (int i = 0; i < grid.getNumRows(); i++) {
	            		int y = i*dy;
	            		g2.drawLine(0, y, w, y);
	            	}
	            	
	            	for (int i = 0; i < grid.getNumColumns(); i++) {
	            		int x = i*dx;
	            		g2.drawLine(x, 0, x, h);
	            	}
	            	
	            	for (int i = 0; i < grid.getNumRows(); i++) {
	            		int y = i*dy;
		            	for (int j = 0; j < grid.getNumColumns(); j++) {
		            		int x = j*dx;
		            		int c = grid.get(j, i);
		            		if (c != 0) {
		            			g2.fillRect(x+2, y+2, dx - 4, dy - 4);
		            		}
		            	}
	            	}
	            	
	            }
        }

    }
	
}
