package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.graphics.Graphics;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class Game {
    private List<GameState> states;
    private GameState currentState;
    private int width, height;
    private String title;
    private boolean fullscreen = false;
    private Graphics graphics;
    private int fps = 0, frameCount = 0, cumulativeDelta = 0;

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
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(title);
            Display.setFullscreen(fullscreen);
            Display.create();
            
            graphics = new Graphics(this);
            states = new ArrayList<GameState>();
            initStates();
         
            for (GameState state : states) state.init(this);
            loop();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }

    public void loop() 
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
             
                int remainder = delta % 10;
                int step = delta / 10;
                for (int i = 0; i < step; i++) 
                    currentState.updateDefault(this, 10); 
                if (remainder != 0) 
                    currentState.updateDefault(this, remainder); 
               
                graphics.clear();
                currentState.renderDefault(this, graphics);
                graphics.render();
                Display.update();
            } while (!Display.isCloseRequested());
            Display.destroy();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }

    public void enterState(int newState) {
        for (GameState state : states) {
            if (state.getId() == newState) currentState = state;
        }
    }

    public void addState(GameState state) {
        states.add(state);
    }

    public int getWidth() {
        return width;
    }
   
    public int getHeight() {
        return height;
    }

    public GameState currentState() {
        return currentState;
    }

    public int getFPS() {
        return fps;
    }
}
