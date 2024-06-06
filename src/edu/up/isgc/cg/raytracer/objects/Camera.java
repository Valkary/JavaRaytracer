package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Material;

import java.awt.*;

/**
 * The Camera class represents a camera in a 3D scene.
 * It contains properties such as field of view, resolution, and near and far clipping planes.
 * The camera is used to calculate the positions of rays to be traced in the scene.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public class Camera extends Object3D {
    // FOV[0] = Horizontal | FOV[1] = Vertical
    private double[] fieldOfView = new double[2];
    private double defaultZ = 15.0;
    private int[] resolution = new int[2];
    private double[] nearFarPlanes = new double[2];

    /**
     * Constructs a new Camera with the specified properties.
     *
     * @param position The position of the camera.
     * @param fovH The horizontal field of view.
     * @param fovV The vertical field of view.
     * @param width The width of the resolution.
     * @param height The height of the resolution.
     * @param nearPlane The near clipping plane.
     * @param farPlane The far clipping plane.
     */
    public Camera(Vector3D position, double fovH, double fovV,
                  int width, int height, double nearPlane, double farPlane) {
        super(position, Material.NONE);
        setFOV(fovH, fovV);
        setResolution(width, height);
        setNearFarPlanes(new double[]{nearPlane, farPlane});
    }

    /**
     * Gets the field of view.
     *
     * @return The field of view.
     */
    public double[] getFieldOfView() {
        return fieldOfView;
    }

    private void setFieldOfView(double[] fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    /**
     * Gets the horizontal field of view.
     *
     * @return The horizontal field of view.
     */
    public double getFOVHorizontal() {
        return fieldOfView[0];
    }

    /**
     * Gets the vertical field of view.
     *
     * @return The vertical field of view.
     */
    public double getFOVVertical() {
        return fieldOfView[1];
    }

    /**
     * Sets the horizontal field of view.
     *
     * @param fovH The horizontal field of view.
     */
    public void setFOVHorizontal(double fovH) {
        fieldOfView[0] = fovH;
    }

    /**
     * Sets the vertical field of view.
     *
     * @param fovV The vertical field of view.
     */
    public void setFOVVertical(double fovV) {
        fieldOfView[1] = fovV;
    }

    /**
     * Sets both horizontal and vertical fields of view.
     *
     * @param fovH The horizontal field of view.
     * @param fovV The vertical field of view.
     */
    public void setFOV(double fovH, double fovV) {
        setFOVHorizontal(fovH);
        setFOVVertical(fovV);
    }

    /**
     * Gets the default Z coordinate.
     *
     * @return The default Z coordinate.
     */
    public double getDefaultZ() {
        return defaultZ;
    }

    /**
     * Sets the default Z coordinate.
     *
     * @param defaultZ The default Z coordinate.
     */
    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }

    /**
     * Gets the resolution.
     *
     * @return The resolution.
     */
    public int[] getResolution() {
        return resolution;
    }

    /**
     * Sets the width of the resolution.
     *
     * @param width The width of the resolution.
     */
    public void setResolutionWidth(int width) {
        resolution[0] = width;
    }

    /**
     * Sets the height of the resolution.
     *
     * @param height The height of the resolution.
     */
    public void setResolutionHeight(int height) {
        resolution[1] = height;
    }

    /**
     * Sets the resolution.
     *
     * @param width The width of the resolution.
     * @param height The height of the resolution.
     */
    public void setResolution(int width, int height) {
        setResolutionWidth(width);
        setResolutionHeight(height);
    }

    /**
     * Gets the width of the resolution.
     *
     * @return The width of the resolution.
     */
    public int getResolutionWidth() {
        return resolution[0];
    }

    /**
     * Gets the height of the resolution.
     *
     * @return The height of the resolution.
     */
    public int getResolutionHeight() {
        return resolution[1];
    }

    private void setResolution(int[] resolution) {
        this.resolution = resolution;
    }

    /**
     * Gets the near and far clipping planes.
     *
     * @return The near and far clipping planes.
     */
    public double[] getNearFarPlanes() {
        return nearFarPlanes;
    }

    private void setNearFarPlanes(double[] nearFarPlanes) {
        this.nearFarPlanes = nearFarPlanes;
    }

    /**
     * Calculates the positions to ray trace based on the field of view and resolution.
     *
     * @return A 2D array of Vector3D positions.
     */
    public Vector3D[][] calculatePositionsToRay() {
        double angleMaxX = getFOVHorizontal() / 2.0;
        double radiusMaxX = getDefaultZ() / Math.cos(Math.toRadians(angleMaxX));

        double maxX = Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
        double minX = -maxX;

        double angleMaxY = getFOVVertical() / 2.0;
        double radiusMaxY = getDefaultZ() / Math.cos(Math.toRadians(angleMaxY));

        double maxY = Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
        double minY = -maxY;

        Vector3D[][] positions = new Vector3D[getResolutionWidth()][getResolutionHeight()];
        double posZ = defaultZ;

        double stepX = (maxX - minX) / getResolutionWidth();
        double stepY = (maxY - minY) / getResolutionHeight();
        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[x].length; y++) {
                double posX = minX + (stepX * x);
                double posY = maxY - (stepY * y);
                positions[x][y] = new Vector3D(posX, posY, posZ);
            }
        }
        return positions;
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }
}
