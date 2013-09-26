/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package main.java.edu.cg1.exercises.introduction;

import java.awt.Color;
import java.awt.font.TransformAttribute;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
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
public class CGAufgabenblatt1 extends JFrame {

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
	public CGAufgabenblatt1() {
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

		// Add a coordinate system to the scene.
		scene.addChild(createBird());

		// Assemble scene
		scene.compile();
		universe.addBranchGraph(scene);
	}

	/**
	 * Create a group to represent the coordinate system.
	 * 
	 * @return
	 */
	protected Node createBird() {

		Group group = new Group();
		group.addChild(createBody());
		group.addChild(createHead());
		group.addChild(createLeftWing());
		group.addChild(createRightWing());

		return group;
	}

	protected Node createBody() {
		Cylinder body = new Cylinder(0.15f, 0.6f);
		AppearanceHelper.setColor(body, new Color3f(0.45f, 1.0f, 0.58f));
		Transform3D bodyRotate = new Transform3D();
		bodyRotate.rotZ(90.0 * Math.PI / 180.0);
		TransformGroup bodyTG = new TransformGroup(bodyRotate);
		bodyTG.addChild(body);
		return bodyTG;
	}

	protected Node createHead() {
		Sphere head = new Sphere(0.17f);
		AppearanceHelper.setColor(head, new Color3f(0.4f, 0.98f, 0.56f));
		Transform3D headTranslate = new Transform3D();
		headTranslate.setTranslation(new Vector3f(-0.3f, 0f, 0f));
		TransformGroup headTG = new TransformGroup(headTranslate);

		headTG.addChild(head);
		headTG.addChild(createBeak());
		headTG.addChild(createEyes());

		return headTG;
	}
	
	protected Node createLeftWing(){
		Box wing = new Box(0.2f,0.03f,0.4f, new Appearance());
		AppearanceHelper.setColor(wing, new Color3f(0.45f, 0.0f, 0.58f));
		Transform3D wingfirstTranslate = new Transform3D();
		wingfirstTranslate.setTranslation(new Vector3f(0f, 0f, 0.4f));
		Transform3D wingTranslate = new Transform3D();
	
		wingTranslate.setTranslation(new Vector3f(0f, 0f, 0.15f));
		TransformGroup wingFirstTG = new TransformGroup(wingfirstTranslate);
		TransformGroup wingTG = new TransformGroup(wingTranslate);
		wingFirstTG.addChild(wing);
		wingTG.addChild(wingFirstTG);
		return wingTG;
	}
	
	protected Node createRightWing(){
		Box wing = new Box(0.2f,0.03f,0.4f, new Appearance());
		AppearanceHelper.setColor(wing, new Color3f(0.45f, 0.0f, 0.58f));
		Transform3D wingfirstTranslate = new Transform3D();
		wingfirstTranslate.setTranslation(new Vector3f(0f, 0f, -0.4f));
		Transform3D wingTranslate = new Transform3D();
	
		wingTranslate.setTranslation(new Vector3f(0f, 0f, -0.15f));
		TransformGroup wingFirstTG = new TransformGroup(wingfirstTranslate);
		TransformGroup wingTG = new TransformGroup(wingTranslate);
		wingFirstTG.addChild(wing);
		wingTG.addChild(wingFirstTG);
		return wingTG;
	}

	protected Node createBeak() {
		Cone beak = new Cone(0.12f, 0.4f);
		AppearanceHelper.setColor(beak, new Color3f(0.8f, 0.4f, 0.0f));
		Transform3D beakRotate = new Transform3D();
		beakRotate.rotZ(95.0 * Math.PI / 180.0);
		TransformGroup beakTGRot = new TransformGroup(beakRotate);
		beakTGRot.addChild(beak);
		Transform3D beakTranslate = new Transform3D();
		beakTranslate.setTranslation(new Vector3f(-0.2f, -0.05f, 0f));
		TransformGroup beakTGTrans = new TransformGroup(beakTranslate);
		beakTGTrans.addChild(beakTGRot);
		return beakTGTrans;
	}

	protected Node createEyes() {
		// Creating Spheres for the Eyes
		Sphere eyeLeft = new Sphere(0.07f);
		AppearanceHelper.setColor(eyeLeft, new Color3f(1f, 1f, 1f));
		Sphere eyeLeftInner = new Sphere(0.05f);
		AppearanceHelper.setColor(eyeLeftInner, new Color3f(0f, 0f, 0f));
		Sphere eyeRight = new Sphere(0.07f);
		AppearanceHelper.setColor(eyeRight, new Color3f(1f, 1f, 1f));
		Sphere eyeRightInner = new Sphere(0.05f);
		AppearanceHelper.setColor(eyeRightInner, new Color3f(0f, 0f, 0f));

		// Outer Transformation Group
		Transform3D outerTranslate = new Transform3D();
		outerTranslate.setTranslation(new Vector3f(-0.1f, 0.08f, 0f));
		TransformGroup outerTG = new TransformGroup(outerTranslate);

		// inner Translate - Transform3D Object
		Transform3D innerTranslate = new Transform3D();
		innerTranslate.setTranslation(new Vector3f(-0.03f, 0f, 0f));

		// Translating Left Eye
		Transform3D eyeLeftTranslate = new Transform3D();
		eyeLeftTranslate.setTranslation(new Vector3f(0f, 0f, -0.05f));
		TransformGroup eyeLeftTG = new TransformGroup(eyeLeftTranslate);

		TransformGroup eyeLeftInnerTG = new TransformGroup(innerTranslate);
		eyeLeftInnerTG.addChild(eyeLeftInner);

		eyeLeftTG.addChild(eyeLeft);
		eyeLeftTG.addChild(eyeLeftInnerTG);

		// Translating Right Eye
		Transform3D eyeRightTranslate = new Transform3D();
		eyeRightTranslate.setTranslation(new Vector3f(0f, 0f, 0.05f));
		TransformGroup eyeRightTG = new TransformGroup(eyeRightTranslate);

		TransformGroup eyeRightInnerTG = new TransformGroup(innerTranslate);
		eyeRightInnerTG.addChild(eyeRightInner);

		eyeRightTG.addChild(eyeRight);
		eyeRightTG.addChild(eyeRightInnerTG);

		// Combining Eyes to Outer TG
		outerTG.addChild(eyeLeftTG);
		outerTG.addChild(eyeRightTG);

		return outerTG;
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// Create the central frame
		CGAufgabenblatt1 frame = new CGAufgabenblatt1();
		// Add content to the scene graph
		frame.createSceneGraph();
	}
}
