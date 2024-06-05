package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

public class ColorTools {
    public static Color addColor(Color original, Color otherColor) {
        return new Color(
                (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0),
                (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0),
                (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0)
        );
    }

    public static Color addWeightedColor(Color color1, Color color2, double weight){
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

    public static Color getComplementaryColor(Color color) {
        return new Color(
            255 - color.getRed(),
            255 - color.getGreen(),
            255 - color.getBlue()
        );
    }

    public static Color scaleColor(Color color, double scale) {
        return new Color(
            (int) Math.clamp(color.getRed() * scale, 0, 255),
            (int) Math.clamp(color.getGreen() * scale, 0, 255),
            (int) Math.clamp(color.getBlue() * scale, 0, 255)
        );
    }

    public static Color getAmbientColor(Color objColor, double ambientIntensity){
        return new Color((float)(objColor.getRed() / 255.0 * ambientIntensity),
                (float)(objColor.getGreen() / 255.0 * ambientIntensity),
                (float)(objColor.getBlue() / 255.0 * ambientIntensity));
    }

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

    public static Color blendColors(Color color1, Color color2) {
        return new Color(
            (color1.getRed() + color2.getRed()) / 2,
            (color1.getGreen() + color2.getGreen()) / 2,
            (color1.getBlue() + color2.getBlue()) / 2
        );
    }
}
