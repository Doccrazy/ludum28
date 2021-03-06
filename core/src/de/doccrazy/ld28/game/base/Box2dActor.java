package de.doccrazy.ld28.game.base;

import java.util.ArrayList;
import java.util.List;

import box2dLight.Light;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.doccrazy.ld28.game.GameWorld;

public abstract class Box2dActor extends Actor {
	protected GameWorld world;
	protected Body body;
	protected List<Light> lights = new ArrayList<>();

	public Box2dActor(GameWorld world) {
		this.world = world;
		world.stage.addActor(this);
		world.onActorAdded(this);
	}

	public Body getBody() {
		return body;
	}

	@Override
	public boolean remove() {
		if (super.remove()) {
			for (Light light : lights) {
				light.remove();
			}
			if (body != null) {
				world.box2dWorld.destroyBody(body);
			}
			world.onActorRemoved(this);
			body = null;
			return true;
		}
		return false;
	}
}
