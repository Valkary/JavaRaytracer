package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

/**
 * The Material class represents the material properties of an object in a ray tracer.
 * It includes properties such as color, reflectivity, refractivity, shininess, and absorption.
 *
 * @author José Salcedo
 */
public class Material {
    public static final double MAX_SHININESS = 100.0;

    private Color color = Color.WHITE;
    private double reflectivity;
    private double refractivity;
    private double shininess;
    private double absorption;

    public static final Material MIRROR = new Material(1, 0.0, 100.0, 0.0);
    public static final Material MATTE = new Material(0.0, 0.0, 15.0, 0.0);
    public static final Material GLASS = new Material(0.8, 1.5, 0.0, 100);
    public static final Material NONE = new Material(0.0, 0.0, 0.0, 0.0);
    public static final Material METAL = new Material(0.8, 0.0, 10, 0.0);

    /**
     * Constructs a new Material with the specified properties.
     *
     * @param color The color of the material.
     * @param reflectivity The reflectivity of the material.
     * @param refractivity The refractivity of the material.
     * @param shininess The shininess of the material.
     * @param absorption The absorption of the material.
     */
    public Material(Color color, double reflectivity, double refractivity, double shininess, double absorption) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
        this.absorption = absorption;
    }

    /**
     * Constructs a new Material with the specified properties and a default color of white.
     *
     * @param reflectivity The reflectivity of the material.
     * @param refractivity The refractivity of the material.
     * @param shininess The shininess of the material.
     * @param absorption The absorption of the material.
     */
    public Material(double reflectivity, double refractivity, double shininess, double absorption) {
        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.shininess = shininess;
        this.absorption = absorption;
    }

    /**
     * Returns a new Material with the specified color and the same other properties.
     *
     * @param color The new color of the material.
     * @return A new Material with the specified color.
     */
    public Material colored(Color color) {
        return new Material(color, reflectivity, refractivity, shininess, absorption);
    }

    /**
     * Gets the color of the material.
     *
     * @return The color of the material.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the material.
     *
     * @param color The new color of the material.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the reflectivity of the material.
     *
     * @return The reflectivity of the material.
     */
    public double getReflectivity() {
        return reflectivity;
    }

    /**
     * Sets the reflectivity of the material.
     *
     * @param reflectivity The new reflectivity of the material.
     */
    public void setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
    }

    /**
     * Gets the refractivity of the material.
     *
     * @return The refractivity of the material.
     */
    public double getRefractivity() {
        return refractivity;
    }

    /**
     * Sets the refractivity of the material.
     *
     * @param refractivity The new refractivity of the material.
     */
    public void setRefractivity(double refractivity) {
        this.refractivity = refractivity;
    }

    /**
     * Gets the shininess of the material.
     *
     * @return The shininess of the material.
     */
    public double getShininess() {
        return shininess;
    }

    /**
     * Sets the shininess of the material.
     *
     * @param shininess The new shininess of the material.
     */
    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    /**
     * Gets the absorption of the material.
     *
     * @return The absorption of the material.
     */
    public double getAbsorption() {
        return absorption;
    }

    /**
     * Sets the absorption of the material.
     *
     * @param absorption The new absorption of the material.
     */
    public void setAbsorption(double absorption) {
        this.absorption = absorption;
    }
}
