package net.industrial.grassland;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

public class Light {
    private boolean tracking = false;
    private GameObject tracked;
    private int lightNumber;
    private float x, y, z;

    public static final int LIGHT_0 = GL_LIGHT0,
            LIGHT_1 = GL_LIGHT1,
            LIGHT_2 = GL_LIGHT2,
            LIGHT_3 = GL_LIGHT3,
            LIGHT_4 = GL_LIGHT4,
            LIGHT_5 = GL_LIGHT5,
            LIGHT_6 = GL_LIGHT6,
            LIGHT_7 = GL_LIGHT7;
   
    public Light(float x, float y, float z,
            float r, float g, float b,
            int lightNumber) {
        this.lightNumber = lightNumber; 
        setPosition(x, y, z); 
        
        FloatBuffer colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(g).put(1f);
        colour.flip();
        glLight(lightNumber, GL_DIFFUSE, colour);
        
        glEnable(lightNumber);
    }
    
    public Light(GameObject tracked,
            float r, float g, float b,
            int lightNumber) {
        tracking = true;
        this.tracked = tracked;
        this.lightNumber = lightNumber; 
        
        FloatBuffer colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(g).put(1f);
        colour.flip();
        glLight(lightNumber, GL_DIFFUSE, colour);
     
        glEnable(lightNumber);
    }

    public Light(float r, float g, float b, float a,
            int lightNumber) {
        FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(r).put(g).put(b).put(a);
        ambient.flip();
        glLight(lightNumber, GL_AMBIENT, ambient);
     
        FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
        diffuse.put(0f).put(0f).put(0f).put(0f);
        diffuse.flip();
        glLight(lightNumber, GL_DIFFUSE, diffuse);
        
        glEnable(lightNumber);
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        
        FloatBuffer position = BufferUtils.createFloatBuffer(4);
        position.put(x).put(y).put(z).put(1f);
        position.flip();
        glLight(lightNumber, GL_POSITION, position);
    }

    public void update(Game game, int delta) {
        if (tracking && !tracked.willDie()) {
            setPosition(tracked.getX(), tracked.getY(), tracked.getZ());
        }
    }

    public void renderDebug(Game game, int delta) {
        RenderUtils.drawCuboid(x, y, z, 0.05f, 0.05f, 0.05f);
    }

    public int getNumber() {
        return lightNumber;
    }
}
