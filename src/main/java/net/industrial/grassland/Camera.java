package net.industrial.grassland;

import static org.lwjgl.util.glu.GLU.*;

public abstract class Camera {
    private float x = 0f, y = 0f, z = 0f;
    private float pitch = 0f, yaw = 0f;
    
    public void look() {
        gluLookAt(x, y, z,
            x - (float) Math.sin(pitch) * (float) Math.sin(yaw), 
            y + (float) Math.cos(pitch), 
            z - (float) Math.sin(pitch) * (float) Math.cos(yaw),  
            0f, 1f, 0f);
    }
    
    public abstract void update(Game game, int delta);
   
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
        RenderUtils.drawCuboid(x, y, z, 0.05f, 0.05f, 0.05f);
    }

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
