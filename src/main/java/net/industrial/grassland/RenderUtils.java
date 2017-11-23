package net.industrial.grassland;

<<<<<<< HEAD
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

=======
import net.industrial.grassland.Game;
import org.lwjgl.util.vector.Vector3f;
>>>>>>> upstream/master
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


    public static List<Vector3f> createQuad(Vector3f p, Vector3f n, Vector3f a, float l, float w) {
        List<Vector3f> vertices = new ArrayList<>();

        Vector3f L = (Vector3f) a.normalise();
        Vector3f N = (Vector3f) n.normalise();
        Vector3f W = (Vector3f) Vector3f.cross(L,N,null).normalise();

        Vector3f A = new Vector3f(
                p.x - (l/2f) * L.x - (w/2f) * W.x,
                p.y - (l/2f) * L.y - (w/2f) * W.y,
                p.z - (l/2f) * L.z - (w/2f) * W.z);
        vertices.add(A);

        Vector3f B = new Vector3f(
                p.x - (l/2f) * L.x + (w/2f) * W.x,
                p.y - (l/2f) * L.y + (w/2f) * W.y,
                p.z - (l/2f) * L.z + (w/2f) * W.z);
        vertices.add(B);

        Vector3f C = new Vector3f(
                p.x + (l/2f) * L.x + (w/2f) * W.x,
                p.y + (l/2f) * L.y + (w/2f) * W.y,
                p.z + (l/2f) * L.z + (w/2f) * W.z);
        vertices.add(C);

        Vector3f D = new Vector3f(
                p.x + (l/2f) * L.x - (w/2f) * W.x,
                p.y + (l/2f) * L.y - (w/2f) * W.y,
                p.z + (l/2f) * L.z - (w/2f) * W.z);
        vertices.add(D);

        return vertices;
    }

    public static  void fillQuad(Vector3f p, Vector3f n, Vector3f a, float l, float w) {
        glBegin(GL_QUADS);

        glNormal3f(n.x,n.y,n.z);
        List<Vector3f> vertices = createQuad(p, n, a, l, w);
        for(Vector3f v : vertices)
            glVertex3f(v.x,v.y,v.z);

        glEnd();
    }

    public static void fillFlatPlane(Vector3f p, Vector3f n, Vector3f a, float l, float w, int c) {
        glBegin(GL_QUADS);

        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                glNormal3f(n.x,n.y,n.z);
                Vector3f L = (Vector3f) a.normalise();
                Vector3f W = (Vector3f) Vector3f.cross(L,(Vector3f)n.normalise(),null).normalise();
                Vector3f pos = new Vector3f(
                        p.x + (i - (c-1)/2f) * l/c * L.x + (j - (c-1)/2f) * w/c * W.x,
                        p.y + (i - (c-1)/2f) * l/c * L.y + (j - (c-1)/2f) * w/c * W.y,
                        p.z + (i - (c-1)/2f) * l/c * L.z + (j - (c-1)/2f) * w/c * W.z );
                List<Vector3f> vertices = createQuad(pos, n, a, l/c, w/c);
                for(Vector3f v : vertices)
                    glVertex3f(v.x,v.y,v.z);
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
