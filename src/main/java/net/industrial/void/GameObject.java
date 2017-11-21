package net.industrial.src.GameObject;

import org.lwjgl.LWJGLException;

import net.industrial.src.Game;
import net.industrial.src.World;

public abstract class GameObject {
    private World world; 
    private float x, y, z;

    public GameObject(World world) {
        this.world = world;    
    }

    public void update(Game game, int delta) throws LWJGLException {
        //TODO
        updateImpl(game, delta);
    }

    public abstract void updateImpl(Game game, int delta) throws LWJGLException;
    public abstract void render(Game game, int delta) throws LWJGLException;
}
