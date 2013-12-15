package de.doccrazy.ld28.game.level;

import aurelienribon.bodyeditor.BodyEditorLoader;
import aurelienribon.bodyeditor.BodyEditorLoader.PolygonModel;
import aurelienribon.bodyeditor.BodyEditorLoader.RigidBodyModel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import de.doccrazy.ld28.core.Resource;

public enum ElementType {
	pillarBottom(Resource.pillar, 10),
	pillarMiddle(Resource.pillar, 10),
	pillarTop(Resource.pillar, 10),
	middle(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0, 0, false), new JoinPoint(0, 0.1f, true)}),
	middle2(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0.075f, 0, false), new JoinPoint(0.075f, 0.3f, true)}),
	pillarMedium(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0, 0, false), new JoinPoint(0, 0.3f, true)}),
	//split(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0.275f, 0, false), new JoinPoint(0, 0.275f, true), new JoinPoint(0.525f, 0.275f, true)}),
	//split2(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0.275f, 0.275f, true), new JoinPoint(0, 0, false), new JoinPoint(0.525f, 0, false)}),
	top2(Resource.pillar, 5, new JoinPoint[]{new JoinPoint(0.1f, 0, false)}),
	//lightPost(Resource.pillar, 5f, new JoinPoint[]{new JoinPoint(0, 0, false)}),
	bottom(Resource.pillar, 5f, new JoinPoint[]{new JoinPoint(0.075f, 0.15f, true)}),
	bottom2(Resource.pillar, 5f, new JoinPoint[]{new JoinPoint(0.1f, 0.1f, true)}),
	plank(Resource.pillar, 3f);

	private float width;
	private float height;
	private BodyEditorLoader loader;
	private float scale;
	private JoinPoint[] joints;

	ElementType(BodyEditorLoader loader, float scale, JoinPoint... joints) {
		this.loader = loader;
		this.scale = scale;
		this.joints = joints;
		for (JoinPoint joint : joints) {
			joint.x *= scale;
			joint.y *= scale;
		}
		calcBB();
	}

	private void calcBB() {
		RigidBodyModel rbModel = loader.getInternalModel().rigidBodies.get(toString());
		if (rbModel == null) throw new RuntimeException("Name '" + toString() + "' was not found.");
		Vector2 vec = new Vector2();
		width = 0;
		height = 0;
		for (PolygonModel poly : rbModel.polygons) {
			for (Vector2 pv : poly.vertices) {
				vec.set(pv).scl(scale);
				width = Math.max(width, vec.x);
				height = Math.max(height, vec.y);
			}
		}
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public JoinPoint[] getJoints() {
		return joints;
	}

	public void attach(Body body, FixtureDef fd) {
		loader.attachFixture(body, toString(), fd, scale);
	}
}
