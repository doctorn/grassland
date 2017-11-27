package net.industrial.grassland.graphics;

import net.industrial.grassland.resources.Sprite;
import net.industrial.grassland.scene.Camera;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class Quad implements Comparable<Quad> {
    private int texture = 0;
    private Vector3f[] vertices = new Vector3f[4];
    private Vector3f normal;
    private Vector2f tStart, tSize;
    private float distance;
    private boolean wireframe;

    public Quad(Vector3f position, Vector3f normal, Vector3f lAxis, 
            float l, float w, boolean wireframe, 
            Sprite sprite, Vector2f tStart, Vector2f tSize,
            Camera camera) {
        this.wireframe = wireframe;
     
        if (sprite != null) {
            texture = sprite.getID();
            this.tStart = tStart;
            this.tSize = tSize;
        }
     
        this.normal = (Vector3f) normal.normalise();
        lAxis = (Vector3f) lAxis.normalise();
        Vector3f wAxis = (Vector3f) Vector3f.cross(lAxis, normal, null).normalise();
        
        vertices[0] = new Vector3f(
                position.x - (l / 2f) * lAxis.x - (w / 2f) * wAxis.x,
                position.y - (l / 2f) * lAxis.y - (w / 2f) * wAxis.y,
                position.z - (l / 2f) * lAxis.z - (w / 2f) * wAxis.z);
        vertices[1] = new Vector3f(
                position.x - (l / 2f) * lAxis.x + (w / 2f) * wAxis.x,
                position.y - (l / 2f) * lAxis.y + (w / 2f) * wAxis.y,
                position.z - (l / 2f) * lAxis.z + (w / 2f) * wAxis.z);
        vertices[2] = new Vector3f(
                position.x + (l / 2f) * lAxis.x + (w / 2f) * wAxis.x,
                position.y + (l / 2f) * lAxis.y + (w / 2f) * wAxis.y,
                position.z + (l / 2f) * lAxis.z + (w / 2f) * wAxis.z);
        vertices[3] = new Vector3f(
                position.x + (l / 2f) * lAxis.x - (w / 2f) * wAxis.x,
                position.y + (l / 2f) * lAxis.y - (w / 2f) * wAxis.y,
                position.z + (l / 2f) * lAxis.z - (w / 2f) * wAxis.z);
       
        if (camera != null) {
            distance = Float.NEGATIVE_INFINITY;
            for (int i = 0; i < 4; i++) {
                Vector3f d = Vector3f.sub(vertices[i], camera.getPosition(), null);
                float potential = d.length();
                if (potential > distance) distance = potential;
            }
        } else distance = 0;
    }

    public void render() {
        boolean lighting = glIsEnabled(GL_LIGHTING); 
     
        if (texture != 0) {
            glEnable(GL_TEXTURE_2D);
            glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            glBindTexture(GL_TEXTURE_2D, texture);
        }
     
        if (wireframe) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
            glDisable(GL_TEXTURE_2D);
            if (lighting) glDisable(GL_LIGHTING);
        }
        
        glBegin(GL_QUADS);
     
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(tStart.x, tStart.y); 
        glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
     
        glTexCoord2f(tStart.x, tStart.y + tSize.y); 
        glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
     
        glTexCoord2f(tStart.x + tSize.x, tStart.y + tSize.y); 
        glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
     
        glTexCoord2f(tStart.x + tSize.x, tStart.y); 
        glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
        
        glEnd();
     
        if (texture != 0) {
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisable(GL_TEXTURE_2D);
        }
     
        if (wireframe) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glEnable(GL_CULL_FACE); 
            if (lighting) glEnable(GL_LIGHTING);
        }
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Quad other) {
        if (distance - other.distance < 0) return 1;
        else if (distance - other.distance > 0) return -1;
        else return 0;
    }
}
