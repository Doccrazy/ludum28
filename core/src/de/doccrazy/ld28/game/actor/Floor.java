package de.doccrazy.ld28.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;
import de.doccrazy.ld28.game.base.CollisionListener;

public class Floor extends Box2dActor implements CollisionListener {
	private static final Vector2 SIZE = new Vector2(20000f, 10f);

	public Floor(GameWorld world, float y) {
		super(world);
		// Create our body definition
		BodyDef groundBodyDef =new BodyDef();
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, y - SIZE.y));

		body = world.box2dWorld.createBody(groundBodyDef);
		body.setUserData(this);

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(SIZE.x, SIZE.y);
		// Create a fixture from our polygon shape and add it to our ground body
		body.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setOrigin(-SIZE.x, -SIZE.y);
		setPosition(body.getPosition().x, body.getPosition().y);
		setWidth(SIZE.x*2); setHeight(SIZE.y*2);
		float ar = getWidth() / getHeight();

		batch.draw(Resource.scifi2, getX() + getOriginX(), getY() + getOriginY(), getWidth(), getHeight(),
				0, 0, (int)(Resource.scifi2.getWidth() * ar * SIZE.y), (int)(Resource.scifi2.getHeight() * SIZE.y), false, false);
	}

	@Override
	public void beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (other.getUserData() instanceof Player) {
			world.spawnDeathPortal();
		}
	}

	@Override
	public void endContact(Body other) {
	}

}
