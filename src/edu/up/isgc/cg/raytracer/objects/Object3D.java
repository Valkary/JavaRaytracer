package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Vector3D position;
    private Material material;

    public Object3D(Vector3D position, Material material) {
        setMaterial(material);
        setPosition(position);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }
}
