package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Barycentric;
import edu.up.isgc.cg.raytracer.tools.Material;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * The Model3D class represents a 3D model composed of triangles.
 * It includes functionality for scaling, rotating, and transforming the model.
 *
 * @author  Jafet Rodriguez, José Salcedo
 */
public class Model3D extends Object3D {
    private List<Triangle> triangles;
    private double scale = 1;
    private Quaternion rotation = null;
    public final List<Triangle> originalTriangles; // Store the original triangles

    /**
     * Constructs a new Model3D with the specified position, triangles, and material.
     *
     * @param position The position of the model.
     * @param triangles The array of triangles composing the model.
     * @param material The material of the model.
     */
    public Model3D(Vector3D position, Triangle[] triangles, Material material) {
        super(position, material);
        originalTriangles = Arrays.asList(triangles);
        setTriangles(triangles);
    }

    /**
     * Sets the triangles of the model and applies transformations.
     *
     * @param triangles The array of triangles to set.
     */
    private void setTriangles(Triangle[] triangles) {
        this.triangles = Arrays.asList(triangles.clone());
        updateTrianglesWithTransformation();
    }

    /**
     * Gets the list of triangles composing the model.
     *
     * @return The list of triangles.
     */
    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector3D position = Vector3D.ZERO();
        Vector3D normal = Vector3D.ZERO();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if (intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)) {
                distance = intersectionDistance;
                position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
                normal = Vector3D.ZERO();
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                Vector3D[] normals = triangle.getNormals();
                for (int i = 0; i < uVw.length; i++) {
                    normal = Vector3D.add(normal, Vector3D.scalarMultiplication(normals[i], uVw[i]));
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(position, distance, normal, this);
    }

    /**
     * Gets the scale of the model.
     *
     * @return The scale of the model.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets the scale of the model and updates the transformations.
     *
     * @param scale The new scale of the model.
     */
    public void setScale(double scale) {
        this.scale = scale;
        updateTrianglesWithTransformation();
    }

    /**
     * Gets the rotation of the model.
     *
     * @return The rotation of the model.
     */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the model and updates the transformations.
     *
     * @param rotation The new rotation of the model.
     */
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
        updateTrianglesWithTransformation();
    }

    /**
     * Updates the triangles with the current transformations (scale and rotation).
     */
    private void updateTrianglesWithTransformation() {
        Vector3D position = getPosition();
        for (int i = 0; i < originalTriangles.size(); i++) {
            Triangle originalTriangle = originalTriangles.get(i).clone();
            Vector3D[] transformedVertices = new Vector3D[]{Vector3D.ZERO(), Vector3D.ZERO(), Vector3D.ZERO()};
            Vector3D[] transformedNormals = new Vector3D[]{Vector3D.ZERO(), Vector3D.ZERO(), Vector3D.ZERO()};

            for (int j = 0; j < 3; j++) {
                transformedVertices[j] = Vector3D.add(Vector3D.scalarMultiplication(originalTriangle.getVertices()[j], scale), position);
                transformedNormals[j] = originalTriangle.getNormals()[j];

                if (rotation != null && !rotation.equals(Quaternion.IDENTITY)) {
                    transformedVertices[j] = Vector3D.rotate(transformedVertices[j], rotation);
                    transformedNormals[j] = Vector3D.rotate(transformedNormals[j], rotation);
                }
            }

            triangles.set(i, new Triangle(transformedVertices, transformedNormals));
        }
    }
}
