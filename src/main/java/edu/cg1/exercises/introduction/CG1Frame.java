/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package main.java.edu.cg1.exercises.introduction;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;

/**
 * Central widget for the 3D graphics exercises.
 * 
 * @author Philipp Jenke
 * 
 */
public class CG1Frame extends JFrame {

    /**
     * Required for Serializable interface.
     */
    private static final long serialVersionUID = -8406043101882693554L;

    /**
     * Canvas object for the 3D content.
     */
    private Canvas3D canvas3D;

    /**
     * Simple universe (provides reasonable default values).
     */
    protected SimpleUniverse universe;

    /**
     * Scene graph for the 3D content scene.
     */
    protected BranchGroup scene = new BranchGroup();

    /**
     * Default constructor.
     */
    public CG1Frame() {
        // Create canvas object to draw on
        canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        // The SimpleUniverse provides convenient default settings
        universe = new SimpleUniverse(canvas3D);
        universe.getViewingPlatform().setNominalViewingTransform();

        // Setup lighting
        addLight(universe);

        // Allow for mouse control
        OrbitBehavior ob = new OrbitBehavior(canvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0),
                Double.MAX_VALUE));
        universe.getViewingPlatform().setViewPlatformBehavior(ob);

        // Set the background color
        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 100000);
        background.setApplicationBounds(sphere);
        scene.addChild(background);

        // Setup frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Einfuehrung in die Computergrafik");
        setSize(500, 500);
        getContentPane().add("Center", canvas3D);
        setVisible(true);
    }

    /**
     * Setup the lights in the scene. Attention: The light need to be added to
     * the scene before the scene in compiled (see createSceneGraph()).
     */
    private void addLight(SimpleUniverse universe) {
        addPointLight(new Point3f(1, 1, 1));
        addPointLight(new Point3f(-1, -1, -1));
        addPointLight(new Point3f(1, -1, 1));
        addDirectionalLight(new Vector3f(0, 0, 1));
    }

    void addPointLight(Point3f position) {
        PointLight light = new PointLight();
        light.setPosition(position);
        light.setColor(new Color3f(1, 1, 1));
        light.setInfluencingBounds(new BoundingSphere(
                new Point3d(0.0, 0.0, 0.0), 100.0));
        scene.addChild(light);
    }

    void addDirectionalLight(Vector3f direction) {
        DirectionalLight light = new DirectionalLight();
        light.setDirection(direction);
        light.setColor(new Color3f(1, 1, 1));
        light.setInfluencingBounds(new BoundingSphere(
                new Point3d(0.0, 0.0, 0.0), 100.0));
        scene.addChild(light);
    }

    /**
     * Create the default scene graph.
     */
    protected void createSceneGraph() { 
        // Example object
        Box box = new Box();
        AppearanceHelper.setColor(box, new Color3f(0.75f, 0.25f, 0.25f));
        Transform3D scale = new Transform3D();
        scale.setScale(0.25);
        TransformGroup transformation = new TransformGroup(scale);
        transformation.addChild(box);
        scene.addChild(transformation);

        // Add a coordinate system to the scene.
        scene.addChild(createCoordinateSystem());

        // Assemble scene
        scene.compile();
        universe.addBranchGraph(scene);
    }

    /**
     * Create a group to represent the coordinate system.
     * 
     * @return
     */
    protected Node createCoordinateSystem() {

        Group group = new Group();

        // X-coordinate axis - line
        Cylinder cylinderX = new Cylinder(0.02f, 1.0f);
        AppearanceHelper.setColor(cylinderX, new Color3f(1, 0, 0));
        Transform3D tX = new Transform3D();
        tX.setTranslation(new Vector3f(0, 0.5f, 0));
        TransformGroup cylinderXTransform = new TransformGroup(tX);
        cylinderXTransform.addChild(cylinderX);
        Transform3D tXRotate = new Transform3D();
        tXRotate.rotZ(-90.0 * Math.PI / 180.0);
        TransformGroup xAxis = new TransformGroup(tXRotate);
        xAxis.addChild(cylinderXTransform);
        group.addChild(xAxis);

        // X-coordinate axis - end point
        Sphere sphereX = new Sphere(0.03f);
        Transform3D tSphereX = new Transform3D();
        tSphereX.setTranslation(new Vector3f(1, 0, 0));
        TransformGroup tgSphereX = new TransformGroup(tSphereX);
        tgSphereX.addChild(sphereX);
        AppearanceHelper.setColor(sphereX, new Color3f(1, 0, 0));
        group.addChild(tgSphereX);

        // Y-coordinate axis - line
        Cylinder cylinderY = new Cylinder(0.02f, 1.0f);
        AppearanceHelper.setColor(cylinderY, new Color3f(0, 1, 0));
        Transform3D tY = new Transform3D();
        tY.setTranslation(new Vector3f(0, 0.5f, 0));
        TransformGroup cylinderYTransform = new TransformGroup(tY);
        cylinderYTransform.addChild(cylinderY);
        group.addChild(cylinderYTransform);

        // Y-coordinate axis - end point
        Sphere sphereY = new Sphere(0.03f);
        Transform3D tSphereY = new Transform3D();
        tSphereY.setTranslation(new Vector3f(0, 1, 0));
        TransformGroup tgSphereY = new TransformGroup(tSphereY);
        tgSphereY.addChild(sphereY);
        AppearanceHelper.setColor(sphereY, new Color3f(0, 1, 0));
        group.addChild(tgSphereY);

        // Z-coordinate axis - line
        Cylinder cylinderZ = new Cylinder(0.02f, 1.0f);
        AppearanceHelper.setColor(cylinderZ, new Color3f(0, 0, 1));
        Transform3D tZ = new Transform3D();
        tZ.setTranslation(new Vector3f(0, 0.5f, 0));
        TransformGroup cylinderZTransform = new TransformGroup(tZ);
        cylinderZTransform.addChild(cylinderZ);
        Transform3D tZRotate = new Transform3D();
        tZRotate.rotX(90.0 * Math.PI / 180.0);
        TransformGroup zAxis = new TransformGroup(tZRotate);
        zAxis.addChild(cylinderZTransform);
        group.addChild(zAxis);

        // Z-coordinate axis - end point
        Sphere sphereZ = new Sphere(0.03f);
        Transform3D tSphereZ = new Transform3D();
        tSphereZ.setTranslation(new Vector3f(0, 0, 1));
        TransformGroup tgSphereZ = new TransformGroup(tSphereZ);
        tgSphereZ.addChild(sphereZ);
        AppearanceHelper.setColor(sphereZ, new Color3f(0, 0, 1));
        group.addChild(tgSphereZ);

        return group;
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {
        // Create the central frame
        CG1Frame frame = new CG1Frame();
        // Add content to the scene graph
        frame.createSceneGraph();
    }
}
