package net.industrial.grassland.graphics;

public interface Vector<T> {
    public float dot(T other);
    public float length();
    public T scale(float scale);
    public T add(T other);
    public T sub(T other);
    public T normalise();
}
