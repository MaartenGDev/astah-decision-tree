package me.maartendev.seeders;

import java.awt.*;

public class ColorSeeder {
    private Color[] colors;
    private int colorIndex = 0;

    public ColorSeeder(){
        this.colors = new Color[]{
            Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.PINK, Color.MAGENTA
        };
    }

    public Color getNextColor(){
        Color color = this.colors[colorIndex];
        this.colorIndex++;
        return color;
    }

    public String getAsHexColor(Color color){
        return "#"+Integer.toHexString(color.getRGB()).substring(2);
    }
}