package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.audio.AudioMaster;
import net.industrial.grassland.graphics.Graphics;
import net.industrial.grassland.graphics.Vector3f;
import net.industrial.grassland.scene.Camera;

public abstract class Game {
    private List<GameState> states;
    private GameState currentState, targetState;
    private boolean changingState = false;
    private float transitionCooldown = 0f;

    private int width, height;
    private String title;
    private boolean fullscreen = false;

    private Graphics graphics;
    private int fps = 0, frameCount = 0, cumulativeDelta = 0;

    private Input input;
    
    public Game(String title, int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.fullscreen = fullscreen;
    }

    public abstract void initStates();

    public void init() 
            throws GrasslandException {
        try {
            if (!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
            else Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            Display.create();
         
            AudioMaster.init();
            graphics = new Graphics(this);
            input = new Input();
            states = new ArrayList<GameState>();
            initStates();
         
            for (GameState state : states) state.init(this);
            loop();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }

    private void loop() 
            throws GrasslandException {
        try { 
            long lastLoop = System.currentTimeMillis();
            do {
                int delta = (int) (System.currentTimeMillis() - lastLoop);
                lastLoop = System.currentTimeMillis();
                
                frameCount++;
                cumulativeDelta += delta;
                if (frameCount == 10) {
                    frameCount = 0;
                    fps = 10000 / cumulativeDelta;
                    cumulativeDelta = 0;
                }
                
                if (changingState) {
                    transitionCooldown += 0.002f * delta;
                    if (transitionCooldown > 1f) {
                        transitionCooldown = 1f;
                        changingState = false;
                        currentState = targetState;
                    }
                } else if (transitionCooldown > 0f) {
                    transitionCooldown -= 0.002f * delta;
                    if (transitionCooldown < 0f) 
                        transitionCooldown = 0f;
                }
             
                int remainder = delta % 10;
                int step = delta / 10;
                for (int i = 0; i < step; i++) update(10); 
                if (remainder != 0) update(remainder); 
              
                graphics.clear();
                currentState.renderDefault(this, graphics);
                graphics.render();
                Display.update();
            } while (!Display.isCloseRequested());
            AudioMaster.cleanUp();
            Display.destroy();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }

    private void update(int delta) 
            throws GrasslandException {
        input.update();
        Camera camera = currentState.getCamera();
        if (camera != null) AudioMaster.setListenerData(camera.getPosition());
        else AudioMaster.setListenerData(new Vector3f());
        currentState.updateDefault(this, delta);
    }

    public void enterState(int newState) {
        if (currentState != null) {
            changingState = true;
            for (GameState state : states) {
                if (state.getId() == newState) targetState = state;
            }
        } else {
            for (GameState state : states) {
                if (state.getId() == newState) currentState = state;
            }
        }
    }

    public void addState(GameState state) {
        states.add(state);
    }

    public int getWidth() {
        return Display.getWidth();
    }
   
    public int getHeight() {
        return Display.getHeight();
    }

    public GameState currentState() {
        return currentState;
    }

    public int getFPS() {
        return fps;
    }

    public float getTransitionAlpha() {
        return transitionCooldown;
    }

    public Input getInput() {
        return input;
    }
}
