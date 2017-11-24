package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
import net.industrial.grassland.Game;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    public static void drawCuboid(Vector3f position, 
            float dX, float dY, float dZ) {
        boolean lighting = glIsEnabled(GL_LIGHTING); 
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDisable(GL_CULL_FACE);
        
        if (lighting) glDisable(GL_LIGHTING);
        
        float sX = position.x - dX / 2;
        float sY = position.y - dY / 2;
        float sZ = position.z + dZ / 2;
        float mX = position.x + dX / 2;
        float mY = position.y + dY / 2;
        float mZ = position.z - dZ / 2;
     
        glBegin(GL_QUADS);
        
        glVertex3f(sX, sY, sZ);
        glVertex3f(mX, sY, sZ);
        glVertex3f(mX, mY, sZ);
        glVertex3f(sX, mY, sZ);
     
        glVertex3f(sX, sY, mZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(mX, sY, mZ);
      
        glVertex3f(sX, sY, sZ);
        glVertex3f(sX, mY, sZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(sX, sY, mZ);
     
        glVertex3f(mX, sY, sZ);
        glVertex3f(mX, sY, mZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(mX, mY, sZ);
        
        glVertex3f(sX, sY, mZ);
        glVertex3f(mX, sY, mZ);
        glVertex3f(mX, sY, sZ);
        glVertex3f(sX, sY, sZ);
     
        glVertex3f(mX, mY, sZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(sX, mY, sZ);
        
        glEnd();
        
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_CULL_FACE); 
        if (lighting) glEnable(GL_LIGHTING);
    }
    
    private static void quad(Vector3f p, Vector3f n, Vector3f a, float l, float w) {
        Vector3f L = (Vector3f) a.normalise();
        Vector3f N = (Vector3f) n.normalise();
        Vector3f W = (Vector3f) Vector3f.cross(L, N, null).normalise();
        
        glNormal3f(N.x, N.y, N.z);
        glVertex3f(p.x - (l / 2f) * L.x - (w / 2f) * W.x,
                p.y - (l / 2f) * L.y - (w / 2f) * W.y,
                p.z - (l / 2f) * L.z - (w / 2f) * W.z);
     
        glVertex3f(p.x - (l / 2f) * L.x + (w / 2f) * W.x,
                p.y - (l / 2f) * L.y + (w / 2f) * W.y,
                p.z - (l / 2f) * L.z + (w / 2f) * W.z);
     
        glVertex3f(p.x + (l / 2f) * L.x + (w / 2f) * W.x,
                p.y + (l / 2f) * L.y + (w / 2f) * W.y,
                p.z + (l / 2f) * L.z + (w / 2f) * W.z);
     
        glVertex3f(p.x + (l / 2f) * L.x - (w / 2f) * W.x,
                p.y + (l / 2f) * L.y - (w / 2f) * W.y,
                p.z + (l / 2f) * L.z - (w / 2f) * W.z);
    }

    private static void texturedQuad(Vector3f p, Vector3f n, Vector3f a, 
            float l, float w,
            Vector2f tStart, Vector2f tSize) {
        Vector3f L = (Vector3f) a.normalise();
        Vector3f N = (Vector3f) n.normalise();
        Vector3f W = (Vector3f) Vector3f.cross(L, N, null).normalise();
        
        glNormal3f(N.x, N.y, N.z);
        glTexCoord2f(tStart.x, tStart.y); 
        glVertex3f(p.x - (l / 2f) * L.x - (w / 2f) * W.x,
                p.y - (l / 2f) * L.y - (w / 2f) * W.y,
                p.z - (l / 2f) * L.z - (w / 2f) * W.z);
     
        glTexCoord2f(tStart.x, tStart.y + tSize.y); 
        glVertex3f(p.x - (l / 2f) * L.x + (w / 2f) * W.x,
                p.y - (l / 2f) * L.y + (w / 2f) * W.y,
                p.z - (l / 2f) * L.z + (w / 2f) * W.z);
     
        glTexCoord2f(tStart.x + tSize.x, tStart.y + tSize.y); 
        glVertex3f(p.x + (l / 2f) * L.x + (w / 2f) * W.x,
                p.y + (l / 2f) * L.y + (w / 2f) * W.y,
                p.z + (l / 2f) * L.z + (w / 2f) * W.z);
     
        glTexCoord2f(tStart.x + tSize.x, tStart.y); 
        glVertex3f(p.x + (l / 2f) * L.x - (w / 2f) * W.x,
                p.y + (l / 2f) * L.y - (w / 2f) * W.y,
                p.z + (l / 2f) * L.z - (w / 2f) * W.z);
    }
    
    public static void fillQuad(Vector3f p, Vector3f n, Vector3f a, float l, float w) {
        glBegin(GL_QUADS);
        quad(p, n, a, l, w); 
        glEnd();
    }

    public static void fillQuad(Vector3f p, Vector3f n, Vector3f a, float l, float w, Texture texture) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getID());
        glBegin(GL_QUADS);
        texturedQuad(p, n, a, l, w, new Vector2f(0, 0), new Vector2f(1, 1)); 
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0); 
        glDisable(GL_TEXTURE_2D); 
    }

    public static void fillPlane(Vector3f p, Vector3f n, Vector3f a, float l, float w, int c) {
        glBegin(GL_QUADS);
        
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                glNormal3f(n.x, n.y, n.z);
                Vector3f L = (Vector3f) a.normalise();
                Vector3f W = (Vector3f) Vector3f.cross(L, (Vector3f) n.normalise(), null).normalise();
                Vector3f pos = new Vector3f(
                        p.x + (i - (c - 1) / 2f) * l / c * L.x + (j - (c - 1) / 2f) * w / c * W.x,
                        p.y + (i - (c - 1) / 2f) * l / c * L.y + (j - (c - 1) / 2f) * w / c * W.y,
                        p.z + (i - (c - 1) / 2f) * l / c * L.z + (j - (c - 1) / 2f) * w / c * W.z);
                quad(pos, n, a, l / c, w / c);
            }
        }
        
        glEnd();
    }

    public static void fillPlane(Vector3f p, Vector3f n, Vector3f a, float l, float w, int c, Texture texture) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getID());
        glBegin(GL_QUADS);
        
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                glNormal3f(n.x, n.y, n.z);
                Vector3f L = (Vector3f) a.normalise();
                Vector3f W = (Vector3f) Vector3f.cross(L, (Vector3f) n.normalise(), null).normalise();
                Vector3f pos = new Vector3f(
                        p.x + (i - (c - 1) / 2f) * l / c * L.x + (j - (c - 1) / 2f) * w / c * W.x,
                        p.y + (i - (c - 1) / 2f) * l / c * L.y + (j - (c - 1) / 2f) * w / c * W.y,
                        p.z + (i - (c - 1) / 2f) * l / c * L.z + (j - (c - 1) / 2f) * w / c * W.z);
                texturedQuad(pos, n, a, l / c, w / c, 
                        new Vector2f((float) i * l / (float) c, (float) j * w / (float) c), 
                        new Vector2f(l / (float) c, w / (float) c));
            }
        }
        
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D); 
    }

    public static void enterOrtho(Game game) {
        glPushAttrib(GL_DEPTH_BUFFER_BIT | GL_ENABLE_BIT);
        glPushMatrix();
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
     
        glLoadIdentity();
        glOrtho(0, game.getWidth(), game.getHeight(), 0, -1, 1);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
    } 

    public void leaveOrtho() {
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glPopAttrib();
    }
}
