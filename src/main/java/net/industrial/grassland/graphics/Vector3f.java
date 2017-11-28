package net.industrial.grassland.graphics;

public class Vector3f implements Vector<Vector3f> {
    public float x, y, z;

    public Vector3f() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    @Override
    public float dot(Vector3f other) {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public Vector3f scale(float scale) {
        return new Vector3f(x * scale, y * scale, z * scale);
    }

    @Override
    public Vector3f add(Vector3f other) {
        return new Vector3f(x + other.x, y + other.y, z + other.z);
    }

    @Override
    public Vector3f sub(Vector3f other) {
        return new Vector3f(x - other.x, y - other.y, z - other.z);
    }

    @Override
    public Vector3f normalise() {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f cross(Vector3f other) {
        return new Vector3f(y * other.z - z * other.y,
                - x * other.z + z * other.x,
                x * other.y - y * other.x);
    }
}
