package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
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
    private boolean inOrtho = false;

    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public abstract void initStates();

    public void init() 
            throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setTitle(title);
        Display.create();
        
        glEnable(GL_CULL_FACE); 
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glShadeModel(GL_SMOOTH);
     
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45.0f, ((float) width) / ((float) height), 0.1f, 100.0f);
        
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
     
        states = new ArrayList<GameState>();
        initStates();

        for (GameState state : states) state.init(this);
        loop(); 
    }

    public void loop() 
            throws LWJGLException {
        long lastLoop = System.currentTimeMillis();
        do {
            int delta = (int) (System.currentTimeMillis() - lastLoop);
            lastLoop = System.currentTimeMillis();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            int remainder = delta % 10;
            int step = delta / 10;
            for (int i = 0; i < step; i++) currentState.update(this, 10); 
            if (remainder != 0) currentState.update(this, remainder); 
            currentState.render(this, 10);
            
            Display.update();
        } while (!Display.isCloseRequested());
        Display.destroy();
    }

    public void enterOrtho() {
        if (!inOrtho) {
            glPushAttrib(GL_DEPTH_BUFFER_BIT | GL_ENABLE_BIT);
            glPushMatrix();
            glLoadIdentity();
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
         
            glLoadIdentity();
            glOrtho(0, width, height, 0, -1, 1);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
            inOrtho = true;
        }
    } 

    public void leaveOrtho() {
        if (inOrtho) { 
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);
            glPopMatrix();
            glPopAttrib();
            inOrtho = false;
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
}
