package de.doccrazy.ld28.game.actor;

import java.util.ArrayList;
import java.util.List;

import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;
import de.doccrazy.ld28.game.base.CollisionListener;
import de.doccrazy.ld28.game.base.PolyRenderer;

public class CheckpointActor extends Box2dActor implements CollisionListener {
	private static final float SCALE = 6f;

	private Body trigger;
	private boolean active;

	public CheckpointActor(GameWorld world, float x) {
		super(world);

		createBody(x);
		createTrigger(x);

		createLight(body, new Vector2(0f, 0.275f));
		createLight(body, new Vector2(0.875f, 0.275f));

		createLight(body, new Vector2(0.075f, 0.5f));
		createLight(body, new Vector2(0.8f, 0.5f));

		createLight(body, new Vector2(0f, 0.65f));
		createLight(body, new Vector2(0.875f, 0.65f));
	}

	private void createBody(float x) {
		// Create our body definition
		BodyDef bodyDef = new BodyDef();
		// Set its world position
		bodyDef.position.set(new Vector2(x, 0));
		bodyDef.type = BodyType.StaticBody;

        FixtureDef fd = new FixtureDef();
        fd.density = 50;
        fd.friction = 0.8f;
        fd.restitution = 0f;

        body = world.box2dWorld.createBody(bodyDef);
		body.setUserData(this);

		Resource.pillar.attachFixture(body, "checkpointBottom", fd, SCALE);
	}

	private void createTrigger(float x) {
		// Create our body definition
		BodyDef bodyDef = new BodyDef();
		// Set its world position
		bodyDef.position.set(new Vector2(x + (0.4375f * SCALE), 0.75f * SCALE));
		bodyDef.type = BodyType.StaticBody;

        trigger = world.box2dWorld.createBody(bodyDef);
        trigger.setUserData(this);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.3f * SCALE, 0.05f * SCALE);
		Fixture fix = trigger.createFixture(groundBox, 0.0f);
		fix.setSensor(true);
	}

	private void createLight(Body body, Vector2 vertex) {
		PointLight light = new PointLight(world.rayHandler, 10,
				new Color(0.5f, 0f, 0f, 1f),
				4f, 0f, 0f);
		light.setStaticLight(true);
		light.attachToBody(body, vertex.x * SCALE, vertex.y * SCALE);
		light.setXray(true);
		lights.add(light);
	}

	public void activate() {
		if (!active) {
			active = true;
			for (Light light : lights) {
				light.setColor(0f, 0.5f, 0f, 1f);
			}
			world.generateLevel();
		}
	}

	private List<Body> tmp = new ArrayList<>();
	@Override
	public void draw(Batch batch, float parentAlpha) {
		tmp.clear();
		tmp.add(body);
		batch.end();
		PolyRenderer.drawBodies(tmp, batch.getProjectionMatrix());
		batch.begin();
	}

	@Override
	public void beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (me == trigger && other.getUserData() == world.getPlayer()) {
			activate();
		}
	}

	@Override
	public void endContact(Body other) {
	}

	@Override
	public float getRight() {
		return body.getPosition().x + 0.875f * SCALE;
	}

}
