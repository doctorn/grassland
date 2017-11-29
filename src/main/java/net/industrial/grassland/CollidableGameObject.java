package net.industrial.grassland;

import net.industrial.grassland.graphics.Ray;
import net.industrial.grassland.graphics.Vector3f;

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

    public boolean rayIntersection(Ray r) {
        float s = (this.getPosition().dot(r.getDirection()) - r.getOrigin().dot(r.getDirection())) /
                r.getDirection().dot(r.getDirection());
        Vector3f closestVector = r.getOrigin().sub(this.getPosition()).add(r.getDirection().scale(s));

        return (Math.abs(closestVector.x) <= getWidth() / 2f &&
                Math.abs(closestVector.y) <= getHeight() / 2f &&
                Math.abs(closestVector.z) <= getDepth() / 2f);
    }
}
