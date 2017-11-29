package net.industrial.grassland.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.graphics.Vector2f;

public class Sprite {
    private int width, height;
    private int coordX, coordY;
    private SpriteSheet sheet;
    private boolean hasAlpha;
    
    public Sprite(String filename) 
            throws GrasslandException {
        sheet = new SpriteSheet(filename, 0, 0); 
        this.coordX = 0;
        this.coordY = 0;
        sheet.setSpriteWidth(sheet.getWidth());
        sheet.setSpriteHeight(sheet.getHeight());
        width = sheet.getWidth();
        height = sheet.getHeight();
        hasAlpha = sheet.hasAlpha();
    }

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

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public SpriteSheet getSheet() {
        return sheet;
    }

    public int getID() {
        return sheet.getID();
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    public Cursor toCursor(int hotX, int hotY) 
            throws GrasslandException {
        try {
            hotY = height - hotY - 1;
            String file = sheet.getFile();
         
            BufferedImage image = ImageIO.read(new File(file));
            IntBuffer buffer = IntBuffer.allocate(width * height);
            int x = coordX * sheet.getSpriteWidth();
            int y = coordY * sheet.getSpriteHeight();
            float scale = (float) width / sheet.getSpriteWidth() * 
                    (float) sheet.getWidth() / image.getWidth();
         
            for (int i = x; i < x + width; i++) {
                for (int j = y; j < y + height; j++) {
                    int rgb = image.getRGB((int) Math.floor(i / scale), 
                            (int) Math.floor(j / scale));
                    buffer.put(i - x + (y - (j + 1) + height) * width, rgb);
                }
            }
            
            buffer.flip();
            buffer.limit(buffer.capacity());
         
            return new Cursor(width, height, hotX, hotY, 1, buffer, null);
        } catch (IOException e) {
            throw new GrasslandException();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }
}
