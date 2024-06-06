package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

/**
 * The Object3D class represents a 3D object in a ray tracing scene.
 * It contains properties such as position and material.
 * This class is abstract and implements the IIntersectable interface.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public abstract class Object3D implements IIntersectable {
    private Vector3D position;
    private Material material;

    /**
     * Constructs a new Object3D with the specified position and material.
     *
     * @param position The position of the object.
     * @param material The material of the object.
     */
    public Object3D(Vector3D position, Material material) {
        setMaterial(material);
        setPosition(position);
    }

    /**
     * Gets the material of the object.
     *
     * @return The material of the object.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the object.
     *
     * @param material The new material of the object.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Gets the position of the object.
     *
     * @return The position of the object.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the position of the object.
     *
     * @param position The new position of the object.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }
}
