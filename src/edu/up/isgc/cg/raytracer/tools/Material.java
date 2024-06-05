package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

public class Material {
    public static final double MAX_SHININESS = 100.0;

    private Color color = Color.WHITE;
    private double reflectivity;
    private double refractivity;
    private double shininess;
    private double absorption;

    public static Material MIRROR = new Material(0.95, 0.0, 100.0, 0.0);
    public static Material MATTE = new Material(0.0, 0.0, 15.0, 0.0);
    public static Material GLASS = new Material(0.8, 1.5, 0.0, 100);
    public static Material NONE = new Material(0.0, 0.0, 0.0, 0.0);
    public static Material METAL = new Material(0.8, 0.0, 10, 0.0);

    public Material(Color color, double reflectivity, double refractivity, double shininess, double absorption) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
        this.absorption = absorption;
    }

    public Material(double reflectivity, double refractivity, double shininess, double absorption) {
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
        this.absorption = absorption;
    }

    public Material colored(Color color) {
        return new Material(color, reflectivity, refractivity, shininess, absorption);
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

    public double getAbsorption() {
        return absorption;
    }

    public void setAbsorption(double absorption) {
        this.absorption = absorption;
    }
}
