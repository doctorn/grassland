package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
import net.industrial.grassland.GrasslandException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public abstract class Game {
    private List<GameState> states;
    private GameState currentState;
    private int width, height;
    private String title;
    private boolean inOrtho = false, fullscreen = false;

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
            
            glEnable(GL_CULL_FACE); 
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
         
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(45.0f, 
                    ((float) width) / ((float) height), 
                    0.1f, 
                    100.0f);
            
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
         
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
                
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                
                int remainder = delta % 10;
                int step = delta / 10;
                for (int i = 0; i < step; i++) 
                    currentState.updateDefault(this, 10); 
                if (remainder != 0) 
                    currentState.updateDefault(this, remainder); 
               
                glPushMatrix(); 
                glScalef(1.0f, 1.0f, 1.0f);
                currentState.renderDefault(this, 10);
                glPopMatrix();
                
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
}
