package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

/**
 * The DirectionalLight class represents a directional light source in a 3D scene.
 * It extends the Light class and includes a direction vector.
 *
 * @author Jafet Rodriguez
 */
public class DirectionalLight extends Light {
    private Vector3D direction;

    /**
     * Constructs a new DirectionalLight with the specified direction, material, and intensity.
     *
     * @param direction The direction of the light.
     * @param material The material of the light.
     * @param intensity The intensity of the light.
     */
    public DirectionalLight(Vector3D direction, Material material, double intensity) {
        super(Vector3D.ZERO(), material, intensity);
        setDirection(direction);
    }

    /**
     * Gets the direction of the light.
     *
     * @return The direction of the light.
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the light.
     *
     * @param direction The new direction of the light.
     */
    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}
