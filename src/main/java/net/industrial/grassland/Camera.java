package net.industrial.grassland;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.util.glu.GLU.*;

public abstract class Camera {
    private Vector3f position = new Vector3f();
    private float pitch = 0f, yaw = 0f;
    
    public void look() {
        Vector3f l = lookVector();
        gluLookAt(position.x, position.y, position.z,
            position.x - l.x, 
            position.y - l.y, 
            position.z - l.z,  
            0f, 1f, 0f);
    }

    public Vector3f lookVector() {
        return (Vector3f) (new Vector3f((float) Math.sin(pitch) * (float) Math.sin(yaw),
                - (float) Math.cos(pitch),
                (float) Math.sin(pitch) * (float) Math.cos(yaw))).normalise();
    }

    public Vector3f flatLookVector() {
        return (Vector3f) (new Vector3f((float) Math.sin(pitch) * (float) Math.sin(yaw),
                0f,
                (float) Math.sin(pitch) * (float) Math.cos(yaw))).normalise();
    }
    
    public Vector3f axisVector() {
        return Vector3f.cross(new Vector3f(0f, 1f, 0f), lookVector(), null);
    }

    public Vector3f flatAxisVector() {
        return Vector3f.cross(new Vector3f(0f, 1f, 0f), flatLookVector(), null);
    }

    public abstract void update(Game game, int delta);
   
    public void setPosition(Vector3f position) {
        this.position = position; 
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }

    public void setAngle(float yaw, float pitch) {
        if (pitch < 0.1f * (float) Math.PI) pitch = 0.1f * (float) Math.PI;
        if (pitch > 0.9f * (float) Math.PI) pitch = 0.9f * (float) Math.PI;
      
        while (yaw > 2f * (float) Math.PI) yaw -= 2f * (float) Math.PI;
        while (yaw < 0f) yaw += 2f * (float) Math.PI;
     
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public void renderDebug(Game game, int delta) {
        RenderUtils.drawCuboid(position, 0.05f, 0.05f, 0.05f);
    }

    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public Vector3f getPosition() {
        return new Vector3f(position);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
