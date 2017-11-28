package net.industrial.grassland.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.industrial.grassland.Game;
import net.industrial.grassland.resources.Font;
import net.industrial.grassland.resources.Sprite;
import net.industrial.grassland.scene.Camera;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.util.glu.GLU.*;

public class Graphics {
    private List<Quad> quads = new ArrayList<>(),
            orthoQuads = new ArrayList<>(),
            alphaQuads = new ArrayList<>();
    private Game game;
    private static final int[] LIGHTS = {
        GL_LIGHT0, GL_LIGHT1, GL_LIGHT2, GL_LIGHT3, 
        GL_LIGHT4, GL_LIGHT5, GL_LIGHT6, GL_LIGHT7 
    };

    public Graphics(Game game) {
        this.game = game; 
        
        glEnable(GL_CULL_FACE); 
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        quads = new ArrayList<Quad>();
        orthoQuads = new ArrayList<Quad>();
        alphaQuads = new ArrayList<Quad>();
    }

    public void setBackgroundColour(float r, float g, float b) {
        glClearColor(r, g, b, 1f);    
    }

    public void drawCuboid(Vector3f position, 
            float dX, float dY, float dZ) {
        quads.add(new Quad(
                new Vector3f(position.x + dX / 2, position.y, position.z),
                new Vector3f(1f, 0, 0), new Vector3f(0, 1f, 0), 
                dY, dZ, true, null, null, null, game.currentState().getCamera()));
        quads.add(new Quad(
                new Vector3f(position.x - dX / 2, position.y, position.z),
                new Vector3f(1f, 0, 0), new Vector3f(0, 1f, 0), 
                dY, dZ, true, null, null, null, game.currentState().getCamera()));
        quads.add(new Quad(
                new Vector3f(position.x, position.y, position.z - dZ / 2),
                new Vector3f(0, 0, 1f), new Vector3f(1f, 0, 0), 
                dX, dY, true, null, null, null, game.currentState().getCamera()));
        quads.add(new Quad(
                new Vector3f(position.x, position.y, position.z + dZ / 2),
                new Vector3f(0, 0, 1f), new Vector3f(1f, 0, 0), 
                dX, dY, true, null, null, null, game.currentState().getCamera()));
    }
    
    public void fillQuad(Vector3f p, Vector3f n, Vector3f a, 
            float l, float w, Sprite sprite) {
        if (!sprite.hasAlpha()) quads.add(new Quad(p, n, a, l, w, false, 
                sprite, sprite.getStartVector(), sprite.getSizeVector(),
                game.currentState().getCamera()));
        else alphaQuads.add(new Quad(p, n, a, l, w, false, 
                sprite, sprite.getStartVector(), sprite.getSizeVector(),
                game.currentState().getCamera()));
    } 
 
    public void drawImage(Sprite sprite, int x, int y) {
        Vector3f position = 
            new Vector3f(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, 0);
        orthoQuads.add(new Quad(position, 
                new Vector3f(0, 0, -1f),
                new Vector3f(1f, 0, 0),
                sprite.getWidth(), sprite.getHeight(), false,
                sprite, sprite.getStartVector(), sprite.getSizeVector(),
                game.currentState().getCamera()));
    }

    public void drawString(Font font, String text, int x, int y) {
        for (int i = 0; i < text.length(); i++)
            drawImage(font.getCharacter(text.charAt(i)), 
                    x + i * font.getCharacterWidth(), y);
    }

    public void render() {
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glColor4f(1f, 1f, 1f, 1f); 
        float aspect = ((float) game.getWidth()) / ((float) game.getHeight());
        if (game.currentState().perspectiveEnabled()) {
            gluPerspective(45.0f, aspect, 0.1f, 100.0f);
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        } else glOrtho(- aspect / 2f, aspect / 2f, -0.5f, 0.5f, 0.1f, 100f);
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        if (game.currentState().getCamera() != null)
            game.currentState().getCamera().look();
        
        for (int i = 0; i < 8 && i < game.currentState().getLights().size(); i++)
            game.currentState().getLights().get(i).render(LIGHTS[i]); 
     
        Iterator<Quad> it = quads.iterator();
        while (it.hasNext()) {
            Quad quad = it.next();
            if (quad.getDistance() < game.currentState().getRenderDistance()) 
                quad.render();
        }
        
        Collections.sort(alphaQuads);
        it = alphaQuads.iterator();
        while (it.hasNext()) {
            Quad quad = it.next();
            if (quad.getDistance() < game.currentState().getRenderDistance())
                quad.render();
        }
     
        glLoadIdentity();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING); 
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, game.getWidth(), game.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
     
        it = orthoQuads.iterator();
        while (it.hasNext()) it.next().render();
        
        glColor4f(0f, 0f, 0f, game.getTransitionAlpha());
        glBegin(GL_QUADS);
        glVertex3f(0, game.getHeight(), 0);
        glVertex3f(game.getWidth(), game.getHeight(), 0);
        glVertex3f(game.getWidth(), 0, 0);
        glVertex3f(0, 0, 0);
        glEnd();
    }
}
