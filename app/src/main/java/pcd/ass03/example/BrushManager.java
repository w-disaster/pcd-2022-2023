package pcd.ass03.example;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.awt.Toolkit;

public class BrushManager {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 3;
    private List<Brush> brushes = new java.util.ArrayList<>();

    void draw(final Graphics2D g) {
        brushes.forEach(brush -> {
            g.setColor(new java.awt.Color(brush.color));
            g.setStroke(new BasicStroke(STROKE_SIZE));
            // create a cross
            g.drawLine(brush.x - BRUSH_SIZE, brush.y, brush.x + BRUSH_SIZE, brush.y);
            g.drawLine(brush.x, brush.y - BRUSH_SIZE, brush.x, brush.y + BRUSH_SIZE);

            //g.fillRect(brush.x, brush.y, 10, 10);
        });
    }

    void addBrush(final Brush brush) {
        brushes.add(brush);
    }

    void removeBrush(final Brush brush) {
        brushes.remove(brush);
    }

    public static class Brush {
        int x, y;
        int color;

        public Brush(final int x, final int y, final int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void updatePosition(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }
}
