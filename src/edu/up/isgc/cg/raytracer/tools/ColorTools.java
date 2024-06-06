package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

/**
 * Utility class for performing various color operations.
 *
 * @author José Salcedo
 */
public class ColorTools {

    /**
     * Adds two colors together, clamping the result to the range [0, 1].
     *
     * @param original The first color.
     * @param otherColor The second color.
     * @return The resulting color from adding the two colors.
     */
    public static Color addColor(Color original, Color otherColor) {
        return new Color(
                (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0),
                (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0),
                (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0)
        );
    }

    /**
     * Adds two colors together with a weighted blend.
     *
     * @param color1 The first color.
     * @param color2 The second color.
     * @param weight The weight for the first color (0-1).
     * @return The resulting color from the weighted blend.
     */
    public static Color addWeightedColor(Color color1, Color color2, double weight) {
        double invWeight = 1 - weight;
        Color weightedColor1 = new Color(
                (int) (color1.getRed() * weight),
                (int) (color1.getGreen() * weight),
                (int) (color1.getBlue() * weight)
        );
        Color weightedColor2 = new Color(
                (int) (color2.getRed() * invWeight),
                (int) (color2.getGreen() * invWeight),
                (int) (color2.getBlue() * invWeight)
        );
        return addColor(weightedColor1, weightedColor2);
    }

    /**
     * Gets the complementary color of a given color.
     *
     * @param color The original color.
     * @return The complementary color.
     */
    public static Color getComplementaryColor(Color color) {
        return new Color(
                255 - color.getRed(),
                255 - color.getGreen(),
                255 - color.getBlue()
        );
    }

    /**
     * Scales a color by a given factor, clamping the result to the range [0, 255].
     *
     * @param color The original color.
     * @param scale The scaling factor.
     * @return The scaled color.
     */
    public static Color scaleColor(Color color, double scale) {
        return new Color(
                (int) Math.clamp(color.getRed() * scale, 0, 255),
                (int) Math.clamp(color.getGreen() * scale, 0, 255),
                (int) Math.clamp(color.getBlue() * scale, 0, 255)
        );
    }

    /**
     * Gets the ambient color of a given color with a specified ambient intensity.
     *
     * @param objColor The original color.
     * @param ambientIntensity The ambient intensity.
     * @return The ambient color.
     */
    public static Color getAmbientColor(Color objColor, double ambientIntensity) {
        return new Color(
                (float)(objColor.getRed() / 255.0 * ambientIntensity),
                (float)(objColor.getGreen() / 255.0 * ambientIntensity),
                (float)(objColor.getBlue() / 255.0 * ambientIntensity)
        );
    }

    /**
     * Applies Beer's Law to a color to simulate the effect of light absorption in a material.
     *
     * @param refractedColor The color of the refracted light.
     * @param material The material properties.
     * @param distance The distance the light travels through the material.
     * @return The color after applying Beer's Law.
     */
    public static Color getBeersLawColor(Color refractedColor, Material material, double distance) {
        double absorbedRed = material.getAbsorption() * distance * (material.getColor().getRed() / 255.0);
        double absorbedGreen = material.getAbsorption() * distance * (material.getColor().getGreen() / 255.0);
        double absorbedBlue = material.getAbsorption() * distance * (material.getColor().getBlue() / 255.0);

        double transparencyRed = Math.exp(-absorbedRed);
        double transparencyGreen = Math.exp(-absorbedGreen);
        double transparencyBlue = Math.exp(-absorbedBlue);

        return new Color(
                (int) Math.min(255, refractedColor.getRed() * transparencyRed),
                (int) Math.min(255, refractedColor.getGreen() * transparencyGreen),
                (int) Math.min(255, refractedColor.getBlue() * transparencyBlue)
        );
    }

    /**
     * Blends two colors together by averaging their RGB components.
     *
     * @param color1 The first color.
     * @param color2 The second color.
     * @return The resulting blended color.
     */
    public static Color blendColors(Color color1, Color color2) {
        return new Color(
                (color1.getRed() + color2.getRed()) / 2,
                (color1.getGreen() + color2.getGreen()) / 2,
                (color1.getBlue() + color2.getBlue()) / 2
        );
    }
}
