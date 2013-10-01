/**
 * Prof. Philipp Jenke
 * Hochschule fÃ¼r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package main.java.edu.cg1.exercises.introduction;

import java.awt.Color;
import java.awt.font.TransformAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
public class CopyOfCGAufgabenblatt1 extends JFrame {

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
	public CopyOfCGAufgabenblatt1() {
		// Create canvas object to draw on
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

		// The SimpleUniverse provides convenient default settings
		universe = new SimpleUniverse(canvas3D);
		universe.getViewingPlatform().setNominalViewingTransform();
		// Setup lighting
		addLight(universe);

		// Allow for mouse control
		OrbitBehavior ob = new OrbitBehavior(canvas3D);
		ob.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));
		universe.getViewingPlatform().setViewPlatformBehavior(ob);

		// Set the background color
		Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
		BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 100000);
		background.setApplicationBounds(sphere);
		scene.addChild(background);

		// Setup frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Einfuehrung in die Computergrafik");
		setSize(750, 750);
		getContentPane().add("Center", canvas3D);
		setVisible(true);
	}

	/**
	 * Setup the lights in the scene. Attention: The light need to be added to
	 * the scene before the scene in compiled (see createSceneGraph()).
	 */
	private void addLight(SimpleUniverse universe) {
		addPointLight(new Point3f(100, 100, 100));
		addPointLight(new Point3f(-100, -100, -100));
		addPointLight(new Point3f(100, -100, 100));
		addDirectionalLight(new Vector3f(0, 0, 100));
	}

	void addPointLight(Point3f position) {
		PointLight light = new PointLight();
		light.setPosition(position);
		light.setColor(new Color3f(1, 1, 1));
		light.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
		scene.addChild(light);
	}

	void addDirectionalLight(Vector3f direction) {
		DirectionalLight light = new DirectionalLight();
		light.setDirection(direction);
		light.setColor(new Color3f(1, 1, 1));
		light.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
		scene.addChild(light);
	}

	/**
	 * Create the default scene graph.
	 */
	protected void createSceneGraph() {
		// Add birds to the scene.
		scene.addChild(createBird(0.0, 0.75, 6.5f));
		scene.addChild(createBird(15.0, 0.5, 6.0f));
		scene.addChild(createBird(15.0, 0.5, 7.0f));
		scene.addChild(createBird(30.0, 0.25, 5.5f));
		scene.addChild(createBird(30.0, 0.25, 7.5f));
		scene.addChild(createBird(45.0, 0.15, 5.0f));
		scene.addChild(createBird(45.0, 0.15, 8.0f));
		// Add the landscape
		scene.addChild(createLandscape());

		// Assemble scene
		scene.compile();
		universe.addBranchGraph(scene);
	}

	protected Node createLandscape() {
		Group group = new Group();

		Box baseplate = new Box(11f, 0.1f, 11f, new Appearance());
		AppearanceHelper.setColor(baseplate, new Color3f(0.3f, 0.4f, 0f));

		group.addChild(baseplate);
		Group trees = new Group();
		// MAXIMALE ANZAHL BÄUME == 185
		placeTrees(trees, new ArrayList<PointOfTree>(), 20);
		group.addChild(trees);

		return group;
	}

	protected Node createTree(float xTrans, float zTrans) {
		Cylinder trunk = new Cylinder(0.2f, 1f);
		AppearanceHelper.setColor(trunk, new Color3f(0.6f, 0.4f, 0f));
		Sphere treetop = new Sphere(0.5f);
		AppearanceHelper.setColor(treetop, new Color3f(0.2f, 0.7f, 0f));

		Transform3D treeTranslate = new Transform3D();
		treeTranslate.setTranslation(new Vector3f(0f, 0.5f, 0f));

		Transform3D treePostion = new Transform3D();
		treePostion.setTranslation(new Vector3f(xTrans, 0f, zTrans));

		TransformGroup treeTopTG = new TransformGroup(treeTranslate);
		treeTopTG.addChild(treetop);
		TransformGroup treeTG = new TransformGroup(treeTranslate);
		treeTG.addChild(trunk);
		treeTG.addChild(treeTopTG);

		TransformGroup treePositionTG = new TransformGroup(treePostion);
		treePositionTG.addChild(treeTG);

		return treePositionTG;
	}

	protected void placeTrees(Group trees, List<PointOfTree> placedTrees, int numberOfTrees) {
		if (numberOfTrees == 0) {
			return;
		} else {
			float xTrans = new Random().nextFloat() * 20 - 10;
			float zTrans = new Random().nextFloat() * 20 - 10;

			if (checkPlaceOfTree(xTrans, zTrans, placedTrees)) {
				trees.addChild(createTree(xTrans, zTrans));
				placedTrees.add(new PointOfTree(xTrans, zTrans));
				placeTrees(trees, placedTrees, numberOfTrees - 1);
			} else {
				placeTrees(trees, placedTrees, numberOfTrees);
			}
		}

	}

	private boolean checkPlaceOfTree(float xTrans, float zTrans, List<PointOfTree> placedTrees) {
		if (placedTrees.isEmpty()) {
			return true;
		} else {
			for (PointOfTree elem : placedTrees) {
				float distance = (float) Math.sqrt(((xTrans - elem.getXTrans()) * (xTrans - elem.getXTrans())) + ((zTrans - elem.getZTrans()) * (zTrans - elem.getZTrans())));
				if (distance <= 1.2)
					return false;
			}
		}
		return true;
	}

	protected Node createBird(double angle, double scale, float translate) {
		Transform3D birdTranslate = new Transform3D();
		birdTranslate.rotX(-20.0 * Math.PI / 180.0);
		birdTranslate.setTranslation(new Vector3f(0f, 2.5f, translate));
		birdTranslate.setScale(scale);
		TransformGroup birdGroup = new TransformGroup(birdTranslate);

		birdGroup.addChild(createBody());
		birdGroup.addChild(createHead());
		birdGroup.addChild(createWings());
		birdGroup.addChild(createLegs());
		birdGroup.addChild(createTail());

		Transform3D birdOuterAnimation = new Transform3D();

		

		TransformGroup birdOuterAnimationTG = new TransformGroup(birdOuterAnimation);
		birdOuterAnimationTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		birdOuterAnimationTG.addChild(birdGroup);

		outerAnimation(birdOuterAnimationTG, angle);

		return birdOuterAnimationTG;
	}

	protected void outerAnimation(final TransformGroup animationTG, final double a) {
		class AnimateTask extends TimerTask {
			double angle = a;
			public void run() {
				Transform3D newRot = new Transform3D();
				newRot.rotY(angle * Math.PI / 180.0);
				animationTG.setTransform(newRot);
				if (angle <= 0.0)
					angle = 360.0;
				else
					angle -= 0.5;
			}
		}

		Timer t1 = new Timer();
		t1.schedule(new AnimateTask(), 0, 15);
	}

	protected void innerAnimation(final TransformGroup animationLeftWingTG, final TransformGroup animationRightWingTG) {
		class AnimateTask extends TimerTask {
			double angle = 0.0;
			boolean goesDown = true;

			public void run() {
				Transform3D newRotLeft = new Transform3D();
				newRotLeft.rotX(angle * Math.PI / 180.0);
				Transform3D newRotRight = new Transform3D();
				newRotRight.rotX(-angle * Math.PI / 180.0);
				animationLeftWingTG.setTransform(newRotLeft);
				animationRightWingTG.setTransform(newRotRight);
				if (angle <= -30) {
					angle = -29.8;
					goesDown = false;
				} else if (angle >= 30) {
					angle = 29.8;
					goesDown = true;
				} else {
					if (goesDown)
						angle -= 0.2;
					else
						angle += 0.2;
				}
			}
		}

		Timer t1 = new Timer();
		t1.schedule(new AnimateTask(), 0, 2);
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

	protected Node createWings() {
		Group wings = new Group();
		TransformGroup leftWing = createLeftWing();
		TransformGroup rightWing = createRightWing();
		wings.addChild(leftWing);
		wings.addChild(rightWing);
		
		// start of the inner animation
		innerAnimation(leftWing, rightWing);
		
		return wings;
	}

	protected TransformGroup createLeftWing() {
		Box wing = new Box(0.2f, 0.03f, 0.4f, new Appearance());
		AppearanceHelper.setColor(wing, new Color3f(0.45f, 0.0f, 0.58f));
		Transform3D wingfirstTranslate = new Transform3D();
		wingfirstTranslate.setTranslation(new Vector3f(0f, 0f, 0.4f));
		Transform3D wingTranslate = new Transform3D();

		wingTranslate.setTranslation(new Vector3f(0f, 0f, 0.15f));
		TransformGroup wingFirstTG = new TransformGroup(wingfirstTranslate);
		TransformGroup wingTG = new TransformGroup(wingTranslate);
		wingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		wingFirstTG.addChild(wing);
		wingTG.addChild(wingFirstTG);
		return wingTG;
	}

	protected TransformGroup createRightWing() {
		Box wing = new Box(0.2f, 0.03f, 0.4f, new Appearance());
		AppearanceHelper.setColor(wing, new Color3f(0.45f, 0.0f, 0.58f));
		Transform3D wingfirstTranslate = new Transform3D();
		wingfirstTranslate.setTranslation(new Vector3f(0f, 0f, -0.4f));
		Transform3D wingTranslate = new Transform3D();

		wingTranslate.setTranslation(new Vector3f(0f, 0f, -0.15f));
		TransformGroup wingFirstTG = new TransformGroup(wingfirstTranslate);
		TransformGroup wingTG = new TransformGroup(wingTranslate);
		wingFirstTG.addChild(wing);
		wingTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		wingTG.addChild(wingFirstTG);
		return wingTG;
	}

	protected Node createLegs() {
		Group legs = new Group();
		legs.addChild(createLeg(0.075f));
		legs.addChild(createLeg(-0.075f));
		return legs;
	}

	protected Node createLeg(float zTrans) {
		Cylinder leg = new Cylinder(0.03f, 0.15f);
		AppearanceHelper.setColor(leg, new Color3f(0.8f, 0.4f, 0.0f));
		Transform3D legTranslate = new Transform3D();
		legTranslate.setTranslation(new Vector3f(0.12f, -0.18f, zTrans));
		TransformGroup legTG = new TransformGroup(legTranslate);
		legTG.addChild(leg);
		legTG.addChild(createFeet());
		return legTG;
	}

	protected Node createFeet() {
		Box feet = new Box(0.1f, 0.03f, 0.06f, new Appearance());
		AppearanceHelper.setColor(feet, new Color3f(0.8f, 0.4f, 0.0f));
		Transform3D feetTranslate = new Transform3D();
		feetTranslate.setTranslation(new Vector3f(-0.025f, -0.075f, 0f));
		TransformGroup feetTG = new TransformGroup(feetTranslate);
		feetTG.addChild(feet);
		return feetTG;
	}

	protected Node createTail() {
		Box tail = new Box(0.03f, 0.18f, 0.12f, new Appearance());
		AppearanceHelper.setColor(tail, new Color3f(0.45f, 0.0f, 0.58f));
		Transform3D tailTranslate = new Transform3D();

		tailTranslate.rotZ(-8.0 * Math.PI / 180.0);
		tailTranslate.setTranslation(new Vector3f(0.315f, 0.046f, 0.0f));
		TransformGroup tailTG = new TransformGroup(tailTranslate);
		tailTG.addChild(tail);

		return tailTG;
	}

	// helping Class for the placement of Trees
	private class PointOfTree {

		private final float xTrans;
		private final float zTrans;

		public PointOfTree(float xTrans, float zTrans) {
			this.xTrans = xTrans;
			this.zTrans = zTrans;
		}

		public float getXTrans() {
			return xTrans;
		}

		public float getZTrans() {
			return zTrans;
		}

	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		// Create the central frame
		CopyOfCGAufgabenblatt1 frame = new CopyOfCGAufgabenblatt1();
		// Add content to the scene graph
		frame.createSceneGraph();
	}
}
