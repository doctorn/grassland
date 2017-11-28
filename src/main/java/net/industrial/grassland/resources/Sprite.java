package net.industrial.grassland.resources;

import net.industrial.grassland.graphics.Vector2f;

public class Sprite {
    private int width, height;
    private int coordX, coordY;
    private SpriteSheet sheet;
    private boolean hasAlpha;

    public Sprite(SpriteSheet sheet, int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.sheet = sheet;
        width = sheet.getSpriteWidth();
        height = sheet.getSpriteHeight();
        hasAlpha = sheet.hasAlpha();
    }

    public Sprite(Sprite other) {
        coordX = other.coordX;
        coordY = other.coordY;
        sheet = other.sheet;
        width = other.width;
        height = other.height;
        hasAlpha = other.hasAlpha;
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

    public boolean hasAlpha() {
        return hasAlpha;
    }
}
