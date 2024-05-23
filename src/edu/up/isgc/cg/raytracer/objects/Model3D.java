package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Barycentric;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model3D extends Object3D {
    private List<Triangle> triangles;
    private double scale = 1;
    private Quaternion rotation;
    private List<Triangle> originalTriangles; // Store the original triangles

    public Model3D(Vector3D position, Triangle[] triangles, Color color) {
        super(position, color);
        setOriginalTriangles(triangles); // Store original triangles
        setTriangles(triangles);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    private void setTriangles(Triangle[] triangles) {
        this.triangles = Arrays.asList(triangles);
        updateTrianglesWithScale();
    }

    private void setOriginalTriangles(Triangle[] triangles) {
        this.originalTriangles = Arrays.asList(triangles);
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
        updateTrianglesWithScale();
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
            Triangle originalTriangle = originalTriangles.get(i);
            Vector3D[] transformedVertices = new Vector3D[3];
            for (int j = 0; j < 3; j++) {
                Vector3D originalVertex = originalTriangle.getVertices()[j];
                Vector3D scaledVertex = new Vector3D(
                        originalVertex.getX() * scale,
                        originalVertex.getY() * scale,
                        originalVertex.getZ() * scale
                );
                Vector3D rotatedVertex = Vector3D.rotate(scaledVertex, rotation);
                transformedVertices[j] = new Vector3D(
                        position.getX() + rotatedVertex.getX(),
                        position.getY() + rotatedVertex.getY(),
                        position.getZ() + rotatedVertex.getZ()
                );
            }
            triangles.set(i, new Triangle(transformedVertices, originalTriangle.getNormals()));
        }
    }


    private void updateTrianglesWithScale() {
        Vector3D position = getPosition();
        for (int i = 0; i < originalTriangles.size(); i++) {
            Triangle originalTriangle = originalTriangles.get(i);
            Vector3D[] scaledVertices = new Vector3D[3];
            for (int j = 0; j < 3; j++) {
                Vector3D originalVertex = originalTriangle.getVertices()[j];
                scaledVertices[j] = new Vector3D(
                        position.getX() + originalVertex.getX() * scale,
                        position.getY() + originalVertex.getY() * scale,
                        position.getZ() + originalVertex.getZ() * scale
                );
            }
            triangles.set(i, new Triangle(scaledVertices, originalTriangle.getNormals()));
        }
    }
}
