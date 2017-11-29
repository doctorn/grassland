package net.industrial.grassland.graphics;

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
}