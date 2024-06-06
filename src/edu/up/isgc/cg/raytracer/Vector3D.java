package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.objects.Quaternion;

/**
 * The Vector3D class represents a 3-dimensional vector and provides various vector operations.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public class Vector3D {
    private static final Vector3D ZERO = new Vector3D(0.0, 0.0, 0.0);
    private double x, y, z;

    /**
     * Constructs a new Vector3D with the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Vector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Gets the x-coordinate of the vector.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the vector.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the vector.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the vector.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the z-coordinate of the vector.
     *
     * @return The z-coordinate.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z-coordinate of the vector.
     *
     * @param z The new z-coordinate.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Creates and returns a copy of this vector.
     *
     * @return A clone of this vector.
     */
    public Vector3D clone() {
        return new Vector3D(getX(), getY(), getZ());
    }

    /**
     * Returns a zero vector.
     *
     * @return A vector with all components set to zero.
     */
    public static Vector3D ZERO() {
        return ZERO.clone();
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                "}";
    }

    /**
     * Calculates the dot product of two vectors.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The dot product.
     */
    public static double dotProduct(Vector3D vectorA, Vector3D vectorB) {
        return (vectorA.getX() * vectorB.getX()) + (vectorA.getY() * vectorB.getY()) + (vectorA.getZ() * vectorB.getZ());
    }

    /**
     * Calculates the cross product of two vectors.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The cross product.
     */
    public static Vector3D crossProduct(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D((vectorA.getY() * vectorB.getZ()) - (vectorA.getZ() * vectorB.getY()),
                (vectorA.getZ() * vectorB.getX()) - (vectorA.getX() * vectorB.getZ()),
                (vectorA.getX() * vectorB.getY()) - (vectorA.getY() * vectorB.getX()));
    }

    /**
     * Calculates the magnitude of a vector.
     *
     * @param vectorA The vector.
     * @return The magnitude.
     */
    public static double magnitude(Vector3D vectorA) {
        return Math.sqrt(dotProduct(vectorA, vectorA));
    }

    /**
     * Adds two vectors.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The resulting vector from the addition.
     */
    public static Vector3D add(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY(), vectorA.getZ() + vectorB.getZ());
    }

    /**
     * Subtracts one vector from another.
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The resulting vector from the subtraction.
     */
    public static Vector3D substract(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY(), vectorA.getZ() - vectorB.getZ());
    }

    /**
     * Normalizes a vector.
     *
     * @param vectorA The vector to normalize.
     * @return The normalized vector.
     */
    public static Vector3D normalize(Vector3D vectorA) {
        double mag = Vector3D.magnitude(vectorA);
        return new Vector3D(vectorA.getX() / mag, vectorA.getY() / mag, vectorA.getZ() / mag);
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param vectorA The vector.
     * @param scalar The scalar value.
     * @return The resulting vector from the scalar multiplication.
     */
    public static Vector3D scalarMultiplication(Vector3D vectorA, double scalar) {
        return new Vector3D(vectorA.getX() * scalar, vectorA.getY() * scalar, vectorA.getZ() * scalar);
    }

    /**
     * Multiplies this vector by a scalar and updates its components.
     *
     * @param scalar The scalar value.
     */
    public void scalarMultiplicationWithMutation(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    /**
     * Rotates a vector by a quaternion.
     *
     * @param vector The vector to rotate.
     * @param quaternion The quaternion representing the rotation.
     * @return The rotated vector.
     */
    public static Vector3D rotate(Vector3D vector, Quaternion quaternion) {
        return Quaternion.rotate(vector, quaternion);
    }

    /**
     * Rotates this vector by a quaternion and updates its components.
     *
     * @param quaternion The quaternion representing the rotation.
     */
    public void rotateWithMutation(Quaternion quaternion) {
        Quaternion.rotateWithMutation(this, quaternion);
    }
}
