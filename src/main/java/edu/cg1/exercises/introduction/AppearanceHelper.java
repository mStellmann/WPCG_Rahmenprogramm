/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package main.java.edu.cg1.exercises.introduction;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Primitive;

/**
 * @author Philipp Jenke
 * 
 */
public class AppearanceHelper {

    /**
     * Set the color for a primitive.
     * 
     * @param primitive
     *            Primitive 3D object.
     * @param diffuseColor
     *            (Diffuse) color in the appearance of the primitive.
     */
    public static void setColor(Primitive primitive, Color3f diffuseColor) {
        Color3f emissiveColor = new Color3f(0, 0, 0);
        Color3f ambientColor = new Color3f(0, 0, 0);
        Color3f specularColor = new Color3f(1, 1, 1);
        float shininessValue = 128.0f;
        Material material = new Material(ambientColor, emissiveColor,
                diffuseColor, specularColor, shininessValue);
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        primitive.setAppearance(ap);
    }

    /**
     * Set the color for a shape.
     * 
     * @param shape
     *            Shape object.
     * @param diffuseColor
     *            Diffuse color of the object.
     */
    public static void setColor(Shape3D shape, Color3f diffuseColor) {
        Color3f emissiveColor = new Color3f(0, 0, 0);
        Color3f specularColor = new Color3f(10.0f, 10.0f, 10.0f);
        Color3f ambientColor = new Color3f(0, 0, 0);
        float shininessValue = 128.0f;
        Material material = new Material(ambientColor, emissiveColor,
                diffuseColor, specularColor, shininessValue);
        Appearance ap = new Appearance();
        ap.setMaterial(material);
        shape.setAppearance(ap);
    }
}
