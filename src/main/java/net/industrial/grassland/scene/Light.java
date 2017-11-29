package net.industrial.grassland.scene;

import java.nio.FloatBuffer;
import net.industrial.grassland.Game;
import net.industrial.grassland.GameObject;
import net.industrial.grassland.graphics.Graphics;
import net.industrial.grassland.graphics.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

public class Light implements Comparable<Light> {
    private boolean tracking = false;
    private float distance;
    private boolean ambient = false;
    private FloatBuffer colour, attenuation;

    private GameObject tracked;
    private Vector3f position = new Vector3f();

    public Light(float x, float y, float z,
            float r, float g, float b) {
        setPosition(new Vector3f(x, y, z)); 
        
        colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(g).put(1f);
        colour.flip();
        
        setAttenuation(1.0f);
    }
    
    public Light(GameObject tracked,
            float r, float g, float b) {
        tracking = true;
        this.tracked = tracked;
        
        colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(b).put(1f);
        colour.flip();
        
        setAttenuation(1.0f); 
    }

    public Light(float r, float g, float b, float a) {
        colour = BufferUtils.createFloatBuffer(4);
        colour.put(r).put(g).put(b).put(a);
        colour.flip();
        ambient = true;
    }

    public void update(Game game, int delta) {
        Camera camera = game.currentState().getCamera(); 
        if (camera != null && !ambient) {
            Vector3f d = position.sub(camera.getPosition());
            distance = d.sub(camera.getPosition()).length();
        } else distance = 0;
     
        if (tracking && !tracked.willDie()) {
            setPosition(tracked.getPosition());
        }
    }

    public void render(int lightNumber) {
        if (!ambient) {
            glLight(lightNumber, GL_DIFFUSE, colour);
            glLight(lightNumber, GL_QUADRATIC_ATTENUATION, attenuation);
        } else {
            glLight(lightNumber, GL_AMBIENT, colour);
            FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
            diffuse.put(0f).put(0f).put(0f).put(0f);
            diffuse.flip();
            glLight(lightNumber, GL_DIFFUSE, diffuse);
        }
        
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(position.x).put(position.y).put(position.z).put(1f);
        buffer.flip();
        glLight(lightNumber, GL_POSITION, buffer);
     
        glEnable(lightNumber);
    }

    public void renderDebug(Game game, Graphics graphics) {
        graphics.drawCuboid(position, 0.05f, 0.05f, 0.05f);
    }
    
    public void setAttenuation(float a) {
        attenuation = BufferUtils.createFloatBuffer(4);
        attenuation.put(a).put(0).put(0).put(0).flip();
    }

    public void setPosition(Vector3f position) {
        this.position = position; 
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

    @Override
    public int compareTo(Light other) {
        if (distance - other.distance > 0) return 1;
        else if (distance - other.distance < 0) return -1;
        else return 0;
    }
}
