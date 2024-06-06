package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.objects.Object3D;
import edu.up.isgc.cg.raytracer.objects.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * The Scene class represents a 3D scene containing a camera, objects, and lights.
 * It is used to manage and render all the components of a scene in ray tracing.
 *
 * @author Jafet Rodriguez, José Salcedo
 */
public class Scene {
    private Camera camera;
    private List<Object3D> objects;
    private List<Light> lights;

    /**
     * Constructs a new Scene with empty lists of objects and lights.
     */
    public Scene() {
        setObjects(new ArrayList<>());
        setLights(new ArrayList<>());
    }

    /**
     * Gets the camera of the scene.
     *
     * @return The camera of the scene.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the camera of the scene.
     *
     * @param camera The new camera of the scene.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Adds an object to the scene.
     *
     * @param object The object to add.
     */
    public void addObject(Object3D object) {
        getObjects().add(object);
    }

    /**
     * Gets the list of objects in the scene.
     *
     * @return The list of objects in the scene.
     */
    public List<Object3D> getObjects() {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        return objects;
    }

    /**
     * Sets the list of objects in the scene.
     *
     * @param objects The new list of objects in the scene.
     */
    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    /**
     * Gets the list of lights in the scene.
     *
     * @return The list of lights in the scene.
     */
    public List<Light> getLights() {
        if (lights == null) {
            lights = new ArrayList<>();
        }
        return lights;
    }

    /**
     * Sets the list of lights in the scene.
     *
     * @param lights The new list of lights in the scene.
     */
    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    /**
     * Adds a light to the scene.
     *
     * @param light The light to add.
     */
    public void addLight(Light light) {
        getLights().add(light);
    }
}
