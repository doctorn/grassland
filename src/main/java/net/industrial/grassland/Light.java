package net.industrial.grassland;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class Light {
    private boolean tracking = false;
    private GameObject tracked;
    private int lightNumber;
    private Vector3f position = new Vector3f();

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
        setPosition(new Vector3f(x, y, z)); 
        
        FloatBuffer colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(g).put(1f);
        colour.flip();
        glLight(lightNumber, GL_DIFFUSE, colour);

        FloatBuffer attenuation = BufferUtils.createFloatBuffer(4);
        attenuation.put(1.0f).put(0).put(0).put(0).flip();
        glLight(lightNumber, GL_QUADRATIC_ATTENUATION, attenuation);
        
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
        FloatBuffer attenuation = BufferUtils.createFloatBuffer(4);
        attenuation.put(1.0f).put(0).put(0).put(0).flip();
        glLight(lightNumber, GL_QUADRATIC_ATTENUATION, attenuation);
     
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

    public void setPosition(Vector3f position) {
        this.position = position; 
     
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(position.x).put(position.y).put(position.z).put(1f);
        buffer.flip();
        glLight(lightNumber, GL_POSITION, buffer);
    }

    public void update(Game game, int delta) {
        if (tracking && !tracked.willDie()) {
            setPosition(tracked.getPosition());
        }
    }

    public void renderDebug(Game game, int delta) {
        RenderUtils.drawCuboid(position, 0.05f, 0.05f, 0.05f);
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public int getNumber() {
        return lightNumber;
    }
}
