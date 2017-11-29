package net.industrial.grassland;

import org.lwjgl.LWJGLException;

import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.graphics.Graphics;
import net.industrial.grassland.graphics.Vector3f;

public abstract class GameObject {
    private boolean willDie = false;
    private Vector3f position = new Vector3f();
    private float width, height, depth;
    
    public abstract void update(Game game, int delta) 
            throws GrasslandException;
    public abstract void render(Game game, Graphics graphics) 
            throws GrasslandException;
    
    public void renderDebug(Game game, Graphics graphics) 
            throws GrasslandException {
        graphics.drawCuboid(position, width, height, depth);
    }
    
    public void setSize(float width, float height, float depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
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
    
    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void kill() {
        willDie = true;
    }

    public boolean willDie() {
        return willDie;
    }
}
