package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.ColorTools;
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
    private static final double AMBIENT_INTENSITY = 1e-2;
    private static final double EPSILON = 1e-3;
    private static final int MAX_RAY_DEPTH = 8;

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
                Material.MIRROR.colored(Color.DARK_GRAY)));

        scene.addObject(new Model3D(new Vector3D(0, -1, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, -50, 50), new Vector3D(100, 50,
                                50)),
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, 50, 50), new Vector3D(-100, 50, 50))
                },
                Material.MATTE.colored(Color.DARK_GRAY)));

        scene.addObject(new Sphere(new Vector3D(1, 0, 1), 1, Material.GLASS));

        Material sphere_material = new Material(0.0, 1.5, 50, 0.1);

        // Background object
        scene.addObject(new Sphere(new Vector3D(.5, 1, 3), 1.0, sphere_material.colored(Color.GREEN)));
        scene.addObject(new Sphere(new Vector3D(2.2, 0, 5), 0.5, sphere_material.colored(Color.RED)));

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

                    Color pixelColor = calculateColor(mainCamera, mainCamera, objects, lights, ray, planes, 0);
                    image.setRGB(i, j, pixelColor.getRGB());
                }
            }
        };
    }

    private static Color calculateColor(Camera camera, Object3D caster, List<Object3D> objects, List<Light> lights, Ray ray, double[] clippingPlanes, int depth) {
        Intersection closestIntersection = raycast(ray, objects, caster, clippingPlanes);
        Color pixelColor = Color.BLACK;

        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getMaterial().getColor();

            for (Light light : lights) {
                if (!isShadowed(closestIntersection, light, objects, clippingPlanes)) {
                    Color diffuse = calculateDiffuseColor(closestIntersection, light, objColor);
                    pixelColor = ColorTools.addColor(pixelColor, diffuse);
                    if (closestIntersection.getObject().getMaterial().getShininess() < Material.MAX_SHININESS) {
                        Color specular = calculateSpecularColor(closestIntersection, light, camera.getPosition());
                        Color ambientColor = ColorTools.getAmbientColor(objColor, AMBIENT_INTENSITY);
                        pixelColor = ColorTools.addColor(pixelColor, specular);
                        pixelColor = ColorTools.addColor(pixelColor, ambientColor);
                    }
                }
            }

            if (closestIntersection.getObject().getMaterial().getReflectivity() > 0.0 && depth <= MAX_RAY_DEPTH) {
                Color reflectedColor = calculateReflectionColor(camera, ray, closestIntersection, objects, lights, clippingPlanes, depth);
                reflectedColor = ColorTools.addWeightedColor(reflectedColor, pixelColor, closestIntersection.getObject().getMaterial().getReflectivity());

                if (closestIntersection.getObject().getMaterial().getRefractivity() == 0) {
                    pixelColor = reflectedColor;
                } else {
                    pixelColor = calculateRefractedColor(camera, ray, closestIntersection, objects, lights, clippingPlanes, depth, reflectedColor);
                }
            }
        }
        return pixelColor;
    }

    public static Color calculateDiffuseColor(Intersection closestIntersection, Light light, Color objColor) {
        double nDotL = light.getNDotL(closestIntersection);
        Color lightColor = light.getMaterial().getColor();
        double intensity = light.getIntensity() * nDotL;

        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
            objColors[colorIndex] *= intensity * lightColors[colorIndex];
        }

        return new Color(
                (float) Math.clamp(objColors[0], 0.0, 1.0),
                (float) Math.clamp(objColors[1], 0.0, 1.0),
                (float) Math.clamp(objColors[2], 0.0, 1.0)
        );
    }

    public static Color calculateSpecularColor(Intersection closestIntersection, Light light, Vector3D viewerPosition) {
        Vector3D L = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
        Vector3D V = Vector3D.normalize(Vector3D.substract(viewerPosition, closestIntersection.getPosition()));
        Vector3D H = Vector3D.normalize(Vector3D.add(L, V));

        double NdotH = Math.max(Vector3D.dotProduct(closestIntersection.getNormal(), H), 0);
        double specularIntensity = Math.pow(NdotH, closestIntersection.getObject().getMaterial().getShininess());

        return new Color(
                (float) (light.getMaterial().getColor().getRed() / 255.0 * specularIntensity),
                (float) (light.getMaterial().getColor().getGreen() / 255.0 * specularIntensity),
                (float) (light.getMaterial().getColor().getBlue() / 255.0 * specularIntensity)
        );
    }

    public static Color calculateRefractedColor(Camera camera, Ray ray, Intersection intersection, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth, Color reflectedColor) {
        Vector3D offset = Vector3D.scalarMultiplication(intersection.getNormal(), -EPSILON);
        Vector3D refractedOrigin = Vector3D.add(intersection.getPosition(), offset);

        Vector3D normal = intersection.getNormal().clone();
        double c1 = Vector3D.dotProduct(ray.getDirection(), normal);
        double n1 = 1;
        double n2 = intersection.getObject().getMaterial().getRefractivity();

        if (c1 < 0) {
            c1 = -c1;
        } else {
            n1 = intersection.getObject().getMaterial().getRefractivity();
            n2 = 1;
            normal = Vector3D.scalarMultiplication(normal, -1);
        }

        double n = n1 / n2;
        double k = 1 - Math.pow(n, 2) * (1 - Math.pow(c1, 2));

        if (k < 0) {
            return reflectedColor;
        }

        double c2 = Math.sqrt(k);
        Vector3D T = Vector3D.normalize(Vector3D.add(Vector3D.scalarMultiplication(ray.getDirection(), n), Vector3D.scalarMultiplication(normal, (n * c1 - c2))));

        Ray refractedRay = new Ray(refractedOrigin, T);
        Color totalRefractedColor = Color.BLACK;
        double totalWeight = 0.0;

        Color refractedColor = calculateColor(
                camera,
                intersection.getObject(),
                objects,
                lights,
                refractedRay,
                clippingPlanes,
                depth
        );

        for (Light light : lights) {
            double nDotL = light.getNDotL(intersection);
            if (!isShadowed(intersection, light, objects, clippingPlanes)) {
                double r0 = Math.pow((n1 - n2) / (n1 + n2), 2);
                double rTheta = r0 + (1 - r0) * Math.pow(1 - c1, 5);
                refractedColor = ColorTools.addWeightedColor(reflectedColor, refractedColor, rTheta);

                // Beer's Law
//                Intersection exitIntersection = raycast(refractedRay, objects, intersection.getObject(), clippingPlanes);
//                if (exitIntersection != null) {
//                    double distance = Vector3D.magnitude(Vector3D.substract(intersection.getPosition(), exitIntersection.getPosition()));
//                    Color complementaryColor = ColorTools.getComplementaryColor(intersection.getObject().getMaterial().getColor());
//                    refractedColor = ColorTools.getBeersLawColor(complementaryColor, intersection.getObject().getMaterial(), distance);
//                }

                refractedColor = ColorTools.scaleColor(refractedColor, nDotL);
                totalRefractedColor = ColorTools.addColor(totalRefractedColor, refractedColor);
                totalWeight += nDotL;
            }
        }

        if (totalWeight > 0) {
            totalRefractedColor = ColorTools.scaleColor(totalRefractedColor, 1.0 / totalWeight);
        }

        return totalRefractedColor;
    }

    public static Color calculateReflectionColor(Camera camera, Ray ray, Intersection closestIntersection, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth) {
        Vector3D reflectedVector = reflect(ray.getDirection(), closestIntersection.getNormal());
        Vector3D offset = Vector3D.scalarMultiplication(closestIntersection.getNormal(), EPSILON);
        Vector3D reflectedOrigin = Vector3D.add(closestIntersection.getPosition(), offset);
        Ray reflectedRay = new Ray(reflectedOrigin, reflectedVector);

        Color initialColor = Color.BLACK;
        double weight = 0.0;

        for (Light light : lights) {
            double nDotL = light.getNDotL(closestIntersection);
            if (!isShadowed(closestIntersection, light, objects, clippingPlanes)) {
                Color reflectedColor = calculateColor(
                        camera,
                        closestIntersection.getObject(),
                        objects,
                        List.of(light),
                        reflectedRay,
                        clippingPlanes,
                        depth + 1);

                reflectedColor = ColorTools.scaleColor(reflectedColor, nDotL * 0.9);
                initialColor = ColorTools.addColor(initialColor, reflectedColor);
                weight += nDotL;
            }
        }

        if (weight > 0) {
            initialColor = ColorTools.scaleColor(initialColor, 1.0 / weight);
        }

        return initialColor;
    }

    public static boolean isShadowed(Intersection origin, Light light, List<Object3D> objects, double[] clippingPlanes) {
        Vector3D lightDirection = Vector3D.normalize(Vector3D.substract(light.getPosition(), origin.getPosition()));
        Ray ray = new Ray(origin.getPosition(), lightDirection);
        Intersection obstacle = raycast(ray, objects, origin.getObject(), clippingPlanes);

        double distanceToLight = Vector3D.magnitude(Vector3D.substract(light.getPosition(), origin.getPosition()));
        if (obstacle == null)
            return false;
        else {
            double distanceToObstacle = Vector3D.magnitude(Vector3D.substract(obstacle.getPosition(), origin.getPosition()));
            if (distanceToLight < distanceToObstacle) {
                return false;
            } else if (obstacle.getObject().getMaterial().getRefractivity() > 0) {
                return isShadowed(obstacle, light, objects, clippingPlanes);
            } else {
                return true;
            }
        }
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

    public static Vector3D reflect(Vector3D I, Vector3D N) {
        return Vector3D.substract(I, Vector3D.scalarMultiplication(N, Vector3D.dotProduct(I, N) * 2.0));
    }
}