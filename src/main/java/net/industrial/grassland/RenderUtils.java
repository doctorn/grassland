package net.industrial.grassland;

import net.industrial.grassland.Game;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void drawCuboid(Vector3f position, 
            float dX, float dY, float dZ) {
        boolean lighting = glIsEnabled(GL_LIGHTING); 
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glDisable(GL_CULL_FACE);
        
        if (lighting) glDisable(GL_LIGHTING);
        fillCuboid(position, dX, dY, dZ);
        
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_CULL_FACE); 
        if (lighting) glEnable(GL_LIGHTING);
    }

    public static void fillFlatPlane(float x, float y, float z,
            float d) {
        glBegin(GL_QUADS);
      
        for (int i = 0; i * 0.1f < d; i++) {
            for (int j = 0; j * 0.1f < d; j++) {
                glNormal3f(0, 1f, 0);
                glVertex3f(x - d / 2f + 0.1f * (i - 1), y, z - d / 2f + 0.1f * j);
                glVertex3f(x - d / 2f + 0.1f * i, y, z - d / 2f + 0.1f * j);
                glVertex3f(x - d / 2f + 0.1f * i, y, z - d / 2f + 0.1f * (j - 1));
                glVertex3f(x - d / 2f + 0.1f * (i - 1), y, z - d / 2f + 0.1f * (j - 1));
            }
        }
        
        glEnd();
    }

    public static void fillCuboid(Vector3f position,
            float dX, float dY, float dZ) {
        float sX = position.x - dX / 2;
        float sY = position.y - dY / 2;
        float sZ = position.z + dZ / 2;
        float mX = position.x + dX / 2;
        float mY = position.y + dY / 2;
        float mZ = position.z - dZ / 2;
     
        glBegin(GL_QUADS);
        
        glNormal3f(0, 0, 1f);
        glVertex3f(sX, sY, sZ);
        glVertex3f(mX, sY, sZ);
        glVertex3f(mX, mY, sZ);
        glVertex3f(sX, mY, sZ);
     
        glNormal3f(0, 0, - 1f);
        glVertex3f(sX, sY, mZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(mX, sY, mZ);
      
        glNormal3f(- 1f, 0, 0);
        glVertex3f(sX, sY, sZ);
        glVertex3f(sX, mY, sZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(sX, sY, mZ);
     
        glNormal3f(1f, 0, 0);
        glVertex3f(mX, sY, sZ);
        glVertex3f(mX, sY, mZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(mX, mY, sZ);
        
        glNormal3f(0, - 1f, 0);
        glVertex3f(sX, sY, mZ);
        glVertex3f(mX, sY, mZ);
        glVertex3f(mX, sY, sZ);
        glVertex3f(sX, sY, sZ);
     
        glNormal3f(0f, 1f, 0);
        glVertex3f(mX, mY, sZ);
        glVertex3f(mX, mY, mZ);
        glVertex3f(sX, mY, mZ);
        glVertex3f(sX, mY, sZ);
        
        glEnd();
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
