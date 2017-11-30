package net.industrial.grassland.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.industrial.grassland.Game;
import net.industrial.grassland.graphics.Vector2f;
import net.industrial.grassland.graphics.Vector3f;
import net.industrial.grassland.resources.Font;
import net.industrial.grassland.resources.Sprite;
import net.industrial.grassland.scene.Camera;
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

    public void drawLine(Vector3f start, Vector3f end) {
        float length = end.sub(start).length();
        float width = 0.0001f;
        Vector3f axis = start.sub(end).normalise();
        float z = - axis.dot(new Vector3f(1f, 1f, 0f)) / axis.z;
        Vector3f normal = (new Vector3f(1f, 1f, z)); 
        Vector3f position = start.add(end.sub(start).scale(0.5f));
        quads.add(new Quad(position, normal, axis, length, width, true,
                null, null, null, game.currentState().getCamera()));
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
   
    public void fillCuboid(Vector3f p, Vector3f n, Vector3f a,
            float l, float w, float d,
            Sprite sprite) {
        fillCuboid(p, n, a, l, w, d, sprite, sprite, sprite);
    } 

    public void fillCuboid(Vector3f p, Vector3f n, Vector3f a,
            float l, float w, float d,
            Sprite top, Sprite side, Sprite bottom) {
        fillCuboid(p, n, a, l, w, d, top, side, side, side, side, bottom);
    }

    public void fillCuboid(Vector3f p, Vector3f n, Vector3f a, 
            float l, float w, float d, 
            Sprite top, Sprite front, Sprite right, 
            Sprite back, Sprite left, Sprite bottom) {
        n = n.normalise();
        a = a.normalise();
        Vector3f u = n.cross(a);
        fillQuad(p.add(n.scale(d / 2f)), n, a, l, w, front);
        fillQuad(p.sub(n.scale(d / 2f)), n.scale(-1f), a.scale(-1f), l, w, back);
        fillQuad(p.add(a.scale(l / 2f)), a, n.scale(-1f), w, d, right);
        fillQuad(p.sub(a.scale(l / 2f)), a.scale(-1f), n, w, d, left);
        fillQuad(p.add(u.scale(w / 2f)), u, a.scale(-1f), l, d, top);
        fillQuad(p.sub(u.scale(w / 2f)), u.scale(-1f), a, l, d, bottom);
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

    public void initMatrices() {
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glColor4f(1f, 1f, 1f, 1f); 
        float aspect = ((float) game.getWidth()) / ((float) game.getHeight());
        if (game.currentState().perspectiveEnabled()) {
            gluPerspective(45f, aspect, 0.1f, 100.0f);
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        } else glOrtho(- aspect / 2f, aspect / 2f, -0.5f, 0.5f, -100f, 100f);
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        if (game.currentState().getCamera() != null)
            game.currentState().getCamera().look();
    }

    public void render() {
        initMatrices(); 
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
