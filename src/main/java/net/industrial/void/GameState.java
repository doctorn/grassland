package net.industrial.src;

import org.lwjgl.LWJGLException;

public abstract class GameState {
    public abstract void init(Game game) throws LWJGLException;
    public abstract void update(Game game, int delta) throws LWJGLException;
    public abstract void render(Game game, int delta) throws LWJGLException;
    public abstract int getId();
}
