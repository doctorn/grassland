package net.industrial.grassland.graphics;

public class Vector2f implements Vector<Vector2f> {
    public float x, y;
    
    public Vector2f() {
        x = 0f;
        y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public float dot(Vector2f other) {
        return x * other.x + y * other.y;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public Vector2f scale(float scale) {
        return new Vector2f(x * scale, y * scale);
    }

    @Override
    public Vector2f add(Vector2f other) {
        return new Vector2f(x + other.x, y + other.y);
    }

    @Override
    public Vector2f sub(Vector2f other) {
        return new Vector2f(x - other.x, y - other.y);
    }

    @Override
    public Vector2f normalise() {
        float length = length();
        return new Vector2f(x / length, y / length);
    }
}
