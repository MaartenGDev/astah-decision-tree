package me.maartendev.seeders;

import java.awt.*;

public class ColorSeeder {
    private Color[] colors;
    private int colorIndex = 0;

    public ColorSeeder() {
        this.colors = new Color[]{
                new Color(255, 0, 0),
                new Color(128, 0, 0),
                new Color(255, 255, 0),
                new Color(128, 128, 0),
                new Color(0, 255, 0),
                new Color(0, 128, 0),
                new Color(0, 255, 255),
                new Color(0, 128, 128),
                new Color(0, 0, 255),
                new Color(0, 0, 128),
                new Color(255, 0, 255),
                new Color(128, 0, 128),
                new Color(128, 128, 128)
        };
    }

    public Color getNext() {
        Color color = this.colors[colorIndex];
        this.colorIndex++;
        return color;
    }

    public String getAsHexColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public void reset() {
        this.colorIndex = 0;
    }
}
