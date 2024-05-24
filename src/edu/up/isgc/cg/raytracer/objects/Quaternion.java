package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Vector3D;
public class Quaternion {
    public double w;
    public double x;
    public double y;
    public double z;

    public static Quaternion IDENTITY = new Quaternion(1,0,0,0);

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Rotates a Vector3 by this Quaternion
    public Vector3D rotate(Vector3D vector) {
        // Quaternion multiplication (p * q * p^(-1))
        // q is the quaternion, p is the vector treated as a quaternion with w=0
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(w, -x, -y, -z);

        // First quaternion multiplication (q * p)
        Quaternion temp = multiply(this, vectorQuat);

        // Second quaternion multiplication (result * conjugate of q)
        Quaternion rotatedQuat = multiply(temp, conj);

        // Update the vector components
        return new Vector3D(rotatedQuat.x,rotatedQuat.y,rotatedQuat.z);
    }

    public void rotateWithMutation(Vector3D vector) {
        // Quaternion multiplication (p * q * p^(-1))
        // q is the quaternion, p is the vector treated as a quaternion with w=0
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(w, -x, -y, -z);

        // First quaternion multiplication (q * p)
        Quaternion temp = multiply(this, vectorQuat);

        // Second quaternion multiplication (result * conjugate of q)
        Quaternion rotatedQuat = multiply(temp, conj);

        // Update the vector components
        vector.setX(rotatedQuat.x);
        vector.setY(rotatedQuat.y);
        vector.setZ(rotatedQuat.z);
    }

    public static Vector3D rotate(Vector3D vector, Quaternion q) {
        // Quaternion multiplication (p * q * p^(-1))
        // q is the quaternion, p is the vector treated as a quaternion with w=0
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(q.w, -q.x, -q.y, -q.z);

        // First quaternion multiplication (q * p)
        Quaternion temp = multiply(q, vectorQuat);

        // Second quaternion multiplication (result * conjugate of q)
        Quaternion rotatedQuat = multiply(temp, conj);

        // Update the vector components
        return new Vector3D(rotatedQuat.x,rotatedQuat.y,rotatedQuat.z);
    }

    public static void rotateWithMutation(Vector3D vector, Quaternion q) {
        // Quaternion multiplication (p * q * p^(-1))
        // q is the quaternion, p is the vector treated as a quaternion with w=0
        Quaternion vectorQuat = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion conj = new Quaternion(q.w, -q.x, -q.y, -q.z);

        // First quaternion multiplication (q * p)
        Quaternion temp = multiply(q, vectorQuat);

        // Second quaternion multiplication (result * conjugate of q)
        Quaternion rotatedQuat = multiply(temp, conj);

        // Update the vector components
        vector.setX(rotatedQuat.x);
        vector.setY(rotatedQuat.y);
        vector.setZ(rotatedQuat.z);
    }

    private static Quaternion multiply(Quaternion a, Quaternion b) {
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
