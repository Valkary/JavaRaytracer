package edu.up.isgc.cg.raytracer;

/**
 * The Ray class represents a ray in 3D space, defined by an origin and a direction.
 * It is used in ray tracing to determine intersections with objects in a scene.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    /**
     * Constructs a Ray with the specified origin and direction.
     *
     * @param origin The origin of the ray.
     * @param direction The direction of the ray.
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * Gets the origin of the ray.
     *
     * @return The origin of the ray.
     */
    public Vector3D getOrigin() {
        return origin;
    }

    /**
     * Sets the origin of the ray.
     *
     * @param origin The new origin of the ray.
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * Gets the normalized direction of the ray.
     *
     * @return The normalized direction of the ray.
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }

    /**
     * Sets the direction of the ray.
     *
     * @param direction The new direction of the ray.
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
