package net.industrial.grassland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.graphics.Graphics;
import net.industrial.grassland.scene.Camera;
import net.industrial.grassland.scene.Light;
import org.lwjgl.LWJGLException;
import static org.lwjgl.opengl.GL11.*;

public abstract class GameState {
    private List<Light> lights = new ArrayList<>();
    private List<Camera> cameras = new ArrayList<>();
    private Camera active;

    private boolean debug = false;
    private boolean lightingEnabled = false;
    
    public GameState() {
        setLighting(false);
    }

    public abstract void init(Game game) 
            throws GrasslandException;
   
    public void updateDefault(Game game, int delta) 
            throws LWJGLException {
        for (Camera camera : cameras) camera.update(game, delta); 
        for (Light light : lights) light.update(game, delta);
        Collections.sort(lights); 
        update(game, delta);
    }
    
    public abstract void update(Game game, int delta) 
            throws GrasslandException;
    
    public void renderDefault(Game game, Graphics graphics) 
            throws LWJGLException {
        if (lightingEnabled) glEnable(GL_LIGHTING);
        if (debug) {
            for (Light light : lights) light.renderDebug(game, graphics);
            for (Camera camera : cameras) camera.renderDebug(game, graphics); 
        }
        render(game, graphics);
    }

    public abstract void render(Game game, Graphics graphics) 
            throws GrasslandException;

    public abstract int getId();

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void toggleDebug() {
        debug = !debug;
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public List<Light> getLights() {
        return lights;
    }

    public void addCamera(Camera camera) {
        cameras.add(camera);
    }

    public void activateCamera(Camera camera) {
        active = camera;
    }

    public Camera getCamera() {
        return active;
    }
    
    public void setLighting(boolean lighting) {
        if (lighting) {
            glEnable(GL_LIGHTING);
            glShadeModel(GL_SMOOTH);
        } else {
            glDisable(GL_LIGHTING);
        }
        
        lightingEnabled = lighting;
    }

    public void toggleLighting() {
        if (!lightingEnabled) {
            glEnable(GL_LIGHTING);
            glShadeModel(GL_SMOOTH);
        } else {
            glDisable(GL_LIGHTING);
        }
        
        lightingEnabled = !lightingEnabled;
    }

    public boolean lightEnabled() {
        return lightingEnabled;
    }
}
