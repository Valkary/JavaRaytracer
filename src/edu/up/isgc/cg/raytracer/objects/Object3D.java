package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double diffuse;
    private double reflectivity;


    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
        diffuse = 100;
        reflectivity = 0;
    }

    public Object3D(Vector3D position, Color color, double diffuse, double reflectivity) {
        setPosition(position);
        setColor(color);
        setDiffuse(diffuse);
        setReflectivity(reflectivity);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }
}
