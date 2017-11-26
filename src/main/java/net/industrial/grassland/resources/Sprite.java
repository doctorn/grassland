package net.industrial.grassland.resources;

import org.lwjgl.util.vector.Vector2f;

public class Sprite {
    private int width, height;
    private int coordX, coordY;
    private SpriteSheet sheet;

    public Sprite(SpriteSheet sheet, int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.sheet = sheet;
        this.width = sheet.getSpriteWidth();
        this.height = sheet.getSpriteHeight();
    }

    public Sprite(Sprite other) {
        coordX = other.coordX;
        coordY = other.coordY;
        sheet = other.sheet;
        width = other.width;
        height = other.height;
    }

    public Vector2f getStartVector() {
        return new Vector2f((float) coordX * sheet.getSpriteWidth() / 
                (float) sheet.getWidth(),
                (float) coordY * sheet.getSpriteHeight() / 
                (float) sheet.getHeight());
    }

    public Vector2f getSizeVector() {
        return new Vector2f((float) sheet.getSpriteWidth() / 
                (float) sheet.getWidth(),
                (float) sheet.getSpriteHeight() / 
                (float) sheet.getHeight());
    }
    
    public Sprite scale(float sf) {
        Sprite scaled = new Sprite(this);
        scaled.width = Math.round(sf * width); 
        scaled.height = Math.round(sf * height);
        return scaled;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getID() {
        return sheet.getID();
    }
}
