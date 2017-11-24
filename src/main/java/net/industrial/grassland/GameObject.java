package net.industrial.grassland;

import org.lwjgl.LWJGLException;

import org.lwjgl.util.vector.Vector3f;

import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.RenderUtils;

public abstract class GameObject {
    private boolean willDie = false;
    private Vector3f position = new Vector3f();
    private float width, height, depth;

    public void update(Game game, int delta) throws GrasslandException {
        //TODO
        updateImpl(game, delta);
    }
    
    public abstract void updateImpl(Game game, int delta) throws GrasslandException;
    public abstract void render(Game game, int delta) throws GrasslandException;
    
    public void renderDebug(Game game, int delta) 
            throws GrasslandException {
        RenderUtils.drawCuboid(position, width, height, depth);
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
