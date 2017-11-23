package net.industrial.grassland;

import org.lwjgl.LWJGLException;

import org.lwjgl.util.vector.Vector3f;

public abstract class GameObject {
    private boolean willDie = false;
    private float x, y, z;

    public void update(Game game, int delta) throws LWJGLException {
        //TODO
        updateImpl(game, delta);
    }

    public abstract void updateImpl(Game game, int delta) throws LWJGLException;
    public abstract void render(Game game, int delta) throws LWJGLException;
    public abstract void renderDebug(Game game, int delta) throws LWJGLException;
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void kill() {
        willDie = true;
    }

    public boolean willDie() {
        return willDie;
    }
}
