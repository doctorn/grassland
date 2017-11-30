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

    public float closestDistance(Ray r) {
        return (this.getPosition().dot(r.getDirection()) - 
                r.getOrigin().dot(r.getDirection())) /
                r.getDirection().dot(r.getDirection());
    }

    public boolean boundsVector(Vector3f v) {
        return (Math.abs(v.x) <= getWidth() / 2f &&
                Math.abs(v.y) <= getHeight() / 2f &&
                Math.abs(v.z) <= getDepth() / 2f);
    }

    public Vector3f closestApproach(Ray r) { 
        return r.getOrigin().sub(this.getPosition())
            .add(r.getDirection().scale(closestDistance(r)));
    }

    public boolean intersectsRay(Ray r) {
        return boundsVector(closestApproach(r));
    }
}
