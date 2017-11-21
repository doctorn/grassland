package net.industrial.grassland;

import org.lwjgl.LWJGLException;

public abstract class GameObject {
    public void update(Game game, int delta) throws LWJGLException {
        //TODO
        updateImpl(game, delta);
    }

    public abstract void updateImpl(Game game, int delta) throws LWJGLException;
    public abstract void render(Game game, int delta) throws LWJGLException;
}
