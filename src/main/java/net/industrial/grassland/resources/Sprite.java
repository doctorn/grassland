package net.industrial.grassland.resources;

import org.lwjgl.util.vector.Vector2f;

public class Sprite {
    private int width, height;
    private int rWidth, rHeight;
    private int coordX, coordY;
    private SpriteSheet sheet;

    public Sprite(SpriteSheet sheet, int coordX, int coordY, 
            int width, int height) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.sheet = sheet;
        this.width = width;
        this.height = height;
        this.rWidth = width;
        this.rHeight = height;
    }

    public Sprite(Sprite other) {
        coordX = other.coordX;
        coordY = other.coordY;
        sheet = other.sheet;
        width = other.width;
        height = other.height;
        rWidth = other.rHeight;
        rHeight = other.rHeight;
    }

    public Vector2f getStartVector() {
        return new Vector2f((float) coordX * width / (float) sheet.getWidth(),
                (float) coordY * height / (float) sheet.getHeight());
    }

    public Vector2f getSizeVector() {
        return new Vector2f((float) width / (float) sheet.getWidth(),
                (float) height / (float) sheet.getHeight());
    }
    
    public Sprite scale(float sf) {
        Sprite scaled = new Sprite(this);
        scaled.rWidth = Math.round(sf * rWidth); 
        scaled.rHeight = Math.round(sf * rHeight);
        return scaled;
    }

    public int getWidth() {
        return rWidth;
    }

    public int getHeight() {
        return rHeight;
    }

    public int getID() {
        return sheet.getID();
    }
}
