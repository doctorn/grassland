package net.industrial.grassland;

import org.lwjgl.LWJGLException;

import org.lwjgl.util.vector.Vector3f;

public abstract class GameObject {
    private boolean willDie = false;
    private Vector3f position = new Vector3f();

    public void update(Game game, int delta) throws LWJGLException {
        //TODO
        updateImpl(game, delta);
    }

    public abstract void updateImpl(Game game, int delta) throws LWJGLException;
    public abstract void render(Game game, int delta) throws LWJGLException;
    public abstract void renderDebug(Game game, int delta) throws LWJGLException;
    
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
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void kill() {
        willDie = true;
    }

    public boolean willDie() {
        return willDie;
    }
}
