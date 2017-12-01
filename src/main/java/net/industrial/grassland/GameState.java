package net.industrial.grassland;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.industrial.grassland.CollidableGameObject;
import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.audio.Sound;
import net.industrial.grassland.graphics.Graphics;
import net.industrial.grassland.graphics.Ray;
import net.industrial.grassland.graphics.Vector3f;
import net.industrial.grassland.scene.Camera;
import net.industrial.grassland.scene.Light;
import org.lwjgl.LWJGLException;
import static org.lwjgl.opengl.GL11.*;

public abstract class GameState {
    private List<Light> lights = new ArrayList<>();
    private List<Camera> cameras = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();
    private Camera active;

    private Sound music;

    private boolean debug = false;
    private boolean lighting = false, perspective = true;
    private float renderDistance = 100f;
    private boolean init = false;

    public GameState() {
        setLighting(false);
    }

    public abstract void init(Game game) 
            throws GrasslandException;
   
    public void updateDefault(Game game, int delta) 
            throws GrasslandException {
        for (Camera camera : cameras) camera.update(game, delta); 
        for (Light light : lights) light.update(game, delta);
        Collections.sort(lights);
     
        if (music != null && active!= null)
            music.setPosition(active.getPosition());
      
        int count = objects.size();
        for (int i = 0; i < count; i++) {
            objects.get(i).update(game, delta); 
        }
     
        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            if (it.next().willDie()) it.remove();
        }
        
        update(game, delta);
    }
    
    public abstract void update(Game game, int delta) 
            throws GrasslandException;
    
    public void renderDefault(Game game, Graphics graphics) 
            throws GrasslandException {
        if (lighting) glEnable(GL_LIGHTING);
        if (debug) {
            for (Light light : lights) light.renderDebug(game, graphics);
            for (Camera camera : cameras) camera.renderDebug(game, graphics); 
            for (GameObject object : objects) object.renderDebug(game, graphics); 
        }
     
        render(game, graphics);
        for (GameObject object : objects) object.render(game, graphics);
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

    public boolean debugEnabled() {
        return debug;
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
        } else glDisable(GL_LIGHTING);
        
        this.lighting = lighting;
    }

    public void toggleLighting() {
        if (!lighting) {
            glEnable(GL_LIGHTING);
            glShadeModel(GL_SMOOTH);
        } else glDisable(GL_LIGHTING);
        
        lighting = !lighting;
    }

    public boolean lightEnabled() {
        return lighting;
    }

    public void setPerspective(boolean perspective) {
        this.perspective = perspective;
    }

    public void togglePerspective() {
        perspective = !perspective;
    }

    public boolean perspectiveEnabled() {
        return perspective;
    }

    public float getRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(float renderDistance) {
        this.renderDistance = renderDistance; 
    }

    public void setMusic(Sound music) {
        this.music = music;
        music.setLooping(true);
        if (active != null) music.setPosition(active.getPosition());
        music.play();
    }

    public void setMusicVolume(float volume) {
        music.setVolume(volume);
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public Ray getMouseRay(Vector3f worldSpaceMouse) {
        if (perspective) {
            Vector3f cameraPosition = new Vector3f();
            if (active != null) cameraPosition = active.getPosition();
            return new Ray(cameraPosition, worldSpaceMouse.sub(cameraPosition));
        } else {
            Vector3f lookVector = new Vector3f(0f, 0f, -1f);
            if (active != null) lookVector = active.lookVector();
            return new Ray(worldSpaceMouse, lookVector);
        }
    }

    public List<GameObject> castRay(Ray r) {
        ArrayList<GameObject> intersections = new ArrayList<>();
        for (GameObject object : objects) {
            if (object instanceof CollidableGameObject && 
                    ((CollidableGameObject) object).intersectsRay(r))
                intersections.add(object);
        }
        Collections.sort(intersections, (a, b) -> {
            float dif = ((CollidableGameObject) a).closestDistance(r) -
                    ((CollidableGameObject) b).closestDistance(r);
            if (dif > 0) return 1;
            else if (dif < 0) return -1;
            else return 0;
        });
        return  intersections;
    }
}
