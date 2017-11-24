package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
import net.industrial.grassland.GrasslandException;
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

    public abstract void init(Game game) throws GrasslandException;
   
    public void updateDefault(Game game, int delta) 
            throws LWJGLException {
        for (Camera camera : cameras) camera.update(game, delta); 
        for (Light light : lights) light.update(game, delta);
        update(game, delta);
    }
    
    public abstract void update(Game game, int delta) throws GrasslandException;
    
    public void renderDefault(Game game, int delta) 
            throws LWJGLException {
        active.look();
        if (debug) {
            for (Light light : lights) light.renderDebug(game, delta);
            for (Camera camera : cameras) camera.renderDebug(game, delta); 
        }
        render(game, delta);
    }

    public abstract void render(Game game, int delta) throws GrasslandException;
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

    public void addCamera(Camera camera) {
        cameras.add(camera);
    }

    public void activateCamera(Camera camera) {
        active = camera;
    }

    public void setLight(int lightNumber, Light light) {
        boolean found = false; 
        for (int i = 0; i < lights.size(); i++) {
            if (lights.get(i).getNumber() == lightNumber) {
                lights.set(i, light);
                found = true;
            }
        }
        
        if (found) addLight(light);
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
