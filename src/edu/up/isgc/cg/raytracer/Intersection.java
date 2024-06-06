package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.objects.Object3D;

/**
 * The Intersection class represents the intersection point between a ray and an object in the scene.
 * It contains information about the distance from the ray's origin to the intersection point, the position
 * of the intersection, the normal at the intersection, and the object that was intersected.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public class Intersection {
    private double distance;
    private Vector3D position;
    private Vector3D normal;
    private Object3D object;

    /**
     * Constructs a new Intersection with the specified position, distance, normal, and object.
     *
     * @param position The position of the intersection.
     * @param distance The distance from the ray's origin to the intersection point.
     * @param normal The normal vector at the intersection point.
     * @param object The object that was intersected.
     */
    public Intersection(Vector3D position, double distance, Vector3D normal, Object3D object) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }

    /**
     * Gets the distance from the ray's origin to the intersection point.
     *
     * @return The distance to the intersection point.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the distance from the ray's origin to the intersection point.
     *
     * @param distance The new distance to the intersection point.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets the position of the intersection point.
     *
     * @return The position of the intersection point.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the position of the intersection point.
     *
     * @param position The new position of the intersection point.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Gets the normal vector at the intersection point.
     *
     * @return The normal vector at the intersection point.
     */
    public Vector3D getNormal() {
        return normal;
    }

    /**
     * Sets the normal vector at the intersection point.
     *
     * @param normal The new normal vector at the intersection point.
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    /**
     * Gets the object that was intersected.
     *
     * @return The object that was intersected.
     */
    public Object3D getObject() {
        return object;
    }

    /**
     * Sets the object that was intersected.
     *
     * @param object The new object that was intersected.
     */
    public void setObject(Object3D object) {
        this.object = object;
    }
}
