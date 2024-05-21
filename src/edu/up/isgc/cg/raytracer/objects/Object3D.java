package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double diffuseIndex;
    private double reflectionIndex;
    private double refractiveIndex;

    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
        diffuseIndex = 100;
        reflectionIndex = 0;
        refractiveIndex = 0;
    }

    public Object3D(Vector3D position, Color color, double diffuse, double reflectivity, double refraction) {
        setPosition(position);
        setColor(color);
        setDiffuseIndex(diffuse);
        setReflectionIndex(reflectivity);
        setRefractiveIndex(refraction);
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

    public double getReflectionIndex() {
        return reflectionIndex;
    }

    public void setReflectionIndex(double reflectionIndex) {
        this.reflectionIndex = reflectionIndex;
    }

    public double getDiffuseIndex() {
        return diffuseIndex;
    }

    public void setDiffuseIndex(double diffuseIndex) {
        this.diffuseIndex = diffuseIndex;
    }

    public double getRefractiveIndex() {
        return refractiveIndex;
    }

    public void setRefractiveIndex(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }
}
