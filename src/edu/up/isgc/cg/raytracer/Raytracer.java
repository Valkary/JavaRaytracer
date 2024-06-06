package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.Light;
import edu.up.isgc.cg.raytracer.lights.PointLight;
import edu.up.isgc.cg.raytracer.lights.SpotLight;
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
    private static final int MAX_RAY_DEPTH = 3;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println(new Date());

        Vector3D xAxis = new Vector3D(1, 0, 0);
        Vector3D yAxis = new Vector3D(0, 0, 1);
        Vector3D zAxis = new Vector3D(0, 1, 0);
//        Quaternion rotationY = Quaternion.fromAxisAngle(yAxis, 30);
//        Quaternion rotationX = Quaternion.fromAxisAngle(xAxis, 15);
//        Quaternion combinedRotation = Quaternion.normalize(Quaternion.multiply(rotationY, rotationX));
//        Quaternion rotationXUFO = Quaternion.fromAxisAngle(xAxis, 10);
//        Quaternion rotationZUFO = Quaternion.fromAxisAngle(zAxis, 30);
//        Quaternion ufoRotation = Quaternion.normalize(Quaternion.multiply(rotationXUFO, rotationZUFO));
//        Scene scene01 = new Scene();
//        scene01.setCamera(new Camera(new Vector3D(0, 0, -5), 90, 60, 3840, 2160, 0.6, 60.0));
//        Vector3D sun_center = new Vector3D(-10.0, 10.0, -5.0);
//        scene01.addLight(new PointLight(sun_center, Material.NONE, 1));
//        Model3D saturn = OBJReader.getModel3D("Saturn.obj", new Vector3D(-3.5, 0, 3), Material.MATTE.colored(Color.PINK));
//        scene01.addLight(new SpotLight(sun_center, Vector3D.normalize(Vector3D.substract(sun_center, saturn.getPosition())), Material.NONE.colored(Color.YELLOW), 1, 60));
//        scene01.addObject(new Model3D(new Vector3D(0, -1, 0),
//                new Triangle[]{
//                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, -50, 50), new Vector3D(100, 50,
//                                50)),
//                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, 50, 50), new Vector3D(-100, 50, 50))
//                },
//                Material.MATTE.colored(Color.BLACK)));
//        Model3D ufo = OBJReader.getModel3D("UFO.obj", new Vector3D(3.5, 4, 4), Material.MATTE.colored(Color.GRAY));
//        Model3D steve = OBJReader.getModel3D("Steve.obj", new Vector3D(-1.5, -0.5, 1), Material.GLASS.colored(Color.GREEN));
//        ufo.setScale(0.5);
//        ufo.setRotation(ufoRotation);
//        steve.setScale(0.15);
//        steve.setRotation(combinedRotation);
//        scene01.addObject(saturn);
//        scene01.addObject(ufo);
//        scene01.addObject(steve);

//        Scene scene02 = new Scene();
//        scene02.setCamera(new Camera(new Vector3D(0, 0, -5), 90, 60, 1920, 1080, 0.6, 60.0));
//        scene02.addLight(new PointLight(new Vector3D(-10.0, 10.0, -5.0), Material.NONE, .6));
//        scene02.addObject(new Model3D(new Vector3D(0, -1, 0),
//                new Triangle[]{
//                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, -50, 50), new Vector3D(100, 50,
//                                50)),
//                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, 50, 50), new Vector3D(-100, 50, 50))
//                },
//                Material.MATTE.colored(new Color(0,0,50))));
//        scene02.addObject(new Model3D(new Vector3D(0, -1, 0),
//                new Triangle[]{
//                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, -100), new Vector3D(100, 0, 100)),
//                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, 100), new Vector3D(-100, 0, 100))
//                },
//                Material.MIRROR.colored(Color.DARK_GRAY)));
//        Model3D street_lamp = OBJReader.getModel3D("Scene02/StreetLamp.obj", new Vector3D(-3.5, 0, 3), Material.MATTE.colored(Color.LIGHT_GRAY));
//        Quaternion lampRotation = Quaternion.fromAxisAngle(zAxis, 30);
//        street_lamp.setRotation(lampRotation);
//        Model3D car = OBJReader.getModel3D("Scene02/Car.obj", new Vector3D(-3.0, 0, 2), Material.METAL.colored(Color.BLACK));
//        Quaternion carRotation = Quaternion.fromAxisAngle(zAxis, 30);
//        car.setRotation(carRotation);
//        Model3D cone = OBJReader.getModel3D("Scene02/Cone.obj", new Vector3D(-2, -2, -1.5), Material.MATTE.colored(Color.ORANGE));
//        Model3D hidrant = OBJReader.getModel3D("Scene02/FireHidrant.obj", new Vector3D(6.5, -1.5, 3), Material.MATTE.colored(Color.RED));
//        Model3D rain01 = OBJReader.getModel3D("Scene02/Rain.obj", new Vector3D(0,0,0), Material.GLASS);
//        Model3D rain02 = OBJReader.getModel3D("Scene02/Rain.obj", new Vector3D(0,0,0), Material.GLASS);
//        rain02.setRotation(carRotation);
//        scene02.addObject(rain01);
//        scene02.addObject(rain02);
//        scene02.addObject(street_lamp);
//        scene02.addObject(car);
//        scene02.addObject(cone);
//        scene02.addObject(hidrant);

        Scene scene03 = new Scene();
        scene03.setCamera(new Camera(new Vector3D(0, 0, -5), 60, 60, 400, 400, 0.6, 60.0));
        scene03.addObject(new Model3D(new Vector3D(0, -1, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, -100), new Vector3D(100, 0, 100)),
                        new Triangle(new Vector3D(-100, 0, -100), new Vector3D(100, 0, 100), new Vector3D(-100, 0, 100))
                },
                Material.MIRROR.colored(Color.DARK_GRAY)));
        scene03.addObject(new Model3D(new Vector3D(0, -1, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, -50, 50), new Vector3D(100, 50,
                                50)),
                        new Triangle(new Vector3D(-100, -50, 50), new Vector3D(100, 50, 50), new Vector3D(-100, 50, 50))
                },
                Material.MATTE.colored(new Color(0,0,50))));

        Material cera = new Material(Color.WHITE, 0.3, 0.0, 50, 0.0);
        Vector3D sceneOrigin = new Vector3D(0, -0.5, 2);
        scene03.addLight(new PointLight(new Vector3D(-10, 7, 5.45), Material.NONE.colored(Color.RED), .2));
        scene03.addLight(new PointLight(new Vector3D(15, 15, -10), Material.NONE, .7));

        Model3D base5 = OBJReader.getModel3D("Scene03/5base.obj", sceneOrigin, cera);
        Model3D base = OBJReader.getModel3D("Scene03/base.obj", sceneOrigin, cera);
        Model3D calaca = OBJReader.getModel3D("Scene03/calaca.obj", sceneOrigin, Material.GLASS);
        Model3D cartas = OBJReader.getModel3D("Scene03/cartas.obj", sceneOrigin, Material.METAL.colored(Color.WHITE));
        Model3D fuego = OBJReader.getModel3D("Scene03/fuego.obj", sceneOrigin, Material.MATTE.colored(Color.RED));
        Model3D rosa = OBJReader.getModel3D("Scene03/rosa.obj", sceneOrigin, Material.MATTE.colored(Color.PINK));
        Model3D verde = OBJReader.getModel3D("Scene03/verde.obj", sceneOrigin, Material.MATTE.colored(Color.GREEN));
        scene03.addObject(base5);
        scene03.addObject(base);
        scene03.addObject(calaca);
        scene03.addObject(cartas);
        scene03.addObject(fuego);
        scene03.addObject(rosa);
        scene03.addObject(verde);

        BufferedImage image = parallelImageRaytracing(scene03);
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
            if (!executorService.awaitTermination(1000, TimeUnit.MINUTES)) {
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
                depth + 1
        );

        for (Light light : lights) {
            double nDotL = light.getNDotL(intersection);
            if (!isShadowed(intersection, light, objects, clippingPlanes)) {
                double r0 = Math.pow((n1 - n2) / (n1 + n2), 2);
                double rTheta = r0 + (1 - r0) * Math.pow(1 - c1, 5);
                refractedColor = ColorTools.addWeightedColor(reflectedColor, refractedColor, rTheta);
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