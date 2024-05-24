package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

public class Material {
    private Color color = Color.WHITE;
    private double reflectivity;
    private double refractivity;
    private double shininess;

    public static Material MIRROR = new Material(0.9, 1.0, 100.0);
    public static Material MATTE = new Material(0.0, 1.0, 15.0);
    public static Material GLASS = new Material(0.1, 1.5, 50.0);
    public static Material NONE = new Material(0.0, 0.0, 0.0);
    public static Material METAL = new Material(0.95, 0.0, 200);

    public Material(Color color, double reflectivity, double refractivity, double shininess) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
    }

    public Material(double reflectivity, double refractivity, double shininess) {
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
    }

    public Material instantiateWithColor(Color color) {
        return new Material(color, reflectivity, refractivity, shininess);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    public double getRefractivity() {
        return refractivity;
    }

    public void setRefractivity(double refractivity) {
        this.refractivity = refractivity;
    }

    public double getShininess() {
        return shininess;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }
}
