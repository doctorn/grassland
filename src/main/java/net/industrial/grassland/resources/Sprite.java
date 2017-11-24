package net.industrial.grassland.resources;

import org.lwjgl.util.vector.Vector2f;

public class Sprite {
    private int width, height;
    private int coordX, coordY;
    private SpriteSheet sheet;

    public Sprite(SpriteSheet sheet, int coordX, int coordY, 
            int width, int height) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.sheet = sheet;
        this.width = width;
        this.height = height;
    }

    public Vector2f getStartVector() {
        return new Vector2f((float) coordX * width / (float) sheet.getWidth(),
                (float) coordY * height / (float) sheet.getHeight());
    }

    public Vector2f getSizeVector() {
        return new Vector2f((float) width / (float) sheet.getWidth(),
                (float) height / (float) sheet.getHeight());
    }

    public int getID() {
        return sheet.getID();
    }
}
