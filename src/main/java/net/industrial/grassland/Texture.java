package net.industrial.grassland;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {
    private int id;

    public Texture(String file) 
            throws GrasslandException {
        ByteBuffer buffer = null;
        
        try {
            BufferedImage image = ImageIO.read(new File(file));
            
            int width = image.getWidth();
            int height = image.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int argb = image.getRGB(i, j);
                    buffer.put((i + j * width) * 4 + 0, (byte) ((argb >> 16) & 0xFF));
                    buffer.put((i + j * width) * 4 + 1, (byte) ((argb >> 8) & 0xFF));
                    buffer.put((i + j * width) * 4 + 2, (byte) ((argb >> 0) & 0xFF));
                    buffer.put((i + j * width) * 4 + 3, (byte) ((argb >> 24) & 0xFF));
                }
            }
            buffer.flip();
            buffer.limit(buffer.capacity());
            
            id = glGenTextures();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, id);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 
                    0, 
                    GL_RGB, 
                    width, 
                    height, 
                    0, 
                    GL_RGBA, 
                    GL_UNSIGNED_BYTE, 
                    buffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
         
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (IOException e) {
            throw new GrasslandException();
        } 
    }

    public int getID() {
        return id;
    }
}
