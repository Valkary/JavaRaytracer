package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

import java.awt.*;

public class SpotLight extends Light {
    private Vector3D direction;
    private double coneAngle;
    private int fallOff;

    public SpotLight(Vector3D position, Vector3D direction, Material material, double intensity, double coneAngle) {
        super(position, material, intensity);
        setDirection(direction);
        setConeAngle(coneAngle);
        setFallOff(1);
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    public double getConeAngle() {
        return coneAngle;
    }

    public void setConeAngle(double coneAngle) {
        this.coneAngle = coneAngle;
    }

    public int getFallOff() {
        return fallOff;
    }

    public void setFallOff(int fallOff) {
        this.fallOff = fallOff;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D lightDirection = Vector3D.normalize(Vector3D.substract(intersection.getPosition(), getPosition()));
        double lightObjectAlignment = Vector3D.dotProduct(lightDirection, getDirection());

        if (lightObjectAlignment > Math.cos(Math.toRadians(getConeAngle()))) {
            double lambertSurface = Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(lightDirection, -1.0)), 0.0);
            return (lambertSurface / Math.pow(Math.abs(Vector3D.magnitude(Vector3D.substract(intersection.getPosition(), getPosition()))), getFallOff()));
        } else {
            return 0.0;
        }
    }
}
