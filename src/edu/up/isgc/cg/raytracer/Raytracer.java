package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.Material;
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
    private static final double EPSILON = 1e-3;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println(new Date());

        Scene scene = new Scene();
        scene.setCamera(new Camera(new Vector3D(0, 0, -5), 60, 60, 800, 800, 0.6, 60.0));
        scene.addLight(new PointLight(new Vector3D(0.0, 5.0, -5.0), Material.NONE, 1));

        scene.addObject(new Model3D(new Vector3D(0, -1, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, -100), new Vector3D(100, 0, 100)),
                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, 100), new Vector3D(-100, 0, 100))
                },
                Material.MIRROR.instantiateWithColor(Color.DARK_GRAY)));

        scene.addObject(new Model3D(new Vector3D(0, -1, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, -50, 50), new Vector3D(100, 50,
                                50)),
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, 50, 50), new Vector3D(-100, 50, 50))
                },
                Material.MATTE.instantiateWithColor(Color.DARK_GRAY)));

        scene.addObject(new Sphere(new Vector3D(1, 0, 1), 1, Material.GLASS));

        Material sphere_material = new Material(0.0, 1.0, 50);

        // Background object
        scene.addObject(new Sphere(new Vector3D(-2, 0, 5), 1.0, sphere_material.instantiateWithColor(Color.GREEN)));
        scene.addObject(new Sphere(new Vector3D(2, 0, 5), 0.5, sphere_material.instantiateWithColor(Color.RED)));

        BufferedImage image = parallelImageRaytracing(scene);
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

                    double x = posRaytrace[i][j].getX() + pos.getX();
                    double y = posRaytrace[i][j].getY() + pos.getY();
                    double z = posRaytrace[i][j].getZ() + pos.getZ();
                    double[] planes = new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]};

                    Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));

                    Color pixelColor = calculateColor(mainCamera, objects, lights, ray, planes, 0);
                    image.setRGB(i, j, pixelColor.getRGB());
                }
            }
        };
    }

    private static Color calculateColor(Object3D caster, List<Object3D> objects, List<Light> lights, Ray ray, double[] clippingPlanes, int depth) {
        Intersection closestIntersection = raycast(ray, objects, caster, clippingPlanes);

        Color pixelColor = Color.BLACK;
        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getMaterial().getColor();

            // Calculate lighting
            for (Light light : lights) {
                double nDotL = light.getNDotL(closestIntersection);
                Color lightColor = light.getMaterial().getColor();
                double intensity = light.getIntensity() * nDotL;

                Vector3D lightDir = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
                Vector3D viewDir = Vector3D.normalize(Vector3D.substract(caster.getPosition(), closestIntersection.getPosition()));
                Vector3D halfwayDir = Vector3D.normalize(Vector3D.add(lightDir, viewDir));

                double specularIntensity = Math.pow(Math.max(Vector3D.dotProduct(halfwayDir, closestIntersection.getNormal()), 0), closestIntersection.getObject().getMaterial().getShininess());

                double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                double[] diffuseColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                double[] specularColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};

                for (int colorIndex = 0; colorIndex < diffuseColors.length; colorIndex++) {
                    diffuseColors[colorIndex] *= intensity * lightColors[colorIndex];
                    specularColors[colorIndex] *= specularIntensity * lightColors[colorIndex];
                }

                Color diffuse = new Color((float) Math.clamp(diffuseColors[0], 0.0, 1.0),
                        (float) Math.clamp(diffuseColors[1], 0.0, 1.0),
                        (float) Math.clamp(diffuseColors[2], 0.0, 1.0));
//
                Color specular = new Color((float) Math.clamp(specularColors[0], 0.0, 1.0),
                        (float) Math.clamp(specularColors[1], 0.0, 1.0),
                        (float) Math.clamp(specularColors[2], 0.0, 1.0));

                pixelColor = addColor(specular, diffuse);
            }

            // Handle reflection
            if (closestIntersection.getObject().getMaterial().getReflectivity() > 0.0 && depth < MAX_RAY_DEPTH) {
                Vector3D reflectedVector = reflect(ray.getDirection(), closestIntersection.getNormal());
                Vector3D offset = Vector3D.scalarMultiplication(closestIntersection.getNormal(), EPSILON);
                Vector3D reflectedOrigin = Vector3D.add(closestIntersection.getPosition(), offset);
                Ray reflectedRay = new Ray(reflectedOrigin, reflectedVector);
                Color reflectedColor = calculateColor(closestIntersection.getObject(), objects, lights, reflectedRay, clippingPlanes, depth + 1);
                pixelColor = blendColors(pixelColor, reflectedColor, closestIntersection.getObject().getMaterial().getReflectivity());
            }

            // Handle refraction
            if (closestIntersection.getObject().getMaterial().getRefractivity() > 1.0 && depth < MAX_RAY_DEPTH) {
                double n1 = caster == null ? 1.0 : caster.getMaterial().getRefractivity(); // Air refraction index
                double n2 = closestIntersection.getObject().getMaterial().getRefractivity();
                Vector3D refractedDir = refract(ray.getDirection(), closestIntersection.getNormal(), n1, n2);
                if (refractedDir != null) { // If not total internal reflection
                    Vector3D offset = Vector3D.scalarMultiplication(closestIntersection.getNormal(), EPSILON);
                    Vector3D refractedOrigin = Vector3D.add(closestIntersection.getPosition(), offset);
                    Ray refractedRay = new Ray(refractedOrigin, refractedDir);
                    Color refractedColor = calculateColor(closestIntersection.getObject(), objects, lights, refractedRay, clippingPlanes, depth + 1);
                    pixelColor = blendColors(pixelColor, refractedColor, closestIntersection.getObject().getMaterial().getRefractivity());
                }
            }
        }
        return pixelColor;
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (Object3D currObj : objects) {
            if (!currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }

    public static Color blendColors(Color baseColor, Color blendColor, double blendFactor) {
        int r = (int) (baseColor.getRed() * (1 - blendFactor) + blendColor.getRed() * blendFactor);
        int g = (int) (baseColor.getGreen() * (1 - blendFactor) + blendColor.getGreen() * blendFactor);
        int b = (int) (baseColor.getBlue() * (1 - blendFactor) + blendColor.getBlue() * blendFactor);
        return new Color(Math.clamp(r, 0, 255), Math.clamp(g, 0, 255), Math.clamp(b, 0, 255));
    }

    public static Vector3D reflect(Vector3D I, Vector3D N) {
        return Vector3D.substract(I, Vector3D.scalarMultiplication(N, Vector3D.dotProduct(I, N) * 2.0));
    }

    public static Vector3D refract(Vector3D incident, Vector3D normal, double n1, double n2) {
        double ratio = n1 / n2;
        double cosI = -Vector3D.dotProduct(normal, incident);
        double sinT2 = ratio * ratio * (1.0 - cosI * cosI);
        if (sinT2 > 1.0) {
            return null; // Total internal reflection
        }
        double cosT = Math.sqrt(1.0 - sinT2);
        return Vector3D.add(Vector3D.scalarMultiplication(incident, ratio), Vector3D.scalarMultiplication(normal, ratio * cosI - cosT));
    }
}