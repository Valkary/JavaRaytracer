package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Barycentric;
import edu.up.isgc.cg.raytracer.tools.Material;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Model3D extends Object3D {
    private List<Triangle> triangles;
    private double scale = 1;
    private Quaternion rotation= null;
    public final List<Triangle> originalTriangles; // Store the original triangles

    public Model3D(Vector3D position, Triangle[] triangles, Material material) {
        super(position, material);
        originalTriangles = Arrays.asList(triangles);
        setTriangles(triangles);
    }

    private void setTriangles(Triangle[] triangles) {
        this.triangles = Arrays.asList(triangles.clone());
        updateTrianglesWithTransformation();
    }

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

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        updateTrianglesWithTransformation();
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
        updateTrianglesWithTransformation();
    }

    private void updateTrianglesWithTransformation() {
        Vector3D position = getPosition();
        for (int i = 0; i < originalTriangles.size(); i++) {
            Triangle originalTriangle = originalTriangles.get(i).clone();
            Vector3D[] transformedVertices = new Vector3D[]{Vector3D.ZERO(),Vector3D.ZERO(),Vector3D.ZERO()};
            for (int j = 0; j < 3; j++) {
                transformedVertices[j] = Vector3D.add(transformedVertices[j], Vector3D.scalarMultiplication(originalTriangle.getVertices()[j], scale));

                if (rotation != null && !rotation.equals(Quaternion.IDENTITY)) {
                    transformedVertices[j] = Vector3D.rotate(transformedVertices[j], rotation);
                }

                transformedVertices[j].setX(transformedVertices[j].getX() + position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() + position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() + position.getZ());
            }

            triangles.set(i, new Triangle(transformedVertices, originalTriangle.getNormals()));
        }
    }
}
