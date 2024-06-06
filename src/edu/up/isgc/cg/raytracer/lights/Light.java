package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.objects.Object3D;
import edu.up.isgc.cg.raytracer.tools.Material;

/**
 * The Light class represents a light source in a 3D scene.
 * It is an abstract class that provides a common interface for different types of lights.
 *
 * @author Jafet Rodriguez
 */
public abstract class Light extends Object3D {
    private double intensity;

    /**
     * Constructs a new Light with the specified position, material, and intensity.
     *
     * @param position The position of the light.
     * @param material The material of the light.
     * @param intensity The intensity of the light.
     */
    public Light(Vector3D position, Material material, double intensity) {
        super(position, material);
        setIntensity(intensity);
    }

    /**
     * Gets the intensity of the light.
     *
     * @return The intensity of the light.
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity of the light.
     *
     * @param intensity The new intensity of the light.
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Calculates the dot product of the light direction and the normal at the intersection point.
     *
     * @param intersection The intersection point.
     * @return The dot product of the light direction and the normal.
     */
    public abstract double getNDotL(Intersection intersection);

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
