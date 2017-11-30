package net.industrial.grassland.graphics;

import net.industrial.grassland.Game;
import net.industrial.grassland.graphics.Graphics;

public class Ray {
    private Vector3f origin;
    private Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction){
        this.origin = origin;
        this.direction = direction.normalise();
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void renderDebug(Game game, Graphics graphics) {
        graphics.drawLine(origin, origin.add(direction.scale(100f))); 
    }
}
