package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

/**
 * The PointLight class represents a point light source in a 3D scene.
 * It extends the Light class and calculates the light intensity based on the normal and light direction.
 *
 * @author Jafet Rodriguez
 */
public class PointLight extends Light {
    /**
     * Constructs a new PointLight with the specified position, material, and intensity.
     *
     * @param position The position of the light.
     * @param material The material of the light.
     * @param intensity The intensity of the light.
     */
    public PointLight(Vector3D position, Material material, double intensity) {
        super(position, material, intensity);
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(
                Vector3D.dotProduct(intersection.getNormal(),
                        Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()))),
                0.0);
    }
}
