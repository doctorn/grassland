package net.industrial.grassland;

public abstract class CollidableGameObject extends GameObject {
    public boolean collidingWith(CollidableGameObject other) {
        if (other.getMinX() > getMaxX()) return false;
        if (other.getMinY() > getMaxY()) return false;
        if (other.getMinZ() > getMaxZ()) return false;
        if (getMinX() > other.getMaxX()) return false;
        if (getMinY() > other.getMaxY()) return false;
        if (getMinZ() > other.getMaxZ()) return false;
        return true;
    }

    public float getMinX() {
        return getPosition().x - getWidth() / 2;
    }
    
    public float getMaxX() {
        return getPosition().x + getWidth() / 2;
    }
    
    public float getMinY() {
        return getPosition().y - getHeight() / 2;
    }
    
    public float getMaxY() {
        return getPosition().y + getHeight() / 2;
    }
    
    public float getMinZ() {
        return getPosition().z - getDepth() / 2;
    }
    
    public float getMaxZ() {
        return getPosition().z + getDepth() / 2;
    }
}
