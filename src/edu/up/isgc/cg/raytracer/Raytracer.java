package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Raytracer {
    private static final int MAX_RAY_DEPTH = 3;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Quaternion rotation = new Quaternion(0.7071f, 0, 0, 0.7071f);

        System.out.println(new Date());

        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60, 800, 800, 0.6, 50.0));
        scene02.addLight(new PointLight(new Vector3D(0.0, 1.0, 0.0), Color.WHITE, 0.8));
        scene02.addObject(new Sphere(new Vector3D(0.0, 1.0, 5.0), 0.5, Color.RED));
        scene02.addObject(new Sphere(new Vector3D(0.5, 1.0, 4.5), 0.25, new Color(200, 255, 0, 1)));
        scene02.addObject(new Sphere(new Vector3D(0.35, 1.0, 4.5), 0.3, Color.BLUE));
        scene02.addObject(new Sphere(new Vector3D(4.85, 1.0, 4.5), 0.3, Color.PINK));
        scene02.addObject(new Sphere(new Vector3D(2.85, 1.0, 304.5), 0.5, Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("CubeQuad.obj", new Vector3D(-3.0, -2.5, 3.0), Color.GREEN));

        Model3D teapot = OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE);
        Model3D cube = OBJReader.getModel3D("Cube.obj", new Vector3D(0f, -2.5, 10.0), Color.WHITE);

        teapot.setScale(0.7);
        teapot.setRotation(rotation);

        cube.setMaterial(Material.REFLECTIVE);

        scene02.addObject(teapot);
        scene02.addObject(cube);
        scene02.addObject(OBJReader.getModel3D("Ring.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE));

        BufferedImage image = parallelImageRaytracing(scene02);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Process duration: " + ((double) duration / (1_000_000_000)) + " seconds");
    }

    public static BufferedImage imageRaytracing(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector3D pos = mainCamera.getPosition();
        double cameraZ = pos.getZ();

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                raytracePixel(image, mainCamera, objects, lights, posRaytrace, pos, nearFarPlanes, cameraZ, i, j);
            }
        }

        return image;
    }

    public static BufferedImage parallelImageRaytracing(Scene scene) {
        int nThreads = 16;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        Camera mainCamera = scene.getCamera();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();

        int stepX = posRaytrace[0].length / nThreads;
        int stepY = posRaytrace.length / nThreads;

        for (int i = 0; i < stepY; i++) {
            for (int j = 0; j < stepX; j++) {
                int startX = j * stepX;
                int startY = i * stepY;

                Runnable runnable = raytraceSection(
                        startX,
                        startX + stepX,
                        startY,
                        startY + stepY,
                        image,
                        mainCamera,
                        objects,
                        lights,
                        posRaytrace
                );

                executorService.execute(runnable);
            }
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(15, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!executorService.isShutdown()) {
                System.err.println("Cancel non-finished");
            }
        }
        executorService.shutdownNow();

        return image;
    }

    private static Runnable raytraceSection(int startX, int endX, int startY, int endY, BufferedImage image, Camera mainCamera, List<Object3D> objects, List<Light> lights, Vector3D[][] posRaytrace) {
        return () -> {
            Vector3D pos = mainCamera.getPosition();
            double[] nearFarPlanes = mainCamera.getNearFarPlanes();
            double cameraZ = pos.getZ();

            for (int i = startY; i < endY; i++) {
                for (int j = startX; j < endX; j++) {

                    // Bound check
                    if (i >= posRaytrace.length || j >= posRaytrace[i].length) {
                        continue;
                    }

                    raytracePixel(image, mainCamera, objects, lights, posRaytrace, pos, nearFarPlanes, cameraZ, i, j);
                }
            }
        };
    }

    private static void raytracePixel(BufferedImage image, Camera mainCamera, List<Object3D> objects, List<Light> lights, Vector3D[][] posRaytrace, Vector3D pos, double[] nearFarPlanes, double cameraZ, int i, int j) {
        double x = posRaytrace[i][j].getX() + pos.getX();
        double y = posRaytrace[i][j].getY() + pos.getY();
        double z = posRaytrace[i][j].getZ() + pos.getZ();

        Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
        Intersection closestIntersection = raycast(ray, objects, null,
                new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]}, 1);

        Color pixelColor = Color.BLACK;
        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getColor();

            for (Light light : lights) {
                double nDotL = light.getNDotL(closestIntersection);
                Color lightColor = light.getColor();
                double intensity = light.getIntensity() * nDotL;

                Vector3D lightDir = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
                Vector3D viewDir = Vector3D.normalize(Vector3D.substract(mainCamera.getPosition(), closestIntersection.getPosition()));
                Vector3D halfwayDir = Vector3D.normalize(Vector3D.add(lightDir, viewDir));

                double spec = Math.pow(Math.max(Vector3D.dotProduct(halfwayDir, closestIntersection.getNormal()), 0), 25);

                double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                    objColors[colorIndex] *= intensity * lightColors[colorIndex];
                }

                Color diffuse = new Color((float) Math.clamp(objColors[0], 0.0, 1.0),
                        (float) Math.clamp(objColors[1], 0.0, 1.0),
                        (float) Math.clamp(objColors[2], 0.0, 1.0));

                Color specular = new Color((float) Math.clamp(spec, 0.0, 1.0),
                        (float) Math.clamp(spec, 0.0, 1.0),
                        (float) Math.clamp(spec, 0.0, 1.0));

                pixelColor = addColor(specular, diffuse);
            }
        }
        image.setRGB(i, j, pixelColor.getRGB());
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes, int depth) {
        if (depth >= MAX_RAY_DEPTH) {
            return null;
        }

        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        switch (intersection.getObject().getMaterial()) {
                            case DIFFUSE -> closestIntersection = intersection;
                            case REFLECTIVE -> {
                                Ray reflection_ray = new Ray(intersection.getPosition(), reflect(ray.getDirection(), intersection.getNormal()));
                                Intersection new_intersection = raycast(reflection_ray, objects, caster, clippingPlanes, depth + 1);

                                if (new_intersection != null) {
                                    closestIntersection = new_intersection;
                                }
                            }
                        }
                    }
                }
            }
        }

        return closestIntersection;
    }

    public static Vector3D reflect(Vector3D I, Vector3D N) {
        return Vector3D.substract(I, Vector3D.scalarMultiplication(N,Vector3D.dotProduct(I, N) * 2.0));
    }
}
