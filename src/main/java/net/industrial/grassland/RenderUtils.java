package net.industrial.grassland;

import java.util.ArrayList;
import java.util.List;
import net.industrial.grassland.Game;
import net.industrial.grassland.resources.Sprite;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

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
    
    private static void texturedQuad(Vector3f p, Vector3f n, Vector3f a, 
            float l, float w, Vector2f tStart, Vector2f tSize) {
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
    
    public static void fillQuad(Vector3f p, Vector3f n, Vector3f a, float l, float w, Sprite sprite) {
        glEnable(GL_TEXTURE_2D);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glBindTexture(GL_TEXTURE_2D, sprite.getID());
        
        glBegin(GL_QUADS);
        texturedQuad(p, n, a, l, w, sprite.getStartVector(), sprite.getSizeVector()); 
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

    public static void leaveOrtho() {
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glPopAttrib();
    }
}
