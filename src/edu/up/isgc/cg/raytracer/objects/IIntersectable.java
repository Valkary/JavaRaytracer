package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;

/**
 * The IIntersectable interface represents objects that can be intersected by a ray.
 * It defines a method for calculating the intersection point between the object and a ray.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public interface IIntersectable {

    /**
     * Calculates the intersection between the object and a ray.
     *
     * @param ray The ray to test for intersection.
     * @return The intersection point, or null if there is no intersection.
     */
    public abstract Intersection getIntersection(Ray ray);
}
