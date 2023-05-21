package pcd.ass03.example;

import javax.swing.*;
import java.awt.*;

public class VisualiserPanel extends JPanel {
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
