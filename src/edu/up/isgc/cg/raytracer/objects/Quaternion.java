package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;

/**
 * The Quaternion class represents a quaternion used for 3D rotations.
 * It includes methods for rotating vectors, normalizing, and creating quaternions from axis-angle representations.
 *
 * @author José Salcedo
 */
public class Quaternion {
    public double w;
    public double x;
    public double y;
    public double z;

    public static final Quaternion IDENTITY = new Quaternion(1, 0, 0, 0);

    /**
     * Constructs a new Quaternion with the specified components.
     *
     * @param w The w component.
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     */
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Rotates a Vector3D by this Quaternion.
     *
     * @param vector The vector to be rotated.
     * @return The rotated vector.
     */
    public Vector3D rotate(Vector3D vector) {
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(w, -x, -y, -z);

        Quaternion temp = multiply(this, vectorQuat);
        Quaternion rotatedQuat = multiply(temp, conj);

        return new Vector3D(rotatedQuat.x, rotatedQuat.y, rotatedQuat.z);
    }

    /**
     * Rotates a Vector3D by this Quaternion and updates its components.
     *
     * @param vector The vector to be rotated.
     */
    public void rotateWithMutation(Vector3D vector) {
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(w, -x, -y, -z);

        Quaternion temp = multiply(this, vectorQuat);
        Quaternion rotatedQuat = multiply(temp, conj);

        vector.setX(rotatedQuat.x);
        vector.setY(rotatedQuat.y);
        vector.setZ(rotatedQuat.z);
    }

    /**
     * Rotates a Vector3D by a specified Quaternion.
     *
     * @param vector The vector to be rotated.
     * @param q The quaternion representing the rotation.
     * @return The rotated vector.
     */
    public static Vector3D rotate(Vector3D vector, Quaternion q) {
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(q.w, -q.x, -q.y, -q.z);

        Quaternion temp = multiply(q, vectorQuat);
        Quaternion rotatedQuat = multiply(temp, conj);

        return new Vector3D(rotatedQuat.x, rotatedQuat.y, rotatedQuat.z);
    }

    /**
     * Rotates a Vector3D by a specified Quaternion and updates its components.
     *
     * @param vector The vector to be rotated.
     * @param q The quaternion representing the rotation.
     */
    public static void rotateWithMutation(Vector3D vector, Quaternion q) {
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(q.w, -q.x, -q.y, -q.z);

        Quaternion temp = multiply(q, vectorQuat);
        Quaternion rotatedQuat = multiply(temp, conj);

        vector.setX(rotatedQuat.x);
        vector.setY(rotatedQuat.y);
        vector.setZ(rotatedQuat.z);
    }

    /**
     * Normalizes a quaternion.
     *
     * @param q The quaternion to be normalized.
     * @return The normalized quaternion.
     */
    public static Quaternion normalize(Quaternion q) {
        double magnitude = Math.sqrt(q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z);
        return new Quaternion(
                q.w / magnitude,
                q.x / magnitude,
                q.y / magnitude,
                q.z / magnitude
        );
    }

    /**
     * Creates a quaternion from an axis-angle representation.
     *
     * @param axis The axis of rotation.
     * @param angleDegrees The angle of rotation in degrees.
     * @return The resulting quaternion.
     */
    public static Quaternion fromAxisAngle(Vector3D axis, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double halfAngle = angleRadians / 2;
        double sinHalfAngle = Math.sin(halfAngle);

        return new Quaternion(
                Math.cos(halfAngle),
                axis.getX() * sinHalfAngle,
                axis.getY() * sinHalfAngle,
                axis.getZ() * sinHalfAngle
        );
    }

    /**
     * Multiplies two quaternions.
     *
     * @param a The first quaternion.
     * @param b The second quaternion.
     * @return The resulting quaternion from the multiplication.
     */
    public static Quaternion multiply(Quaternion a, Quaternion b) {
        return new Quaternion(
                a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z,
                a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y,
                a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x,
                a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quaternion that = (Quaternion) obj;
        return Double.compare(that.w, w) == 0 &&
                Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0;
    }
}
